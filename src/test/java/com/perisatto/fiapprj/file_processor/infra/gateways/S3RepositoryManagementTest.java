package com.perisatto.fiapprj.file_processor.infra.gateways;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import com.perisatto.fiapprj.file_processor.infra.gateways.S3RepositoryManagement;

@ActiveProfiles(value = "test")
public class S3RepositoryManagementTest {
	
	@InjectMocks
	private S3RepositoryManagement s3RepositoryManagement;
		
	@Mock
	private Environment env;
	
	AutoCloseable openMocks;
	
	@BeforeEach
	void setUp() {
		openMocks = MockitoAnnotations.openMocks(this);
		s3RepositoryManagement = new S3RepositoryManagement(env);
	}
	
	@AfterEach
	void teadDown() throws Exception {
		openMocks.close();
	}
	
	@Test
	void givenValidData_thenGenerateDownloadURL() throws URISyntaxException {
		String id = UUID.randomUUID().toString();
		String videoFile = "JohnCenaChairFight.mpeg";			
		
		when(env.getProperty("spring.aws.s3DownloadBucket")).thenReturn("test");
		when(env.getProperty("spring.aws.accessKeyId")).thenReturn("testKey");
		when(env.getProperty("spring.aws.secretAccessKey")).thenReturn("testSecretKey");
		when(env.getProperty("spring.aws.sessionToken")).thenReturn("testToken");
		when(env.getProperty("spring.aws.region")).thenReturn("us-east-1");
		
		String presignedUrl = s3RepositoryManagement.generateDownloadFileURL(id, videoFile);
		
		assertThat(new URI(presignedUrl).isOpaque());
	}
	
	@Test
	void givenValidData_thenGenerateUploadURL() throws URISyntaxException {
		String id = UUID.randomUUID().toString();
		String videoFile = "JohnCenaChairFight.mpeg";			
		
		when(env.getProperty("spring.aws.s3UploadBucket")).thenReturn("test");
		when(env.getProperty("spring.aws.accessKeyId")).thenReturn("testKey");
		when(env.getProperty("spring.aws.secretAccessKey")).thenReturn("testSecretKey");
		when(env.getProperty("spring.aws.sessionToken")).thenReturn("testToken");
		when(env.getProperty("spring.aws.region")).thenReturn("us-east-1");
		
		String presignedUrl = s3RepositoryManagement.generateUploadFileURL(id, videoFile);
		
		assertThat(new URI(presignedUrl).isOpaque());
	}
}
