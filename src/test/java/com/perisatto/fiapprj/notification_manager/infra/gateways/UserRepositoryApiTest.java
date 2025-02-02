package com.perisatto.fiapprj.notification_manager.infra.gateways;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perisatto.fiapprj.notification_manager.domain.entities.User;
import com.perisatto.fiapprj.notification_manager.infra.controllers.dtos.GetUserResponseDTO;
import com.perisatto.fiapprj.notification_manager.infra.gateways.UserRepositoryApi;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@ActiveProfiles(value = "test")
public class UserRepositoryApiTest {
	
	private UserRepositoryApi userRepositoryApi;
	
	public static MockWebServer mockBackEnd;
	
	@Mock
	private Environment env;
	
	AutoCloseable openMocks;
	
	@BeforeEach
	void setUp() throws IOException {
		openMocks = MockitoAnnotations.openMocks(this);
		userRepositoryApi = new UserRepositoryApi(env);
		mockBackEnd = new MockWebServer();
		mockBackEnd.start();
	}
	
	@AfterEach
	void tearDown() throws Exception {
		openMocks.close();
		mockBackEnd.shutdown();
	}
	
	@Test
	void givenValidId_ThenRetrievesUser() throws Exception {

		String userName = "Roberto Machado";
		String userEmail = "roberto.machado@bestmail.com";
		String documentNumber = "90779778057";

		GetUserResponseDTO getUserResponseDTO = new GetUserResponseDTO();
		getUserResponseDTO.setDocumentNumber(documentNumber);
		getUserResponseDTO.setEmail(userEmail);
		getUserResponseDTO.setId(1);
		getUserResponseDTO.setName(userName);
		
		String serverUrl = String.format("http://localhost:%s", mockBackEnd.getPort());
		
		when(env.getProperty(any(String.class)))
		.thenReturn(serverUrl);
		
		mockBackEnd.enqueue(new MockResponse().setBody(asJsonString(getUserResponseDTO))
				.addHeader("Content-Type", "application/json"));
		
		Long userId = 1L;				
		
		Optional<User> getUser = userRepositoryApi.getUserById(userId);
	}
	
	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
