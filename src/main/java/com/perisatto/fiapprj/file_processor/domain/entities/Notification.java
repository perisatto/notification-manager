package com.perisatto.fiapprj.file_processor.domain.entities;

import java.io.Serializable;

public class Notification implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String requestId;
	private String message;
	
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}	
}
