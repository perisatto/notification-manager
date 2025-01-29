package com.perisatto.fiapprj.file_processor.infra.gateways;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.perisatto.fiapprj.file_processor.application.interfaces.RequestRepository;
import com.perisatto.fiapprj.file_processor.domain.entities.Request;
import com.perisatto.fiapprj.file_processor.infra.gateways.mappers.RequestMapper;
import com.perisatto.fiapprj.file_processor.infra.persistences.entities.RequestEntity;
import com.perisatto.fiapprj.file_processor.infra.persistences.repositories.RequestPersistenceRepository;

public class RequestRepositoryJpa implements RequestRepository {

	private final RequestPersistenceRepository requestPersistenceRepository;
	private final RequestMapper requestMapper;
	
	public RequestRepositoryJpa(RequestPersistenceRepository requestPersistenceRepository, RequestMapper requestMapper) {
		this.requestPersistenceRepository = requestPersistenceRepository;
		this.requestMapper = requestMapper;
	}
	
	@Override
	public Request createRequest(Request request) throws Exception {
		RequestEntity requestJpaEntity = requestMapper.mapToJpaEntity(request);
		requestJpaEntity.setIdRequestStatus(1L);
		requestJpaEntity = requestPersistenceRepository.save(requestJpaEntity);
		Request newRequest;
		newRequest = requestMapper.mapToDomainEntity(requestJpaEntity);
		return newRequest;
	}

	@Override
	public Optional<Request> updateRequest(Request request) throws Exception {
		RequestEntity requestEntity = requestMapper.mapToJpaEntity(request);
		requestEntity = requestPersistenceRepository.save(requestEntity);
		Request updatedRequest = requestMapper.mapToDomainEntity(requestEntity);
		return Optional.of(updatedRequest);
	}

	@Override
	public Optional<Request> getRequestById(String id) throws Exception {
		Request request;
		
		Optional<RequestEntity> requestEntity = requestPersistenceRepository.findById(id);
		if(requestEntity.isPresent()) {
			request = requestMapper.mapToDomainEntity(requestEntity.get());
		} else {
			return Optional.empty();
		}
		
		return Optional.of(request);
	}

	@Override
	public Set<Request> findAll(Integer limit, Integer page, String owner) throws Exception {		
		Pageable pageable = PageRequest.of(page, limit, Sort.by("idRequest"));
		Page<RequestEntity> requests = requestPersistenceRepository.findByOwner(owner, pageable);
		Set<Request> requestsSet = new LinkedHashSet<Request>();
		
		for (RequestEntity request : requests) {
			Request retrievedRequest = requestMapper.mapToDomainEntity(request);
			requestsSet.add(retrievedRequest);
		}
		return requestsSet;
	}
}
