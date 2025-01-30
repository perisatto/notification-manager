package com.perisatto.fiapprj.file_processor.infra.gateways;

import java.io.File;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;

import com.perisatto.fiapprj.file_processor.application.interfaces.FileRepositoryManagement;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class S3RepositoryManagement implements FileRepositoryManagement{

	static final Logger logger = LogManager.getLogger(S3RepositoryManagement.class);
	
	private final Environment env;
	
	private final S3Client s3Client;
	
	public S3RepositoryManagement(Environment env, S3Client s3Client) {
		this.env = env;
		this.s3Client = s3Client;
	}

	@Override
	public InputStream getFileToProcess(String id, String videoFile) throws Exception {				
		String awsS3Bucket = env.getProperty("spring.aws.s3UploadBucket");
		
		GetObjectRequest getObjectRequest = GetObjectRequest.builder()
				.bucket(awsS3Bucket)
				.key(id + "_" + videoFile)
				.build();
				
		InputStream inputStream = s3Client.getObject(getObjectRequest);
		
		return inputStream;
	}
	
	@Override
	public void writeProcessedFile(String id, String videoFile, File file) throws Exception {
		String awsS3Bucket = env.getProperty("spring.aws.s3DownloadBucket");
				
		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(awsS3Bucket)
				.key(id + "_" + videoFile)
				.build();
				
		s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));
	}
}
