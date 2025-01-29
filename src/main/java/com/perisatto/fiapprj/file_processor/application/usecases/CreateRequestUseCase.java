package com.perisatto.fiapprj.file_processor.application.usecases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.perisatto.fiapprj.file_processor.application.interfaces.FileRepositoryManagement;
import com.perisatto.fiapprj.file_processor.application.interfaces.RequestRepository;
import com.perisatto.fiapprj.file_processor.domain.entities.Request;
import com.perisatto.fiapprj.file_processor.domain.entities.RequestStatus;

public class CreateRequestUseCase {

	static final Logger logger = LogManager.getLogger(CreateRequestUseCase.class);
	
	private final RequestRepository requestRepository;
	private final FileRepositoryManagement fileRepositoryManagement;
	
	public CreateRequestUseCase(RequestRepository requestRepository, FileRepositoryManagement fileRepositoryManagement) {
		this.requestRepository = requestRepository;
		this.fileRepositoryManagement = fileRepositoryManagement;
	}
	
	public Request createRequest(String userId, Integer interval, String videoFileName) throws Exception {
		
		if("me".equals(userId)) {
			userId = getUserIdFromToken();
		}
		
		Request request = new Request(userId, interval, videoFileName);
		request.setStatus(RequestStatus.PENDING_UPLOAD);
		request.setVideoUploadUrl(fileRepositoryManagement.generateUploadFileURL(request.getId(), request.getVideoFileName()));
		requestRepository.createRequest(request);		
				
		return request;
	}

	private String getUserIdFromToken() {
		// TODO create function to get userId attribute from authentication token
		return "me";
	}
}
