package com.perisatto.fiapprj.file_processor.application.interfaces;

public interface FileRepositoryManagement {

	String generateUploadFileURL(String id, String videoFile);
	
	String generateDownloadFileURL(String id, String videoFile);
}
