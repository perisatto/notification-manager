package com.perisatto.fiapprj.file_processor.application.interfaces;

import java.io.File;
import java.io.InputStream;

public interface FileRepositoryManagement {

	String generateUploadFileURL(String id, String videoFile);
	
	String generateDownloadFileURL(String id, String videoFile);

	InputStream getFileToProcess(String id) throws Exception;

	void writeProcessedFile(String id, File zipFile) throws Exception;
}
