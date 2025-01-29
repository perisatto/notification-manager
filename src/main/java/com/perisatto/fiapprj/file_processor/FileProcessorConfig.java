package com.perisatto.fiapprj.file_processor;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class FileProcessorConfig {

	@Autowired
	private Environment env;


	@Bean
	public Queue pendingRequests() {
		return new Queue("pending_requests");
	}
}
