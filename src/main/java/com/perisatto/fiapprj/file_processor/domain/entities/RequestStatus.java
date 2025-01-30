package com.perisatto.fiapprj.file_processor.domain.entities;

public enum RequestStatus {
	
	PENDING_UPLOAD(1L),
	PENDING_PROCESS(2L),
	PROCESSING(3L),
	ERROR(4L),
	COMPLETED(5L),
	EXPIRATED(6L);
	
	private Long id;
	
	RequestStatus(Long id){
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
}
