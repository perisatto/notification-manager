package com.perisatto.fiapprj.file_processor.aplication.usecases;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import com.perisatto.fiapprj.file_processor.application.interfaces.FileRepositoryManagement;
import com.perisatto.fiapprj.file_processor.application.interfaces.NotificationManagement;
import com.perisatto.fiapprj.file_processor.application.interfaces.RequestRepository;
import com.perisatto.fiapprj.file_processor.application.usecases.FileProcessUseCase;
import com.perisatto.fiapprj.file_processor.domain.entities.Request;
import com.perisatto.fiapprj.file_processor.domain.entities.RequestStatus;

@ActiveProfiles(value = "test")
public class FileProcessUseCaseTest {

	private FileProcessUseCase fileProcessUseCase;
	
	@Mock
	private FileRepositoryManagement fileRepositoryManagement;
	
	@Mock
	private RequestRepository requestRepository;
	
	@Mock
	private NotificationManagement notificationManagement;
	
	AutoCloseable openMocks;
	
	@BeforeEach
	void setUp() {
		openMocks = MockitoAnnotations.openMocks(this);
		fileProcessUseCase = new FileProcessUseCase(fileRepositoryManagement, requestRepository, notificationManagement);
	}
	
	@AfterEach
	void tearDown() throws Exception {
		openMocks.close();
	}
	
	@Test
	void givenValidData_thenProcessFile() throws Exception {		
		InputStream inputStream = new FileInputStream("src/test/resources/video-file/dummy.mkv");
		
		when(fileRepositoryManagement.getFileToProcess(any(String.class), any(String.class)))
		.thenReturn(inputStream);
		
		when(requestRepository.updateRequest(any(Request.class)))
		.thenReturn(true);
		
		
		String owner = "me";
		Integer interval = 10;
		String videoFileName = "JohnCenaChairFight.mpeg";						
		
		Request requestData = new Request(owner, interval, videoFileName);
		
		requestData.setStatus(RequestStatus.PENDING_PROCESS);
		
		fileProcessUseCase.generateImageFiles(requestData);		
	}
	
	@Test
	void givenInvalidInterval_thenRefusesToProcessFile() throws Exception {
		InputStream inputStream = new FileInputStream("src/test/resources/video-file/dummy.mkv");
		
		when(fileRepositoryManagement.getFileToProcess(any(String.class), any(String.class)))
		.thenReturn(inputStream);
		
		String owner = "me";
		Integer interval = 50;
		String videoFileName = "JohnCenaChairFight.mpeg";						
		
		Request requestData = new Request(owner, interval, videoFileName);
		
		requestData.setStatus(RequestStatus.PENDING_PROCESS);
		
		fileProcessUseCase.generateImageFiles(requestData);	
	}
	
	@Test
	void givenValidData_thenRefusesUploadFile() throws Exception {
		InputStream inputStream = new FileInputStream("src/test/resources/video-file/dummy.mkv");
		
		when(fileRepositoryManagement.getFileToProcess(any(String.class), any(String.class)))
		.thenReturn(inputStream);
		
		when(requestRepository.updateRequest(any(Request.class)))
		.thenReturn(true);

		doThrow(new Exception()).when(fileRepositoryManagement).writeProcessedFile(any(String.class), any(String.class), any(File.class));
		
		String owner = "me";
		Integer interval = 10;
		String videoFileName = "JohnCenaChairFight.mpeg";						
		
		Request requestData = new Request(owner, interval, videoFileName);
		
		requestData.setStatus(RequestStatus.PENDING_PROCESS);
		
		fileProcessUseCase.generateImageFiles(requestData);	
	}
}
