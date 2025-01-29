package com.perisatto.fiapprj.file_processor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.perisatto.fiapprj.file_processor.application.interfaces.FileRepositoryManagement;
import com.perisatto.fiapprj.file_processor.application.interfaces.RequestRepository;
import com.perisatto.fiapprj.file_processor.application.usecases.CreateRequestUseCase;
import com.perisatto.fiapprj.file_processor.application.usecases.GetRequestUseCase;
import com.perisatto.fiapprj.file_processor.application.usecases.UpdateRequestUseCase;
import com.perisatto.fiapprj.file_processor.infra.gateways.FileProcessorQueueManagement;
import com.perisatto.fiapprj.file_processor.infra.gateways.RequestRepositoryJpa;
import com.perisatto.fiapprj.file_processor.infra.gateways.S3RepositoryManagement;
import com.perisatto.fiapprj.file_processor.infra.gateways.mappers.RequestMapper;
import com.perisatto.fiapprj.file_processor.infra.persistences.repositories.RequestPersistenceRepository;

@Configuration
public class RequestConfig {
	
	@Autowired
	private Environment env;
	
	@Bean
	CreateRequestUseCase createRequestUseCase(RequestRepository requestManagement, FileRepositoryManagement fileRepositoryManagement) {
		return new CreateRequestUseCase(requestManagement, fileRepositoryManagement);
	}
	
	@Bean
	UpdateRequestUseCase updateRequestUseCase(RequestRepository requestRepository, FileProcessorQueueManagement fileProcessorQueueManagement) {
		return new UpdateRequestUseCase(requestRepository, fileProcessorQueueManagement);
	}

	@Bean
	GetRequestUseCase getRequestUseCase(RequestRepository requestManagement, FileRepositoryManagement fileRepositoryManagement) {
		return new GetRequestUseCase(requestManagement, fileRepositoryManagement);
	}
	
		
	@Bean
	RequestRepositoryJpa requestRepositoryJpa(RequestPersistenceRepository requestPersistenceRepository, RequestMapper requestMapper) {
		return new RequestRepositoryJpa(requestPersistenceRepository, requestMapper);
	}
	
	@Bean
	RequestMapper requestMapper() {
		return new RequestMapper();
	}
	
	@Bean
	S3RepositoryManagement s3RepositoryManagement(Environment env){
		return new S3RepositoryManagement(env);
	}
	
	@Bean
	FileProcessorQueueManagement fileProcessorQueueManagement() {
		return new FileProcessorQueueManagement();
	}
}
