package com.perisatto.fiapprj.file_processor.application.interfaces;

import com.perisatto.fiapprj.file_processor.domain.entities.Request;

public interface RequestRepository {

	Boolean updateRequest(Request request) throws Exception;
}