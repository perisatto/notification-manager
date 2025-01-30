package com.perisatto.fiapprj.file_processor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.perisatto.fiapprj.file_processor.application.interfaces.FileRepositoryManagement;
import com.perisatto.fiapprj.file_processor.application.interfaces.NotificationManagement;
import com.perisatto.fiapprj.file_processor.application.interfaces.RequestRepository;
import com.perisatto.fiapprj.file_processor.application.usecases.FileProcessUseCase;
import com.perisatto.fiapprj.file_processor.infra.gateways.NotificationManagerQueueManagement;
import com.perisatto.fiapprj.file_processor.infra.gateways.RequestRepositoryApi;
import com.perisatto.fiapprj.file_processor.infra.gateways.S3RepositoryManagement;

import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class FileProcessConfig {
	
	@Autowired
	private Environment env;
	
	@Bean
	FileProcessUseCase fileProcessUseCase(FileRepositoryManagement fileRepositoryManagement, RequestRepository requestRepository, NotificationManagement notificationManagement) {
		return new FileProcessUseCase(fileRepositoryManagement, requestRepository, notificationManagement);
	}	
		
	@Bean
	RequestRepositoryApi requestRepositoryApi(Environment env) {
		return new RequestRepositoryApi(env);
	}
	
	@Bean
	S3RepositoryManagement s3RepositoryManagement(Environment env, S3Client s3Client){
		return new S3RepositoryManagement(env, s3Client);
	}
	
	@Bean
	NotificationManagerQueueManagement fileProcessorQueueManagement() {
		return new NotificationManagerQueueManagement();
	}
	
	@Bean
	S3Client s3Client() {
		
		String awsAccessKeyId = env.getProperty("spring.aws.accessKeyId");
		String awsSecretAccessKey = env.getProperty("spring.aws.secretAccessKey");
		String awsSessionToken = env.getProperty("spring.aws.sessionToken");
		Region awsRegion = Region.of(env.getProperty("spring.aws.region"));
		
		AwsCredentials credentials = AwsSessionCredentials.create(awsAccessKeyId, awsSecretAccessKey, awsSessionToken);
		StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);
		
		S3Client s3Client = S3Client.builder()
				.credentialsProvider(credentialsProvider)
				.region(awsRegion)
				.build();
		
		return s3Client;
	}
}
