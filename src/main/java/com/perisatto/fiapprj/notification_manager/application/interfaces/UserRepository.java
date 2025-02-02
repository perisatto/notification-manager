package com.perisatto.fiapprj.notification_manager.application.interfaces;

import java.util.Optional;

import com.perisatto.fiapprj.notification_manager.domain.entities.User;

public interface UserRepository {
	
	Optional<User> getUserById(Long usertId) throws Exception;
}
