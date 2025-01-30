package com.perisatto.fiapprj.file_processor.application.interfaces;

import java.io.File;
import java.io.InputStream;

public interface FileRepositoryManagement {

	InputStream getFileToProcess(String id, String videoFile) throws Exception;

	void writeProcessedFile(String id, String videoFile, File zipFile) throws Exception;
}
