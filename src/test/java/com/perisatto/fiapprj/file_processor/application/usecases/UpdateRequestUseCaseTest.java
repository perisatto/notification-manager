package com.perisatto.fiapprj.file_processor.application.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import com.perisatto.fiapprj.file_processor.application.interfaces.FileProcessingManagement;
import com.perisatto.fiapprj.file_processor.application.interfaces.RequestRepository;
import com.perisatto.fiapprj.file_processor.application.usecases.UpdateRequestUseCase;
import com.perisatto.fiapprj.file_processor.domain.entities.Request;
import com.perisatto.fiapprj.file_processor.domain.entities.RequestStatus;
import com.perisatto.fiapprj.file_processor.handler.exceptions.NotFoundException;
import com.perisatto.fiapprj.file_processor.handler.exceptions.ValidationException;

@ActiveProfiles(value = "test")
public class UpdateRequestUseCaseTest {
	
	private UpdateRequestUseCase updateRequestUseCase;
	
	@Mock
	private RequestRepository requestRepository;
	
	@Mock
	private FileProcessingManagement fileProcessingManagement;;
	
	AutoCloseable openMocks;
	
	@BeforeEach
	void setUp() {
		openMocks = MockitoAnnotations.openMocks(this);
		updateRequestUseCase = new UpdateRequestUseCase(requestRepository, fileProcessingManagement);
	}
	
	@AfterEach
	void tearDown() throws Exception {
		openMocks.close();
	}
	
	@Nested
	class UpdateRequest {
		
		@Test
		void givenNewStatusAndRemarks_thenUpdateRequest() throws Exception {
			String owner = "me";
			Integer interval = 10;
			String videoFileName = "JohnCenaChairFight.mpeg";						
			
			Request requestData = new Request(owner, interval, videoFileName);
			
			requestData.setStatus(RequestStatus.PENDING_UPLOAD);
			
			when(requestRepository.getRequestById(any(String.class)))
			.thenReturn(Optional.of(requestData));
			
			when(requestRepository.updateRequest(any(Request.class)))
			.thenAnswer(i -> {
				Optional<Request> request = Optional.of(i.getArgument(0));
				return request;
			});
			
			Request newRequest = updateRequestUseCase.updateRequest(requestData.getId(), "No remarks", RequestStatus.PENDING_PROCESS);
			
			assertThat(newRequest.getStatus()).isEqualTo(RequestStatus.PENDING_PROCESS);
		}
		
		@Test
		void givenNewStatus_thenUpdateRequest() throws Exception {
			String owner = "me";
			Integer interval = 10;
			String videoFileName = "JohnCenaChairFight.mpeg";						
			
			Request requestData = new Request(owner, interval, videoFileName);
			
			requestData.setStatus(RequestStatus.PENDING_UPLOAD);
			
			when(requestRepository.getRequestById(any(String.class)))
			.thenReturn(Optional.of(requestData));
			
			when(requestRepository.updateRequest(any(Request.class)))
			.thenAnswer(i -> {
				Optional<Request> request = Optional.of(i.getArgument(0));
				return request;
			});
			
			Request newRequest = updateRequestUseCase.updateRequest(requestData.getId(), null, RequestStatus.PENDING_PROCESS);
			
			assertThat(newRequest.getStatus()).isEqualTo(RequestStatus.PENDING_PROCESS);
		}
		
		@Test
		void givenCompletedStatus_thenUpdateRequest() throws Exception {
			String owner = "me";
			Integer interval = 10;
			String videoFileName = "JohnCenaChairFight.mpeg";						
			
			Request requestData = new Request(owner, interval, videoFileName);
			
			requestData.setStatus(RequestStatus.PENDING_UPLOAD);
			
			when(requestRepository.getRequestById(any(String.class)))
			.thenReturn(Optional.of(requestData));
			
			when(requestRepository.updateRequest(any(Request.class)))
			.thenAnswer(i -> {
				Optional<Request> request = Optional.of(i.getArgument(0));
				return request;
			});
			
			Request newRequest = updateRequestUseCase.updateRequest(requestData.getId(), null, RequestStatus.COMPLETED);
			
			assertThat(newRequest.getStatus()).isEqualTo(RequestStatus.COMPLETED);
		}
		
		@Test
		void givenInvalidStatus_thenRefusesUpdateRequest() throws Exception {
			String owner = "me";
			Integer interval = 10;
			String videoFileName = "JohnCenaChairFight.mpeg";						
			
			Request requestData = new Request(owner, interval, videoFileName);
			
			requestData.setStatus(RequestStatus.PENDING_UPLOAD);
			
			when(requestRepository.getRequestById(any(String.class)))
			.thenReturn(Optional.of(requestData));
			
			when(requestRepository.updateRequest(any(Request.class)))
			.thenAnswer(i -> {
				Optional<Request> request = Optional.of(i.getArgument(0));
				return request;
			});
			
			try {
				Request newRequest = updateRequestUseCase.updateRequest(requestData.getId(), null, RequestStatus.PENDING_UPLOAD);
			} catch (Exception e) {
				assertThatExceptionOfType(ValidationException.class);
				assertThat(e.getMessage()).contains("Request already");
			}			
		}
		
		@Test
		void givenLifeCycleStatus_thenRefusesUpdateRequest() throws Exception {
			String owner = "me";
			Integer interval = 10;
			String videoFileName = "JohnCenaChairFight.mpeg";						
			
			Request requestData = new Request(owner, interval, videoFileName);
			
			requestData.setStatus(RequestStatus.PENDING_PROCESS);
			
			when(requestRepository.getRequestById(any(String.class)))
			.thenReturn(Optional.of(requestData));
			
			when(requestRepository.updateRequest(any(Request.class)))
			.thenAnswer(i -> {
				Optional<Request> request = Optional.of(i.getArgument(0));
				return request;
			});
			
			try {
				Request newRequest = updateRequestUseCase.updateRequest(requestData.getId(), null, RequestStatus.PENDING_UPLOAD);
			} catch (Exception e) {
				assertThatExceptionOfType(ValidationException.class);
				assertThat(e.getMessage()).contains("Request can't go back");
			}			
		}
		
		@Test
		void givenInexistentRequest_thenRefusesUpdateRequest() throws Exception {
			String owner = "me";
			Integer interval = 10;
			String videoFileName = "JohnCenaChairFight.mpeg";						
			
			Request requestData = new Request(owner, interval, videoFileName);
			
			requestData.setStatus(RequestStatus.PENDING_PROCESS);
			
			when(requestRepository.getRequestById(any(String.class)))
			.thenReturn(Optional.empty());
			
			when(requestRepository.updateRequest(any(Request.class)))
			.thenAnswer(i -> {
				Optional<Request> request = Optional.of(i.getArgument(0));
				return request;
			});
			
			try {
				Request newRequest = updateRequestUseCase.updateRequest(requestData.getId(), null, RequestStatus.PENDING_UPLOAD);
			} catch (Exception e) {
				assertThatExceptionOfType(NotFoundException.class);
				assertThat(e.getMessage()).contains("Request not found");
			}			
		}
	}
}
