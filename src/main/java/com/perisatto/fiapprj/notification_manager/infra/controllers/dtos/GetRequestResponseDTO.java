package com.perisatto.fiapprj.notification_manager.infra.controllers.dtos;

import com.perisatto.fiapprj.notification_manager.domain.entities.RequestStatus;

public class GetRequestResponseDTO {
	private String id;
	private Integer interval;
	private String videoFileName;
	private String videoDownloadUrl;
	private RequestStatus status;
	private String remarks;
	
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
	public String getVideoDownloadUrl() {
		return videoDownloadUrl;
	}
	public void setVideoDownloadUrl(String videoDownloadUrl) {
		if(videoDownloadUrl == null ) {
			this.videoDownloadUrl = "";
		} else {
			this.videoDownloadUrl = videoDownloadUrl;
		}
	}
	public RequestStatus getStatus() {
		return status;
	}
	public void setStatus(RequestStatus status) {
		this.status = status;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		if(remarks == null) {
			this.remarks = "";
		} else {
			this.remarks = remarks;
		}
	}
}
