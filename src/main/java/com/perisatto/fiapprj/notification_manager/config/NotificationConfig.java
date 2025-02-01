package com.perisatto.fiapprj.notification_manager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.perisatto.fiapprj.notification_manager.application.interfaces.RequestRepository;
import com.perisatto.fiapprj.notification_manager.application.interfaces.UserRepository;
import com.perisatto.fiapprj.notification_manager.application.usecases.CreateNotificationUseCase;
import com.perisatto.fiapprj.notification_manager.infra.gateways.RequestRepositoryApi;
import com.perisatto.fiapprj.notification_manager.infra.gateways.UserRepositoryApi;
import com.postmarkapp.postmark.Postmark;
import com.postmarkapp.postmark.client.ApiClient;

@Configuration
public class NotificationConfig {
	
	@Autowired
	private Environment env;
		
	@Bean
	RequestRepositoryApi requestRepositoryApi(Environment env) {
		return new RequestRepositoryApi(env);
	}
	
	@Bean
	UserRepositoryApi userRepositoryApi(Environment env) {
		return new UserRepositoryApi(env);
	}
	
	@Bean
	CreateNotificationUseCase createNotificationUseCase(RequestRepository requestRepository, UserRepository userRepository, ApiClient client) {
		return new CreateNotificationUseCase(requestRepository, userRepository, client);
	}
	
	@Bean
	ApiClient client() {
		ApiClient client = Postmark.getApiClient(env.getProperty("spring.mailgun.apikey"));
		return client; 
	}
}
