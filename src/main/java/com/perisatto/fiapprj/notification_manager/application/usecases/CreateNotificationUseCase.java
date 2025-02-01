package com.perisatto.fiapprj.notification_manager.application.usecases;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.perisatto.fiapprj.notification_manager.application.interfaces.RequestRepository;
import com.perisatto.fiapprj.notification_manager.application.interfaces.UserRepository;
import com.perisatto.fiapprj.notification_manager.domain.entities.Notification;
import com.perisatto.fiapprj.notification_manager.domain.entities.Request;
import com.perisatto.fiapprj.notification_manager.domain.entities.User;

public class CreateNotificationUseCase {
	
	static final Logger logger = LogManager.getLogger(CreateNotificationUseCase.class);
	
	private final RequestRepository requestRepository;
	
	private final UserRepository userRepository;
	
	private final Environment env;
	
	public CreateNotificationUseCase(RequestRepository requestRepository, UserRepository userRepository, Environment env) {
		this.requestRepository = requestRepository;
		this.userRepository = userRepository;
		this.env = env;
	}

	public void sendEmail(Notification notification) throws Exception {
		logger.info("Sending email notification for request " + notification.getRequestId());
		
		String requestId = notification.getRequestId();
		
		Optional<Request> request = requestRepository.getRequestById(notification.getOwner(), requestId);
		
		if(request.isPresent()) {
			Optional<User> user = userRepository.getUserById(request.get().getOwner());	
			
			String subject = "[Request " + request.get().getId() + "] Error processing request";
			String message = "An error ocurred while your request was processing. The request finished with the following message: " + request.get().getRemarks();
			
			sendSimpleMessage(subject, message, user.get().getEmail());		
		}
	}

  	public JsonNode sendSimpleMessage(String subject, String message, String to) throws UnirestException {  		
  		HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/sandbox7e6a008084314249a7f9a0903808cf4d.mailgun.org/messages")
  			.basicAuth("api", env.getProperty("spring.mailgun.apikey"))
  			.queryString("from", "VFC Notification <USER@sandbox7e6a008084314249a7f9a0903808cf4d.mailgun.org>")
  			.queryString("to", to)
  			.queryString("subject", subject)
  			.queryString("text", message)
  			.asJson();
  		return request.getBody();
  	}
}
