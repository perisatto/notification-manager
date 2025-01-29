package com.perisatto.fiapprj.file_processor.application.usecases;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.perisatto.fiapprj.file_processor.application.interfaces.FileProcessingManagement;
import com.perisatto.fiapprj.file_processor.application.interfaces.RequestRepository;
import com.perisatto.fiapprj.file_processor.domain.entities.Request;
import com.perisatto.fiapprj.file_processor.domain.entities.RequestStatus;
import com.perisatto.fiapprj.file_processor.handler.exceptions.NotFoundException;
import com.perisatto.fiapprj.file_processor.handler.exceptions.ValidationException;

public class UpdateRequestUseCase {
	
	static final Logger logger = LogManager.getLogger(UpdateRequestUseCase.class);
	
	private final RequestRepository requestRepository;	
	private final FileProcessingManagement fileProcessingManagement;
	
	public UpdateRequestUseCase(RequestRepository requestRepository,FileProcessingManagement fileProcessingManagement) {
		this.requestRepository = requestRepository;
		this.fileProcessingManagement = fileProcessingManagement;
	}
	
	public Request updateRequest(String id, String remarks, RequestStatus status) throws Exception {
		
		Optional<Request> request = requestRepository.getRequestById(id);
		
		if(request.isEmpty()) {
			throw new NotFoundException("rqst-1001", "Request not found");
		}
		
		Request updatedRequest = request.get();
		
		if(updatedRequest.getStatus() == status) {
			throw new ValidationException("rqst-1002", "Request already " + status.toString());
		} else {
			if (updatedRequest.getStatus().getId() > status.getId()) {
				throw new ValidationException("rqst-1003", "Request can't go back to " + status.toString() + " status");
			}
		}
		
		if(remarks != null) {
			updatedRequest.setRemarks(remarks);
		}
		
		if(status == RequestStatus.PENDING_PROCESS) {
			fileProcessingManagement.createProcessingRequest(updatedRequest);
		}
				
		updatedRequest.setStatus(status);
		
		requestRepository.updateRequest(updatedRequest);
		
		return updatedRequest;
	}
}
