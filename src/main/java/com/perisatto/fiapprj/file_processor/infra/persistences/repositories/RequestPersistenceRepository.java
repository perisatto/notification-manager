package com.perisatto.fiapprj.file_processor.infra.persistences.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.perisatto.fiapprj.file_processor.infra.persistences.entities.RequestEntity;

public interface RequestPersistenceRepository extends JpaRepository<RequestEntity, Long> {
	
	Optional<RequestEntity> findById(String id);

	Page<RequestEntity> findByOwner(String owner, Pageable pageable); 
}
