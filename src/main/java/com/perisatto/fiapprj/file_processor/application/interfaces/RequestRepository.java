package com.perisatto.fiapprj.file_processor.application.interfaces;

import java.util.Optional;
import java.util.Set;

import com.perisatto.fiapprj.file_processor.domain.entities.Request;

public interface RequestRepository {

	Request createRequest(Request request) throws Exception;

	Optional<Request> updateRequest(Request request) throws Exception;
	
	Optional<Request> getRequestById(String id) throws Exception;
	
	Set<Request> findAll(Integer limit, Integer offset, String owner) throws Exception;
}
