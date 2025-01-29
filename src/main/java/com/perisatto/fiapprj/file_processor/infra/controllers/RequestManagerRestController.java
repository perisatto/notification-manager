package com.perisatto.fiapprj.file_processor.infra.controllers;

import java.net.URI;
import java.util.Properties;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.perisatto.fiapprj.file_processor.application.usecases.CreateRequestUseCase;
import com.perisatto.fiapprj.file_processor.application.usecases.GetRequestUseCase;
import com.perisatto.fiapprj.file_processor.application.usecases.UpdateRequestUseCase;
import com.perisatto.fiapprj.file_processor.domain.entities.Request;
import com.perisatto.fiapprj.file_processor.domain.entities.RequestStatus;
import com.perisatto.fiapprj.file_processor.infra.controllers.dtos.CreateRequestRequestDTO;
import com.perisatto.fiapprj.file_processor.infra.controllers.dtos.CreateRequestResponseDTO;
import com.perisatto.fiapprj.file_processor.infra.controllers.dtos.GetRequestListResponseDTO;
import com.perisatto.fiapprj.file_processor.infra.controllers.dtos.GetRequestResponseDTO;
import com.perisatto.fiapprj.file_processor.infra.controllers.dtos.UpdateRequestRequestDTO;
import com.perisatto.fiapprj.file_processor.infra.controllers.dtos.UpdateRequestResponseDTO;

@RestController
public class RequestManagerRestController {
	private final CreateRequestUseCase createRequestUseCase;
	private final UpdateRequestUseCase updateRequestUseCase;
	private final GetRequestUseCase getRequestUseCase;
	private final Properties requestProperties;

	public RequestManagerRestController(CreateRequestUseCase createRequestUseCase, Properties requestProperties, UpdateRequestUseCase updateRequestUseCase, GetRequestUseCase getRequestUseCase) {
		this.createRequestUseCase = createRequestUseCase;
		this.updateRequestUseCase = updateRequestUseCase;
		this.getRequestUseCase = getRequestUseCase;
		this.requestProperties = requestProperties;
	}

	@PostMapping(value = "/users/{userId}/requests", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreateRequestResponseDTO> createCustomer(@PathVariable(value = "userId") String userId, @RequestBody CreateRequestRequestDTO createRequest) throws Exception {
		requestProperties.setProperty("resourcePath", "/users/" + userId + "/requests");

		Request request = createRequestUseCase.createRequest(userId, createRequest.getInterval(), createRequest.getVideoFileName());		
		ModelMapper requestMapper = new ModelMapper();
		CreateRequestResponseDTO response = requestMapper.map(request, CreateRequestResponseDTO.class);
		URI location = new URI("/users/" + userId + "/requests/" + response.getId());
		return ResponseEntity.status(HttpStatus.CREATED).location(location).body(response);
	}
	
	@GetMapping(value = "/users/{userId}/requests", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetRequestListResponseDTO> getAll(@PathVariable(value = "userId") String userId, @RequestParam(value = "_page", required = true) Integer page, @RequestParam(value = "_size", required = true) Integer size) throws Exception {
		requestProperties.setProperty("resourcePath", "/users/" + userId + "/requests");
		Set<Request> request = getRequestUseCase.findAllRequests(size, page, userId);
		GetRequestListResponseDTO response = new GetRequestListResponseDTO();
		response.setContent(request, page, size);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	
	@GetMapping(value = "/users/{userId}/requests/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetRequestResponseDTO> get(@PathVariable(value = "userId") String userId, @PathVariable(value = "requestId") String requestId) throws Exception {
		requestProperties.setProperty("resourcePath", "/users/" + userId + "/requests/" + requestId);
		
		Request request = getRequestUseCase.getRequestById(requestId);
		ModelMapper requestMapper = new ModelMapper();
		GetRequestResponseDTO response = requestMapper.map(request, GetRequestResponseDTO.class);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PatchMapping(value = "/users/{userId}/requests/{requestId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UpdateRequestResponseDTO> patch(@PathVariable(value = "userId") String userId, @PathVariable(value = "requestId") String requestId, @RequestBody UpdateRequestRequestDTO updateRequest) throws Exception{
		requestProperties.setProperty("resourcePath", "/users/" + userId + "/requests/" + requestId);
		
		RequestStatus status = RequestStatus.valueOf(updateRequest.getStatus());				
		Request request = updateRequestUseCase.updateRequest(requestId, updateRequest.getRemarks(), status);
		ModelMapper requestMapper = new ModelMapper();
		UpdateRequestResponseDTO updateRequestResponseDTO = requestMapper.map(request, UpdateRequestResponseDTO.class);
		return ResponseEntity.status(HttpStatus.OK).body(updateRequestResponseDTO);
	}
}