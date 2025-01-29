package com.perisatto.fiapprj.file_processor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.perisatto.fiapprj.file_processor.application.interfaces.FileRepositoryManagement;
import com.perisatto.fiapprj.file_processor.application.interfaces.RequestRepository;
import com.perisatto.fiapprj.file_processor.application.usecases.FileProcessUseCase;
import com.perisatto.fiapprj.file_processor.infra.gateways.FileProcessorQueueManagement;
import com.perisatto.fiapprj.file_processor.infra.gateways.RequestRepositoryApi;
import com.perisatto.fiapprj.file_processor.infra.gateways.S3RepositoryManagement;

@Configuration
public class FileProcessConfig {
	
	@Autowired
	private Environment env;
	
	@Bean
	FileProcessUseCase fileProcessUseCase(FileRepositoryManagement fileRepositoryManagement, RequestRepository requestRepository) {
		return new FileProcessUseCase(fileRepositoryManagement, requestRepository);
	}	
		
	@Bean
	RequestRepositoryApi requestRepositoryApi(Environment env) {
		return new RequestRepositoryApi(env);
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
