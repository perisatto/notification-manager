package com.perisatto.fiapprj.file_processor.infra.gateways;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perisatto.fiapprj.file_processor.domain.entities.Request;
import com.perisatto.fiapprj.file_processor.domain.entities.RequestStatus;
import com.perisatto.fiapprj.file_processor.infra.controllers.dtos.UpdateRequestResponseDTO;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@ActiveProfiles(value = "test")
public class RequestRepositoryApiTest {
	
	private RequestRepositoryApi requestRepositoryApi;
	
	public static MockWebServer mockBackEnd;
	
	@Mock
	private Environment env;
	
	AutoCloseable openMocks;
	
	@BeforeEach
	void setUp() throws IOException {
		openMocks = MockitoAnnotations.openMocks(this);
		requestRepositoryApi = new RequestRepositoryApi(env);
		mockBackEnd = new MockWebServer();
		mockBackEnd.start();
	}
	
	@AfterEach
	void tearDown() throws Exception {
		openMocks.close();
		mockBackEnd.shutdown();
	}
	
	@Test
	void givenValidId_thenUpdatesStatus() throws Exception {
		UpdateRequestResponseDTO updateRequestResponseDTO = new UpdateRequestResponseDTO();
		updateRequestResponseDTO.setId(UUID.randomUUID().toString());
		updateRequestResponseDTO.setInterval(10);
		updateRequestResponseDTO.setStatus(RequestStatus.COMPLETED);
		updateRequestResponseDTO.setVideoFileName("");
		
		String serverUrl = String.format("http://localhost:%s", mockBackEnd.getPort());
		
		when(env.getProperty(any(String.class)))
		.thenReturn(serverUrl);
		
		mockBackEnd.enqueue(new MockResponse().setBody(asJsonString(updateRequestResponseDTO))
				.addHeader("Content-Type", "application/json"));
		
		String owner = "me";
		Integer interval = 50;
		String videoFileName = "JohnCenaChairFight.mpeg";						
		
		Request requestData = new Request(owner, interval, videoFileName);
		requestData.setStatus(RequestStatus.COMPLETED);
		
		Boolean updatedRequest = requestRepositoryApi.updateRequest(requestData);
		
		assertThat(updatedRequest).isEqualTo(true);
	}
	
	@Test
	void givenValidId_thenUpdatesStatusAndRemarks() throws Exception {
		UpdateRequestResponseDTO updateRequestResponseDTO = new UpdateRequestResponseDTO();
		updateRequestResponseDTO.setId(UUID.randomUUID().toString());
		updateRequestResponseDTO.setInterval(10);
		updateRequestResponseDTO.setStatus(RequestStatus.COMPLETED);
		updateRequestResponseDTO.setVideoFileName("");
		
		String serverUrl = String.format("http://localhost:%s", mockBackEnd.getPort());
		
		when(env.getProperty(any(String.class)))
		.thenReturn(serverUrl);
		
		mockBackEnd.enqueue(new MockResponse().setBody(asJsonString(updateRequestResponseDTO))
				.addHeader("Content-Type", "application/json"));
		
		String owner = "me";
		Integer interval = 50;
		String videoFileName = "JohnCenaChairFight.mpeg";						
		
		Request requestData = new Request(owner, interval, videoFileName);
		requestData.setStatus(RequestStatus.COMPLETED);
		requestData.setRemarks("Remarks");
		
		Boolean updatedRequest = requestRepositoryApi.updateRequest(requestData);
		
		assertThat(updatedRequest).isEqualTo(true);
	}
	
	@Test
	void givenInvalidId_thenRefuseUpdatesStatus() throws Exception {
		
		String serverUrl = String.format("http://localhost:%s", mockBackEnd.getPort());
		
		when(env.getProperty(any(String.class)))
		.thenReturn(serverUrl);
		
		mockBackEnd.enqueue(new MockResponse().setBody("").setResponseCode(404)
				.addHeader("Content-Type", "application/json"));
		
		String owner = "me";
		Integer interval = 50;
		String videoFileName = "JohnCenaChairFight.mpeg";						
		
		Request requestData = new Request(owner, interval, videoFileName);
		requestData.setStatus(RequestStatus.COMPLETED);
		
		Boolean updatedRequest = requestRepositoryApi.updateRequest(requestData);
		
		assertThat(updatedRequest).isEqualTo(false);
	}
	
	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
