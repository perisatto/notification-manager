package com.perisatto.fiapprj.file_processor.infra.gateways;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.perisatto.fiapprj.file_processor.application.interfaces.FileProcessingManagement;
import com.perisatto.fiapprj.file_processor.domain.entities.Request;

public class FileProcessorQueueManagement implements FileProcessingManagement {	
	
    @Autowired
    private RabbitTemplate template;
    
    @Autowired
    private Queue pendingRequests;
	
	@Override
	public void createProcessingRequest(Request request) {						
		template.convertAndSend(pendingRequests.getName(), request);
	}

}
