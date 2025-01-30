package com.perisatto.fiapprj.file_processor.domain.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = "test")
public class RequestTest {
	
	@Test
	void givenValidData_thenCreateRequest() throws Exception {
		
		Long idRequest = 1L;
		String owner = "me";
		Integer interval = 10;
		RequestStatus status = RequestStatus.PENDING_UPLOAD;
		String videoFileName = "JohnCenaChairFight.mpeg";
		String remarks = "Remark";
		String videoUploadUrl = "http://localhost";
		String videoDownloadUrl = "http://localhost";				
				
		Request request = new Request(owner, interval, videoFileName);
		
		request.setIdRequest(idRequest);
		request.setOwner(owner);
		request.setInterval(interval);
		request.setStatus(status);
		request.setVideoFileName(videoFileName);
		request.setVideoUploadUrl(videoUploadUrl);
		request.setVideoDownloadUrl(videoDownloadUrl);
		request.setRemarks(remarks);
		
		assertThat(request.getOwner()).isEqualTo(owner);
		assertThat(request.getInterval()).isEqualTo(interval);
		assertThat(request.getVideoFileName()).isEqualTo(videoFileName);
	}
	
	@Test
	void getRequestData() throws Exception {
		Long idRequest = 1L;
		String owner = "me";
		Integer interval = 10;
		RequestStatus status = RequestStatus.PENDING_UPLOAD;
		String videoFileName = "JohnCenaChairFight.mpeg";
		String remarks = "Remark";
		String videoUploadUrl = "http://localhost";
		String videoDownloadUrl = "http://localhost";				
				
		Request request = new Request(owner, interval, videoFileName);
		
		request.setIdRequest(idRequest);
		request.setOwner(owner);
		request.setInterval(interval);
		request.setStatus(status);
		request.setVideoFileName(videoFileName);
		request.setVideoUploadUrl(videoUploadUrl);
		request.setVideoDownloadUrl(videoDownloadUrl);
		request.setRemarks(remarks);
		
		idRequest = request.getIdRequest();
		String id = request.getId();
		owner = request.getOwner();
		interval = request.getInterval();
		status = request.getStatus();
		videoFileName = request.getVideoFileName();
		remarks = request.getRemarks();
		videoUploadUrl = request.getVideoUploadUrl();
		videoDownloadUrl = request.getVideoDownloadUrl();
		
		assertThat(id).isEqualTo(request.getId());
	}
	
	@Test
	void givenNullOwner_thenRefusesToCreateRequest() {
		try {
			Integer interval = 10;
			String videoFileName = "JohnCenaChairFight.mpeg";			
					
			Request request = new Request(null, interval, videoFileName);
		} catch (Exception e) {
			assertThat(e.getMessage()).contains("null or empty owner");
		}
	}
	
	@Test
	void givenBlankOwner_thenRefusesToCreateRequest() {
		try {
			String owner = " ";
			Integer interval = 10;
			String videoFileName = "JohnCenaChairFight.mpeg";			
					
			Request request = new Request(owner, interval, videoFileName);
		} catch (Exception e) {
			assertThat(e.getMessage()).contains("null or empty owner");
		}
	}
	
	@Test
	void givenEmptyOwner_thenRefusesToCreateRequest() {
		try {
			String owner = "";
			Integer interval = 10;
			String videoFileName = "JohnCenaChairFight.mpeg";			
					
			Request request = new Request(owner, interval, videoFileName);
		} catch (Exception e) {
			assertThat(e.getMessage()).contains("null or empty owner");
		}
	}
	
	@Test
	void givenNullInterval_thenRefusesToCreateRequest() {
		try {
			String owner = "me";
			Integer interval = 10;
			String videoFileName = "JohnCenaChairFight.mpeg";			
					
			Request request = new Request(owner, null, videoFileName);
		} catch (Exception e) {
			assertThat(e.getMessage()).contains("invalid interval value");
		}
	}
	
	@Test
	void givenInvalidInterval_thenRefusesToCreateRequest() {
		try {
			String owner = "me";
			Integer interval = 5;
			String videoFileName = "JohnCenaChairFight.mpeg";			
					
			Request request = new Request(owner, interval, videoFileName);
		} catch (Exception e) {
			assertThat(e.getMessage()).contains("invalid interval value");
		}
	}
	
	@Test
	void givenNullVideoFileName_thenRefusesToCreateRequest() {
		try {
			String owner = "me";
			Integer interval = 10;
			String videoFileName = "JohnCenaChairFight.mpeg";			
					
			Request request = new Request(owner, interval, null);
		} catch (Exception e) {
			assertThat(e.getMessage()).contains("empty, null or blank videoFileName");
		}
	}
	
	@Test
	void givenBlankVideoFileName_thenRefusesToCreateRequest() {
		try {
			String owner = "me";
			Integer interval = 10;
			String videoFileName = " ";			
					
			Request request = new Request(owner, interval, videoFileName);
		} catch (Exception e) {
			assertThat(e.getMessage()).contains("empty, null or blank videoFileName");
		}
	}
	
	@Test
	void givenEmptyVideoFileName_thenRefusesToCreateRequest() {
		try {
			String owner = "me";
			Integer interval = 10;
			String videoFileName = "";			
					
			Request request = new Request(owner, interval, videoFileName);
		} catch (Exception e) {
			assertThat(e.getMessage()).contains("empty, null or blank videoFileName");
		}
	}
}
