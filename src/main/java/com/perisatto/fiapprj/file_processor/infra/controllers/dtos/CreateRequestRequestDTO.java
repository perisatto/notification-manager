package com.perisatto.fiapprj.file_processor.infra.controllers.dtos;

public class CreateRequestRequestDTO {
	private Integer interval;
	private String videoFileName;
	
	public Integer getInterval() {
		return interval;
	}
	public void setInterval(Integer interval) {
		this.interval = interval;
	}
	public String getVideoFileName() {
		return videoFileName;
	}
	public void setVideoFileName(String videoFile) {
		this.videoFileName = videoFile;
	}
	
	
}
