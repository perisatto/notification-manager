package com.perisatto.fiapprj.file_processor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.perisatto.fiapprj.file_processor.domain.entities.Request;

@Configuration
public class FileProcessorConfig {

	@Autowired
	private Environment env;


	@Bean
	public Queue pendingRequests() {
		return new Queue("pending_requests");
	}
	
	@Bean
	public Jackson2JsonMessageConverter jsonMessageConverter() {
	    Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
	    jsonConverter.setClassMapper(classMapper());
	    return jsonConverter;
	}

	@Bean
	public DefaultClassMapper classMapper() {
	    DefaultClassMapper classMapper = new DefaultClassMapper();
	    Map<String, Class<?>> idClassMapping = new HashMap<>();
	    idClassMapping.put("Request", Request.class);
	    classMapper.setIdClassMapping(idClassMapping);
	    return classMapper;
	}	
}
