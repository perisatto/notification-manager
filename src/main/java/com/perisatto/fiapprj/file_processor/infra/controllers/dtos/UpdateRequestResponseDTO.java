package com.perisatto.fiapprj.file_processor.infra.controllers.dtos;

import com.perisatto.fiapprj.file_processor.domain.entities.RequestStatus;

public class UpdateRequestResponseDTO {
	private String id;
	private Integer interval;
	private String videoFileName;
	private RequestStatus status;
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
	public String getVideoFileName() {
		return videoFileName;
	}
	public void setVideoFileName(String videoFileName) {
		this.videoFileName = videoFileName;
	}
	public RequestStatus getStatus() {
		return status;
	}
	public void setStatus(RequestStatus status) {
		this.status = status;
	}
	
	
}
