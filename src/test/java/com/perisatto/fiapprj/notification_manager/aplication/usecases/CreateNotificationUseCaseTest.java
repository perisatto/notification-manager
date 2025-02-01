package com.perisatto.fiapprj.notification_manager.aplication.usecases;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import com.perisatto.fiapprj.notification_manager.application.interfaces.RequestRepository;
import com.perisatto.fiapprj.notification_manager.application.interfaces.UserRepository;
import com.perisatto.fiapprj.notification_manager.application.usecases.CreateNotificationUseCase;
import com.perisatto.fiapprj.notification_manager.domain.entities.CPF;
import com.perisatto.fiapprj.notification_manager.domain.entities.Notification;
import com.perisatto.fiapprj.notification_manager.domain.entities.Request;
import com.perisatto.fiapprj.notification_manager.domain.entities.RequestStatus;
import com.perisatto.fiapprj.notification_manager.domain.entities.User;
import com.perisatto.fiapprj.notification_manager.handler.exceptions.ValidationException;

@ActiveProfiles(value = "test")
public class CreateNotificationUseCaseTest {
	
	private CreateNotificationUseCase createNotificationUseCase;
	
	@Mock
	private RequestRepository requestRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private Environment env;
	
	AutoCloseable openMocks;
	
	@BeforeEach
	void setUp() {
		openMocks = MockitoAnnotations.openMocks(this);
		createNotificationUseCase = new CreateNotificationUseCase(requestRepository, userRepository, env);
	}
	
	@AfterEach
	void tearDown() throws Exception {
		openMocks.close();
	}
	
	@Test
	void givenValidData_thenSendNotification() throws Exception {
		
		Request request = getNewRequest();
		
		when(requestRepository.getRequestById(any(Long.class), any(String.class)))
		.thenReturn(Optional.of(request));
		
		User user = getNewUser();
		
		when(userRepository.getUserById(any(Long.class)))
		.thenReturn(Optional.of(user));
		
		when(env.getProperty(any(String.class)))
		.thenReturn("apikey-code");
		
		Notification notification = new Notification();
		notification.setMessage("An error occurred");
		notification.setOwner(10L);
		notification.setRequestId(UUID.randomUUID().toString());
		
		createNotificationUseCase.sendEmail(notification);
		
	}

	private User getNewUser() throws Exception {
		String userName = "Roberto Machado";
		String userEmail = "mailgun.carmaker171@passmail.net";
		String documentNumber = "90779778057";

		CPF userCPF = new CPF(documentNumber);
		User user = new User(null, userCPF, userName, userEmail);
		return user;
	}

	private Request getNewRequest() throws ValidationException {
		Long idRequest = 1L;
		Long owner = 1L;
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
		return request;
	}
}
