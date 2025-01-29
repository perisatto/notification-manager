package com.perisatto.fiapprj.file_processor.infra.gateways;

import java.net.URI;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import com.perisatto.fiapprj.file_processor.application.interfaces.RequestRepository;
import com.perisatto.fiapprj.file_processor.domain.entities.Request;
import com.perisatto.fiapprj.file_processor.infra.controllers.dtos.UpdateRequestRequestDTO;
import com.perisatto.fiapprj.file_processor.infra.controllers.dtos.UpdateRequestResponseDTO;

public class RequestRepositoryApi implements RequestRepository {

	private final RestClient restClient;
	private final Environment env;

	public RequestRepositoryApi(Environment env) {
		this.restClient = RestClient.create();
		this.env = env;
	}
	
	@Override
	public Boolean updateRequest(Request request) throws Exception {		
		ResponseEntity<UpdateRequestResponseDTO> response;
		
		UpdateRequestRequestDTO updateRequestRequestDTO = new UpdateRequestRequestDTO();
		updateRequestRequestDTO.setStatus(request.getStatus().toString());
		
		if(request.getRemarks() != null) {
			updateRequestRequestDTO.setRemarks(request.getRemarks());
		}

		String url = env.getProperty("spring.request.serviceUrl") + "/request-manager/v1/users/" + request.getOwner() + "/requests/" + request.getId();
		try {
			response = restClient.patch()
					.uri(URI.create(url))
					.body(updateRequestRequestDTO)
					.accept(MediaType.APPLICATION_JSON)
					.retrieve().toEntity(UpdateRequestResponseDTO.class);
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			HttpStatusCode status = e.getStatusCode();
			if(status.value() == 404) {
				return false;
			} else {
				throw e;
			}
		}
		
		if(response.getStatusCode() == HttpStatus.OK) {
			return true;
		} else {
			return false;
		}
	}
}
