package com.perisatto.fiapprj.file_processor.infra.controllers.dtos;

import com.perisatto.fiapprj.file_processor.domain.entities.RequestStatus;

public class CreateRequestResponseDTO {
	private String id;
	private Integer interval;
	private RequestStatus status;
	private String videoUploadUrl;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getInterval() {
		return interval;
	}
	public void setInterval(Integer interval) {
		this.interval = interval;
	}
	public RequestStatus getStatus() {
		return status;
	}
	public void setStatus(RequestStatus status) {
		this.status = status;
	}	
	public String getVideoUploadUrl() {
		return videoUploadUrl;
	}
	public void setVideoUploadUrl(String videoUploadUrl) {
		this.videoUploadUrl = videoUploadUrl;
	}
}
