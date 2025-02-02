package com.perisatto.fiapprj.notification_manager.domain.entities;

import java.io.Serializable;

public class Notification implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long owner;
	private String requestId;
	private String message;
	
	
	public Long getOwner() {
		return owner;
	}
	public void setOwner(Long owner) {
		this.owner = owner;
	}
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
