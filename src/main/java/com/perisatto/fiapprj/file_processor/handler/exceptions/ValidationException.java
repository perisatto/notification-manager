package com.perisatto.fiapprj.file_processor.handler.exceptions;

public class ValidationException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private String errorCode;
	
	public ValidationException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}
}
