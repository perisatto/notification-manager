package com.perisatto.fiapprj.file_processor.infra.gateways.mappers;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import com.perisatto.fiapprj.file_processor.domain.entities.Request;
import com.perisatto.fiapprj.file_processor.domain.entities.RequestStatus;
import com.perisatto.fiapprj.file_processor.handler.exceptions.ValidationException;
import com.perisatto.fiapprj.file_processor.infra.persistences.entities.RequestEntity;

public class RequestMapper {
	
	private ModelMapper modelMapper;

	public RequestEntity mapToJpaEntity(Request request) {
		this.modelMapper = new ModelMapper();
		TypeMap<Request, RequestEntity> propertyMapper = this.modelMapper.createTypeMap(Request.class, RequestEntity.class);
		propertyMapper.addMapping(Request::getId, RequestEntity::setId);
		propertyMapper.addMapping(Request::getOwner, RequestEntity::setOwner);
		propertyMapper.addMapping(Request::getInterval, RequestEntity::setTimeInterval);
		propertyMapper.addMapping(Request::getVideoFileName, RequestEntity::setVideoFileName);
		propertyMapper.addMapping(Request::getRemarks, RequestEntity::setRemarks);
		propertyMapper.addMapping(Request::getVideoUploadUrl, RequestEntity::setVideoUploadUrl);
		propertyMapper.addMapping(Request::getVideoDownloadUrl, RequestEntity::setVideoDownloadUrl);
		
		RequestEntity requestJpaEntity = modelMapper.map(request, RequestEntity.class);
		
		requestJpaEntity.setIdRequestStatus(request.getStatus().getId());
		
		return requestJpaEntity;
	}

	public Request mapToDomainEntity(RequestEntity requestJpaEntity) throws ValidationException {
		Request request = new Request(requestJpaEntity.getOwner(), requestJpaEntity.getTimeInterval(), requestJpaEntity.getVideoFileName());
		request.setIdRequest(requestJpaEntity.getIdRequest());
		request.setId(requestJpaEntity.getId());		
		request.setStatus(RequestStatus.values()[(int) (requestJpaEntity.getIdRequestStatus() - 1)]);
		request.setRemarks(requestJpaEntity.getRemarks());
		request.setVideoUploadUrl(requestJpaEntity.getVideoUploadUrl());
		request.setVideoDownloadUrl(requestJpaEntity.getVideoDownloadUrl());
		return request;
	}
}
