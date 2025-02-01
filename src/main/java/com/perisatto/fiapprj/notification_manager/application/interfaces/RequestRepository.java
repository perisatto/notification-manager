package com.perisatto.fiapprj.notification_manager.application.interfaces;

import java.util.Optional;

import com.perisatto.fiapprj.notification_manager.domain.entities.Request;

public interface RequestRepository {

	Boolean updateRequest(Request request) throws Exception;
	
	Optional<Request> getRequestById(Long owner, String requestId) throws Exception;
}