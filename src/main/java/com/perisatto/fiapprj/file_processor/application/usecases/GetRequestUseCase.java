package com.perisatto.fiapprj.file_processor.application.usecases;

import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.perisatto.fiapprj.file_processor.application.interfaces.FileRepositoryManagement;
import com.perisatto.fiapprj.file_processor.application.interfaces.RequestRepository;
import com.perisatto.fiapprj.file_processor.domain.entities.Request;
import com.perisatto.fiapprj.file_processor.domain.entities.RequestStatus;
import com.perisatto.fiapprj.file_processor.handler.exceptions.NotFoundException;
import com.perisatto.fiapprj.file_processor.handler.exceptions.ValidationException;


public class GetRequestUseCase {
	
	static final Logger logger = LogManager.getLogger(GetRequestUseCase.class);
	
	private final RequestRepository requestRepository;
	private final FileRepositoryManagement fileRepositoryManagement;
	
	public GetRequestUseCase(RequestRepository requestRepository, FileRepositoryManagement fileRepositoryManagement) {
		this.requestRepository = requestRepository;
		this.fileRepositoryManagement = fileRepositoryManagement;		
	}
	
	public Request getRequestById(String requestId) throws Exception {	
		Optional<Request> requestQuery = requestRepository.getRequestById(requestId);
		if(requestQuery.isPresent()) {
			
			Request request = requestQuery.get();
			
			if(request.getStatus() == RequestStatus.COMPLETED) {
				request.setVideoDownloadUrl(fileRepositoryManagement.generateDownloadFileURL(request.getId(), request.getVideoFileName()));
			}						
			return request;
			
		} else {
			throw new NotFoundException("cstm-1004", "Request not found");
		}
	}
	
	public Set<Request> findAllRequests(Integer limit, Integer page, String owner) throws Exception {
		
		if("me".equals(owner)) {
			owner = getUserIdFromToken();
		}
		
		if(limit==null) {
			limit = 10;
		}
		
		if(page==null) {
			page = 1;
		}
		
		validateFindAll(limit, page);		
		
		Set<Request> findResult = requestRepository.findAll(limit, page - 1, owner);		
		return findResult;
	}
	
	
	private String getUserIdFromToken() {
		// TODO create function to get userId attribute from authentication token
		return "me";
	}
	
	private void validateFindAll(Integer limit, Integer page) throws Exception {
		if (limit < 0 || limit > 50) {
			String message = "Invalid size parameter. Value must be greater than 0 and less than 50. Actual value: " + limit;
			logger.debug("\"validateFindAll\" | limit validation: " + message);
			throw new ValidationException("cstm-1006", message);			
		}
		
		if (page < 1) {
			String message = "Invalid page parameter. Value must be greater than 0. Actual value: " + page;
			logger.debug("\"validateFindAll\" | offset validation: " + message);
			throw new ValidationException("cstm-1007", message);	
		}
	}	
}
