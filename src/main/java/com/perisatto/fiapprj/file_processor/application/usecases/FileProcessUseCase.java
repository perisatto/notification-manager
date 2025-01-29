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
import com.perisatto.fiapprj.file_processor.domain.entities.Request;

public class FileProcessUseCase {

	static final Logger logger = LogManager.getLogger(FileProcessUseCase.class);
	
	private final FileRepositoryManagement fileRepositoryManagement;
	
	public FileProcessUseCase(FileRepositoryManagement fileRepositoryManagement) {
		this.fileRepositoryManagement = fileRepositoryManagement;
	}
	
	public void generateImageFiles(Request request) throws Exception {
		Long interval = (long) (request.getInterval() * 1000000);		
		
		FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(fileRepositoryManagement.getFileToProcess(request.getId()));			
		
		Integer frameCount = 0;
		
		grabber.start();
		
		Long duration = grabber.getLengthInTime();
		
		File zipFile = File.createTempFile("frames", ".zip");
		zipFile.deleteOnExit();
		
        FileOutputStream fos = new FileOutputStream(zipFile);
        ZipOutputStream zos = new ZipOutputStream(fos);
		
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
		
		fileRepositoryManagement.writeProcessedFile(request.getId() ,zipFile);
		
		zos.close();
	    fos.close();
		
		zipFile.delete();
		
		grabber.stop();
		grabber.close();
	}
}
