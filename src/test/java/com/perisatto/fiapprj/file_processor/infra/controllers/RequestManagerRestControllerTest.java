package com.perisatto.fiapprj.file_processor.infra.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perisatto.fiapprj.file_processor.application.usecases.CreateRequestUseCase;
import com.perisatto.fiapprj.file_processor.application.usecases.GetRequestUseCase;
import com.perisatto.fiapprj.file_processor.application.usecases.UpdateRequestUseCase;
import com.perisatto.fiapprj.file_processor.domain.entities.Request;
import com.perisatto.fiapprj.file_processor.domain.entities.RequestStatus;
import com.perisatto.fiapprj.file_processor.infra.controllers.RequestManagerRestController;
import com.perisatto.fiapprj.file_processor.infra.controllers.dtos.CreateRequestRequestDTO;
import com.perisatto.fiapprj.file_processor.infra.controllers.dtos.UpdateRequestRequestDTO;

@ActiveProfiles(value = "test")
public class RequestManagerRestControllerTest {
	
	private MockMvc mockMvc;
	
	@Mock
	private CreateRequestUseCase createRequestUseCase;
	
	@Mock
	private UpdateRequestUseCase updateRequestUseCase;
	
	@Mock
	private GetRequestUseCase getRequestUseCase;
	
	@Mock
	private Properties requestProperties;
	
	AutoCloseable openMocks;
	
	@BeforeEach
	void setUp() {
		openMocks = MockitoAnnotations.openMocks(this);
		RequestManagerRestController requestManagerRestController = new RequestManagerRestController(createRequestUseCase, requestProperties, updateRequestUseCase, getRequestUseCase);
		
		mockMvc = MockMvcBuilders.standaloneSetup(requestManagerRestController)
				.addFilter((request, response, chain) -> {
					response.setCharacterEncoding("UTF-8");
					chain.doFilter(request, response);
				}, "/*")
				.build();
	}
	
	@AfterEach
	void teadDown() throws Exception {
		openMocks.close();
	}
	
	@Nested
	class CreateRequest {
		
		@Test
		void givenValidData_CreateRequest() throws Exception {
			String owner = "me";
			Integer interval = 10;
			String videoFileName = "JohnCenaChairFight.mpeg";
			
			Request request = new Request(owner, interval, videoFileName);
			request.setStatus(RequestStatus.PENDING_UPLOAD);
			
			when(createRequestUseCase.createRequest(any(String.class), any(Integer.class), any(String.class)))
			.thenReturn(request);
			
			CreateRequestRequestDTO createRequestRequestDTO = new CreateRequestRequestDTO();
			
			createRequestRequestDTO.setInterval(10);
			createRequestRequestDTO.setVideoFileName("JohnCenaChairFight.mpeg");
			
			mockMvc.perform(post("/users/{userId}/requests", owner)
					.contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(createRequestRequestDTO)))
			.andExpect(status().isCreated());		
			
			verify(createRequestUseCase, times(1)).createRequest(any(String.class), any(Integer.class), any(String.class));
		}
	}
	
	@Nested
	class UpdateRequest {
		
		@Test
		void givenValidStatus_thenUpdateRequest() throws Exception {
			String owner = "me";
			Integer interval = 10;
			String videoFileName = "JohnCenaChairFight.mpeg";
			
			Request request = new Request(owner, interval, videoFileName);
			request.setStatus(RequestStatus.PENDING_PROCESS);
			
			when(updateRequestUseCase.updateRequest(any(String.class), any(String.class), any(RequestStatus.class)))
			.thenReturn(request);
			
			UpdateRequestRequestDTO updateRequestRequestDTO = new UpdateRequestRequestDTO();
			updateRequestRequestDTO.setStatus("PENDING_PROCESS");
			updateRequestRequestDTO.setRemarks("No more remarks");
			
			mockMvc.perform(patch("/users/{userId}/requests/{requestId}", owner, UUID.randomUUID().toString())
					.contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(updateRequestRequestDTO)))
			.andExpect(status().isOk());
			
			verify(updateRequestUseCase, times(1)).updateRequest(any(String.class), any(String.class), any(RequestStatus.class));
		}
	}
	
	@Nested
	class GetRequest {
		
		@Test
		void givenValidId_thenRetrieveRequest() throws Exception {
			String owner = "me";
			Integer interval = 10;
			String videoFileName = "JohnCenaChairFight.mpeg";
			
			Request request = new Request(owner, interval, videoFileName);
			request.setStatus(RequestStatus.PENDING_PROCESS);
			
			when(getRequestUseCase.getRequestById(any(String.class)))
			.thenReturn(request);
			
			mockMvc.perform(get("/users/{userId}/requests/{requestId}", owner, UUID.randomUUID().toString())
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
			
			verify(getRequestUseCase, times(1)).getRequestById(any(String.class));
		}
		
		@Test
		void listRequestData() throws Exception {
		
			when(getRequestUseCase.findAllRequests(any(Integer.class), any(Integer.class), any(String.class)))
			.thenAnswer(i -> {
				Set<Request> result = new LinkedHashSet<Request>();
				Request requestData1 = new Request("me", 10, "JohnCenaChairFight.mpeg");
				requestData1.setStatus(RequestStatus.PENDING_UPLOAD);
				Request requestData2 = new Request("me", 10, "JohnCenaChairFight.mpeg");
				requestData2.setStatus(RequestStatus.COMPLETED);
				result.add(requestData1);
				result.add(requestData2);
				return result;
			});
			
			mockMvc.perform(get("/users/{userId}/requests", "me")
					.contentType(MediaType.APPLICATION_JSON)
					.param("_page", "1")
					.param("_size", "50"))
			.andExpect(status().isOk());
			
			verify(getRequestUseCase, times(1)).findAllRequests(any(Integer.class), any(Integer.class), any(String.class));
		}
	}
	
	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
