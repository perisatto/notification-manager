package com.perisatto.fiapprj.file_processor.application.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import com.perisatto.fiapprj.file_processor.application.interfaces.FileRepositoryManagement;
import com.perisatto.fiapprj.file_processor.application.interfaces.RequestRepository;
import com.perisatto.fiapprj.file_processor.application.usecases.CreateRequestUseCase;
import com.perisatto.fiapprj.file_processor.domain.entities.Request;

@ActiveProfiles(value = "test")
public class CreateRequestUseCaseTest {

	private CreateRequestUseCase createRequestUseCase;
	
	@Mock
	private RequestRepository requestRepository;
	
	@Mock
	private FileRepositoryManagement fileRepositoryManagement;
	
	AutoCloseable openMocks;
	
	@BeforeEach
	void setUp() {
		openMocks = MockitoAnnotations.openMocks(this);
		createRequestUseCase = new CreateRequestUseCase(requestRepository, fileRepositoryManagement);
	}
	
	@AfterEach
	void tearDown() throws Exception {
		openMocks.close();
	}
	
	@Nested
	class CreateRequestd {
		
		@Test
		void givenValidData_thenCreateRequest() throws Exception {
			when(requestRepository.createRequest(any(Request.class)))
			.thenAnswer(i -> i.getArgument(0));
			
			when(fileRepositoryManagement.generateUploadFileURL(any(String.class), any(String.class)))
			.thenAnswer(i -> i.getArgument(0));
			
			String owner = "me";
			Integer interval = 10;
			String videoFileName = "JohnCenaChairFight.mpeg";
			
			Request request = createRequestUseCase.createRequest(owner, interval, videoFileName);
			
			assertThat(request.getOwner()).isEqualTo(owner);
			assertThat(request.getInterval()).isEqualTo(interval);
			assertThat(request.getVideoFileName()).isEqualTo(videoFileName);
		}
		
	}
}
