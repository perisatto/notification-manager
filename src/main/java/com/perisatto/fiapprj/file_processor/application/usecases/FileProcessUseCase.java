package com.perisatto.fiapprj.file_processor.application.usecases;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import com.perisatto.fiapprj.file_processor.application.interfaces.FileRepositoryManagement;
import com.perisatto.fiapprj.file_processor.application.interfaces.NotificationManagement;
import com.perisatto.fiapprj.file_processor.application.interfaces.RequestRepository;
import com.perisatto.fiapprj.file_processor.domain.entities.Notification;
import com.perisatto.fiapprj.file_processor.domain.entities.Request;
import com.perisatto.fiapprj.file_processor.domain.entities.RequestStatus;

public class FileProcessUseCase {

	static final Logger logger = LogManager.getLogger(FileProcessUseCase.class);

	private final FileRepositoryManagement fileRepositoryManagement;
	private final RequestRepository requestRepository;
	private final NotificationManagement notificationManagement;

	public FileProcessUseCase(FileRepositoryManagement fileRepositoryManagement, RequestRepository requestRepository, NotificationManagement notificationManagement) {
		this.fileRepositoryManagement = fileRepositoryManagement;
		this.requestRepository = requestRepository;
		this.notificationManagement = notificationManagement;
	}

	public void generateImageFiles(Request request) throws Exception {

		logger.info("Processing new request. Request id: " + request.getId());

		Long interval = (long) (request.getInterval() * 1000000);

		FFmpegFrameGrabber grabber;

		try {		
			grabber = new FFmpegFrameGrabber(fileRepositoryManagement.getFileToProcess(request.getId(), request.getVideoFileName()));
			grabber.start();
		} catch (Exception e) {			
			reportError(request, "Error getting file to process. Verify the file type (must be a video).");
			return;
		}

		Integer frameCount = 0;

		Long duration = grabber.getLengthInTime();
		
		if(interval > duration) {
			reportError(request, "Error processing video file. Interval must be smaller than duration.");
			
			grabber.stop();
			grabber.close();
			
			return;
		}		
		
		File zipFile = File.createTempFile("frames", ".zip");

		FileOutputStream fos = new FileOutputStream(zipFile);
		ZipOutputStream zos = new ZipOutputStream(fos);

		logger.info("Starting video file processing...");

		try {
			for (Long currentTimestamp = interval; currentTimestamp < duration; currentTimestamp += interval) {
				grabber.setTimestamp(currentTimestamp);
				Frame frame = grabber.grabImage();			
				Java2DFrameConverter converter = new Java2DFrameConverter();
				BufferedImage image = converter.convert(frame);	

				ZipEntry entry = new ZipEntry("frame_" + frameCount + ".png");

				zos.putNextEntry(entry);

				ImageIO.write(image, "png", zos);
				zos.closeEntry();

				frameCount++;			
				converter.close();			
			}
		} catch (Exception e) {
			reportError(request, "Error processing video file. Unable to extract frames");

			zos.close();
			fos.close();
			
			zipFile.delete();

			grabber.stop();
			grabber.close();
			
			return;
		} finally {
			zos.close();
			fos.close();
		}

		logger.info("Video file processing finished. Total frames captured: " + frameCount);
		logger.info("Uploading compressed file...");
		
		try { 
			fileRepositoryManagement.writeProcessedFile(request.getId(), request.getVideoFileName(), zipFile);
		} catch (Exception e) {
			reportError(request, "Error publishing compressed file");

			zipFile.delete();

			grabber.stop();
			grabber.close();
			
			return;
		}
			
		zipFile.delete();

		grabber.stop();
		grabber.close();

		request.setStatus(RequestStatus.COMPLETED);

		requestRepository.updateRequest(request);

		logger.info("Request processed.");
	}

	private void reportError(Request request, String errorMessage) throws Exception {
		logger.error("Error processing request. Ending processing...");
		request.setRemarks(errorMessage);
		request.setStatus(RequestStatus.ERROR);
		requestRepository.updateRequest(request);
		
		Notification notification = new Notification();
		notification.setRequestId(request.getId());
		notification.setMessage(errorMessage);
		
		notificationManagement.createNotification(notification);
	}
}
