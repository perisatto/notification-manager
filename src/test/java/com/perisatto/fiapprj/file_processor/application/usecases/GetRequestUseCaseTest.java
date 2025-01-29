package com.perisatto.fiapprj.file_processor.application.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import com.perisatto.fiapprj.file_processor.application.interfaces.FileRepositoryManagement;
import com.perisatto.fiapprj.file_processor.application.interfaces.RequestRepository;
import com.perisatto.fiapprj.file_processor.application.usecases.GetRequestUseCase;
import com.perisatto.fiapprj.file_processor.domain.entities.Request;
import com.perisatto.fiapprj.file_processor.domain.entities.RequestStatus;
import com.perisatto.fiapprj.file_processor.handler.exceptions.ValidationException;

@ActiveProfiles(value = "test")
public class GetRequestUseCaseTest {
	private GetRequestUseCase getRequestUseCase;
	
	@Mock
	private RequestRepository requestRepository;
	
	@Mock
	private FileRepositoryManagement fileRepositoryManagement;
	
	AutoCloseable openMocks;
	
	@BeforeEach
	void setUp() {
		openMocks = MockitoAnnotations.openMocks(this);
		getRequestUseCase = new GetRequestUseCase(requestRepository, fileRepositoryManagement);
	}
	
	@AfterEach
	void tearDown() throws Exception {
		openMocks.close();
	}
	
	@Nested
	class GetRequest {
		
		@Test
		void givenValidId_theGetRequestData() throws Exception {
			String owner = "me";
			Integer interval = 10;
			String videoFileName = "JohnCenaChairFight.mpeg";						
			
			Request requestData = new Request(owner, interval, videoFileName);
			
			requestData.setStatus(RequestStatus.PENDING_UPLOAD);
			
			when(requestRepository.getRequestById(any(String.class)))
			.thenReturn(Optional.of(requestData));
			
			Request request = getRequestUseCase.getRequestById(requestData.getId());
			
			assertThat(request.getId()).isEqualTo(requestData.getId());
		}
		
		@Test
		void givenCompletedRequestId_theGetRequestData() throws Exception {
			String owner = "me";
			Integer interval = 10;
			String videoFileName = "JohnCenaChairFight.mpeg";						
			
			Request requestData = new Request(owner, interval, videoFileName);
			
			requestData.setStatus(RequestStatus.COMPLETED);
			
			when(requestRepository.getRequestById(any(String.class)))
			.thenReturn(Optional.of(requestData));
			
			when(fileRepositoryManagement.generateDownloadFileURL(any(String.class), any(String.class)))
			.thenReturn("http://localhost");
			
			Request request = getRequestUseCase.getRequestById(requestData.getId());
			
			assertThat(request.getVideoDownloadUrl()).isEqualTo("http://localhost");
		}
		
		@Test
		void givenInexistentRequestId_theGetRequestData() throws Exception {
			
			when(requestRepository.getRequestById(any(String.class)))
			.thenReturn(Optional.empty());
			
			try {
			Request request = getRequestUseCase.getRequestById(UUID.randomUUID().toString());
			} catch (Exception e) {
				assertThatExceptionOfType(ValidationException.class);
				assertThat(e.getMessage()).contains("Request not found");
			}
		}
		
		@Test
		void listRequests() throws Exception {
			when(requestRepository.findAll(any(Integer.class), any(Integer.class), any(String.class)))
			.thenAnswer(i -> {
				Set<Request> result = new LinkedHashSet<Request>();
				Request requestData1 = new Request("me", 10, "JohnCenaChairFight.mpeg");
				Request requestData2 = new Request("me", 10, "JohnCenaChairFight.mpeg"); 
				result.add(requestData1);
				result.add(requestData2);
				return result;
			});
			
			Set<Request> result = getRequestUseCase.findAllRequests(null, null, "me");
			
			assertThat(result.size()).isEqualTo(2);
		}		
		
		@Test
		void givenInvalidParameters_RefusesListRequest() throws Exception {
			try {
				getRequestUseCase.findAllRequests(100, null, "me");
			} catch (ValidationException e) {
				assertThat(e.getMessage()).contains("Invalid size parameter");
			}
			
			try {
				getRequestUseCase.findAllRequests(-1, null, "me");
			} catch (ValidationException e) {
				assertThat(e.getMessage()).contains("Invalid size parameter");
			}
			
			try {
				getRequestUseCase.findAllRequests(null, 0, "me");
			} catch (ValidationException e) {
				assertThat(e.getMessage()).contains("Invalid page parameter");
			}
		}
	}
}
