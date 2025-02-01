package com.perisatto.fiapprj.notification_manager.infra.gateways;

import java.net.URI;
import java.util.Optional;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import com.perisatto.fiapprj.notification_manager.application.interfaces.UserRepository;
import com.perisatto.fiapprj.notification_manager.domain.entities.CPF;
import com.perisatto.fiapprj.notification_manager.domain.entities.User;
import com.perisatto.fiapprj.notification_manager.infra.controllers.dtos.GetUserResponseDTO;

public class UserRepositoryApi implements UserRepository {

	private final RestClient restClient;
	private final Environment env;

	public UserRepositoryApi(Environment env) {
		this.restClient = RestClient.create();
		this.env = env;
	}

	@Override
	public Optional<User> getUserById(Long userId) throws Exception {
		ResponseEntity<GetUserResponseDTO> response;
		
		String url = env.getProperty("spring.user.serviceUrl") + "/user-management/v1/users/" + userId;
		try {
			response = restClient.get()
					.uri(URI.create(url))
					.accept(MediaType.APPLICATION_JSON)
					.retrieve().toEntity(GetUserResponseDTO.class);
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			HttpStatusCode status = e.getStatusCode();
			if(status.value() == 404) {
				return Optional.empty();
			} else {
				throw e;
			}
		}
		
		if(response.getStatusCode() == HttpStatus.OK) {
			CPF userCpf = new CPF(response.getBody().getDocumentNumber());
			User user = new User((long) response.getBody().getId(), userCpf, response.getBody().getName(), response.getBody().getEmail());
			return Optional.of(user);
		} else {
			return Optional.empty();
		}
	}
	
}
