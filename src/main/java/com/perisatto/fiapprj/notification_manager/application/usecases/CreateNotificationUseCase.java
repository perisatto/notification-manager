package com.perisatto.fiapprj.notification_manager.application.usecases;

import java.io.IOException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;

import com.perisatto.fiapprj.notification_manager.application.interfaces.RequestRepository;
import com.perisatto.fiapprj.notification_manager.application.interfaces.UserRepository;
import com.perisatto.fiapprj.notification_manager.domain.entities.Notification;
import com.perisatto.fiapprj.notification_manager.domain.entities.Request;
import com.perisatto.fiapprj.notification_manager.domain.entities.User;
import com.postmarkapp.postmark.Postmark;
import com.postmarkapp.postmark.client.ApiClient;
import com.postmarkapp.postmark.client.data.model.message.Message;
import com.postmarkapp.postmark.client.data.model.message.MessageResponse;
import com.postmarkapp.postmark.client.exception.PostmarkException;

public class CreateNotificationUseCase {
	
	static final Logger logger = LogManager.getLogger(CreateNotificationUseCase.class);
	
	private final RequestRepository requestRepository;
	
	private final UserRepository userRepository;
	
	private final ApiClient client;
	
	public CreateNotificationUseCase(RequestRepository requestRepository, UserRepository userRepository, ApiClient client) {
		this.requestRepository = requestRepository;
		this.userRepository = userRepository;
		this.client = client;
	}

	public void sendEmail(Notification notification) throws Exception {
		logger.info("Sending email notification for request " + notification.getRequestId());
		
		String requestId = notification.getRequestId();
		
		Optional<Request> request = requestRepository.getRequestById(notification.getOwner(), requestId);
		
		if(request.isPresent()) {
			Optional<User> user = userRepository.getUserById(request.get().getOwner());	
			
			String subject = "[Request " + notification.getRequestId() + "] Error processing request";
			String message = "An error ocurred while your request was processing. The request finished with the following message: " + notification.getMessage();
			
			sendSimpleMessage(subject, message, user.get().getEmail());
			
			logger.info("Email notification sent");
		}
	}

  	public void sendSimpleMessage(String subject, String message, String to) throws PostmarkException, IOException {  		
  		Message sendMessage = new Message("rodrigo@perisatto.com", to, subject, message);
  		MessageResponse response = client.deliverMessage(sendMessage);
  	}
}
