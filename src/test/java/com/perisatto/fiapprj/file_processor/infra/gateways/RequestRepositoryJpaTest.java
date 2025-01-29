package com.perisatto.fiapprj.file_processor.infra.gateways;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;

import com.perisatto.fiapprj.file_processor.domain.entities.Request;
import com.perisatto.fiapprj.file_processor.domain.entities.RequestStatus;
import com.perisatto.fiapprj.file_processor.infra.gateways.RequestRepositoryJpa;
import com.perisatto.fiapprj.file_processor.infra.gateways.mappers.RequestMapper;
import com.perisatto.fiapprj.file_processor.infra.persistences.entities.RequestEntity;
import com.perisatto.fiapprj.file_processor.infra.persistences.repositories.RequestPersistenceRepository;

@ActiveProfiles(value = "test")
public class RequestRepositoryJpaTest {

	private RequestRepositoryJpa requestRepositoryJpa;
	
	@Mock
	private RequestPersistenceRepository requestPersistenceRepository;
	
	private RequestMapper requestMapper;
	
	AutoCloseable openMocks;
	
	@BeforeEach
	void setUp() {
		openMocks = MockitoAnnotations.openMocks(this);
		requestMapper = new RequestMapper();
		requestRepositoryJpa = new RequestRepositoryJpa(requestPersistenceRepository, requestMapper);
	}
	
	@AfterEach
	void tearDown() throws Exception {
		openMocks.close();
	}
	
	
	@Test
	void givenValidData_thenSavesRequest() throws Exception {
		
		RequestEntity requestEntity = getRequest();
		
		when(requestPersistenceRepository.save(any()))
		.thenReturn(requestEntity);
		
		Request request = new Request("me", 10, "JohnCenaChairFight.mpeg");
		request.setStatus(RequestStatus.PENDING_UPLOAD);
		
		requestRepositoryJpa.createRequest(request);
		
		verify(requestPersistenceRepository, times(1)).save(any());
	}
	
	@Test
	void givenValidData_thenRetrieveRequest() throws Exception {
		
		RequestEntity requestEntity = getRequest(); 
		
		when(requestPersistenceRepository.findById(any(String.class)))
		.thenReturn(Optional.of(requestEntity));
		
		requestRepositoryJpa.getRequestById(requestEntity.getId());
		
		verify(requestPersistenceRepository, times(1)).findById(any(String.class));
	}
	

	@Test
	void givenInexistentId_thenRefusesRetrieveRequest() throws Exception {
		
		when(requestPersistenceRepository.findById(any(String.class)))
		.thenReturn(Optional.empty());
		
		requestRepositoryJpa.getRequestById(UUID.randomUUID().toString());
		
		verify(requestPersistenceRepository, times(1)).findById(any(String.class));
	}
	
	@Test
	void listAllRequests() throws Exception {
		when(requestPersistenceRepository.findByOwner(any(String.class), any()))
		.thenAnswer(i -> {
			
			List<RequestEntity> requestList = new ArrayList<>();
			
			RequestEntity requestEntity = new RequestEntity();
			
			requestEntity.setId(UUID.randomUUID().toString());
			requestEntity.setIdRequest(10L);
			requestEntity.setIdRequestStatus(1L);
			requestEntity.setOwner("me");
			requestEntity.setRemarks("No remarks");
			requestEntity.setTimeInterval(10);
			requestEntity.setVideoDownloadUrl("http://localhost");
			requestEntity.setVideoUploadUrl("http://localhost");
			requestEntity.setVideoFileName("JohnCenaChairFight.mpeg");
			
			requestList.add(requestEntity);
			
			RequestEntity requestEntity2 = new RequestEntity();
			
			requestEntity2.setId(UUID.randomUUID().toString());
			requestEntity2.setIdRequest(10L);
			requestEntity2.setIdRequestStatus(1L);
			requestEntity2.setOwner("me");
			requestEntity2.setRemarks("No remarks");
			requestEntity2.setTimeInterval(10);
			requestEntity2.setVideoDownloadUrl("http://localhost");
			requestEntity2.setVideoUploadUrl("http://localhost");
			requestEntity2.setVideoFileName("JohnCenaChairFight.mpeg");
			
			requestList.add(requestEntity2);
			
			Page<RequestEntity> requests = new PageImpl<>(requestList);
			
			return requests;
		});
		
		requestRepositoryJpa.findAll(50, 1, "me");
		
		verify(requestPersistenceRepository, times(1)).findByOwner(any(String.class), any());
	}
	
	@Test
	void givenValidData_thenUpdateRequest() throws Exception {
		RequestEntity requestEntity = getRequest();
		
		when(requestPersistenceRepository.save(any(RequestEntity.class)))
		.thenReturn(requestEntity);
		
		Request request = new Request("me", 10, "JohnCenaChairFight.mpeg");
		request.setStatus(RequestStatus.PENDING_UPLOAD);
		
		requestRepositoryJpa.updateRequest(request);
		
		verify(requestPersistenceRepository, times(1)).save(any());		
	}
	
	private RequestEntity getRequest() {
		RequestEntity requestEntity = new RequestEntity();
		
		requestEntity.setId(UUID.randomUUID().toString());
		requestEntity.setIdRequest(10L);
		requestEntity.setIdRequestStatus(1L);
		requestEntity.setOwner("me");
		requestEntity.setRemarks("No remarks");
		requestEntity.setTimeInterval(10);
		requestEntity.setVideoDownloadUrl("http://localhost");
		requestEntity.setVideoUploadUrl("http://localhost");
		requestEntity.setVideoFileName("JohnCenaChairFight.mpeg");
		return requestEntity;
	}
}
