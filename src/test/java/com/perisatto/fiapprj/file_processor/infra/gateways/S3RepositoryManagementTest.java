package com.perisatto.fiapprj.file_processor.infra.gateways;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@ActiveProfiles(value = "test")
public class S3RepositoryManagementTest {
	
	@InjectMocks
	private S3RepositoryManagement s3RepositoryManagement;
		
	@Mock
	private Environment env;
	
	@Mock
	private S3Client s3Client;
	
	AutoCloseable openMocks;
	
	@BeforeEach
	void setUp() {
		openMocks = MockitoAnnotations.openMocks(this);
		s3RepositoryManagement = new S3RepositoryManagement(env, s3Client);
	}
	
	@AfterEach
	void teadDown() throws Exception {
		openMocks.close();
	}
	
	@Test
	void givenValidData_RetrieveFileToProcess() throws Exception {
		String id = UUID.randomUUID().toString();
		String videoFile = "JohnCenaChairFight.mpeg";
		
		when(env.getProperty("spring.aws.s3UploadBucket")).thenReturn("test");
		
		ResponseInputStream<GetObjectResponse> responseInputStream = null;
		
		when(s3Client.getObject(any(GetObjectRequest.class)))
		.thenReturn(responseInputStream);
		
		s3RepositoryManagement.getFileToProcess(id, videoFile);
		
		verify(s3Client, times(1)).getObject(any(GetObjectRequest.class));
	}
	
	@Test
	void givenValidData_UploadProcessedFile() throws Exception {
		String id = UUID.randomUUID().toString();
		String videoFile = "JohnCenaChairFight.mpeg";
		File file = new File("src/test/resources/video-file/dummy.mkv");
		
		when(env.getProperty("spring.aws.s3DownloadBucket")).thenReturn("test");
		
		PutObjectResponse putObjectResponse = null;
		
		when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
		.thenReturn(putObjectResponse);
		
		s3RepositoryManagement.writeProcessedFile(id, videoFile, file);
		
		verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
	}
}
