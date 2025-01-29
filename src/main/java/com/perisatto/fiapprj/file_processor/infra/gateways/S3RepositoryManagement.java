package com.perisatto.fiapprj.file_processor.infra.gateways;

import java.io.File;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;

import com.perisatto.fiapprj.file_processor.application.interfaces.FileRepositoryManagement;

import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class S3RepositoryManagement implements FileRepositoryManagement{

	static final Logger logger = LogManager.getLogger(S3RepositoryManagement.class);
	
	private final Environment env;
	
	public S3RepositoryManagement(Environment env) {
		this.env = env;
	}

	@Override
	public InputStream getFileToProcess(String id, String videoFile) throws Exception {				
		String awsS3Bucket = env.getProperty("spring.aws.s3UploadBucket");
		String awsAccessKeyId = env.getProperty("spring.aws.accessKeyId");
		String awsSecretAccessKey = env.getProperty("spring.aws.secretAccessKey");
		String awsSessionToken = env.getProperty("spring.aws.sessionToken");
		Region awsRegion = Region.of(env.getProperty("spring.aws.region"));
		
		AwsCredentials credentials = AwsSessionCredentials.create(awsAccessKeyId, awsSecretAccessKey, awsSessionToken);
		StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);
		
		GetObjectRequest getObjectRequest = GetObjectRequest.builder()
				.bucket(awsS3Bucket)
				.key(id + "_" + videoFile)
				.build();
		
		S3Client s3Client = S3Client.builder()
				.credentialsProvider(credentialsProvider)
				.region(awsRegion)
				.build();
		
		InputStream inputStream = s3Client.getObject(getObjectRequest);
		
		return inputStream;
	}
	
	@Override
	public void writeProcessedFile(String id, String videoFile, File file) throws Exception {
		String awsS3Bucket = env.getProperty("spring.aws.s3DownloadBucket");
		String awsAccessKeyId = env.getProperty("spring.aws.accessKeyId");
		String awsSecretAccessKey = env.getProperty("spring.aws.secretAccessKey");
		String awsSessionToken = env.getProperty("spring.aws.sessionToken");
		Region awsRegion = Region.of(env.getProperty("spring.aws.region"));
		
		AwsCredentials credentials = AwsSessionCredentials.create(awsAccessKeyId, awsSecretAccessKey, awsSessionToken);
		StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);
		
		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(awsS3Bucket)
				.key(id + "_" + videoFile)
				.build();
		
		S3Client s3Client = S3Client.builder()
				.credentialsProvider(credentialsProvider)
				.region(awsRegion)
				.build();
		
		s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));
	}
}
