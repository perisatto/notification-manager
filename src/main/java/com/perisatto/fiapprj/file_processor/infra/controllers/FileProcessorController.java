package com.perisatto.fiapprj.file_processor.infra.controllers;

import java.util.Properties;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perisatto.fiapprj.file_processor.application.usecases.FileProcessUseCase;
import com.perisatto.fiapprj.file_processor.domain.entities.Request;
import com.perisatto.fiapprj.file_processor.infra.controllers.dtos.CreateRequestResponseDTO;

@RestController
@RabbitListener(queues = "pending_requests")
public class FileProcessorController {
	private final FileProcessUseCase fileProcessUseCase;
	private final Properties requestProperties;

	public FileProcessorController(FileProcessUseCase fileProcessUseCase, Properties requestProperties) {
		this.fileProcessUseCase = fileProcessUseCase;
		this.requestProperties = requestProperties;
	}

	@PostMapping(value = "/process", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreateRequestResponseDTO> createCustomer() throws Exception {
		fileProcessUseCase.generateImageFiles(null);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
	
	@RabbitHandler	
	public void processFileRequest(Request request) throws Exception {
		fileProcessUseCase.generateImageFiles(request);
	}
}