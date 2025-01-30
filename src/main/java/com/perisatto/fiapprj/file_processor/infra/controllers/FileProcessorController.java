package com.perisatto.fiapprj.file_processor.infra.controllers;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Controller;

import com.perisatto.fiapprj.file_processor.application.usecases.FileProcessUseCase;
import com.perisatto.fiapprj.file_processor.domain.entities.Request;

@Controller
@RabbitListener(queues = "pending_requests")
public class FileProcessorController {
	private final FileProcessUseCase fileProcessUseCase;

	public FileProcessorController(FileProcessUseCase fileProcessUseCase) {
		this.fileProcessUseCase = fileProcessUseCase;
	}
	
	@RabbitHandler	
	public void processFileRequest(Request request) throws Exception {
		fileProcessUseCase.generateImageFiles(request);
	}
}