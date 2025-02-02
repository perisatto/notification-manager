package com.perisatto.fiapprj.notification_manager.domain.entities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.perisatto.fiapprj.notification_manager.handler.exceptions.ValidationException;

public class User {	
	
	static final Logger logger = LogManager.getLogger(User.class);
	private String messageValidation;
	
	private Long id;
	private String name = null;
	private String email = null;
	private CPF documentNumber;

	public User(Long id, CPF documentNumber, String userName, String userEmail) throws Exception {
		if(!validate(userName, userEmail)) {
			logger.warn("Error validating user data");
			throw new ValidationException("cstm-2001", messageValidation);
		}
		
		this.id = id;
		this.documentNumber = documentNumber;
		this.name = userName;
		this.email = userEmail;	
	}


	private boolean validate(String userName, String userEmail) {
		messageValidation = "Error validating user data: ";
		Boolean valid = true;
		if((userName == null) || (userName.isEmpty()) || (userName.isBlank())) {
			logger.debug("Error validating user data: empty, null or blank name");
			messageValidation = messageValidation + "empty, null or blank name |";
			valid = false;			
		}

		if((userEmail == null) || (userEmail.isEmpty()) || (userEmail.isBlank())) {
			logger.debug("Error validating user data: empty, null or blank e-mail");
			messageValidation = messageValidation + "empty, null or blank e-mail | ";
			valid = false;			
		} else if(!userEmail.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
			logger.debug("Error validating user data: invalid e-mail format");
			messageValidation = messageValidation + "invalid e-mail format | ";
			valid = false;
		}
		
		return valid;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public CPF getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(CPF documentNumber) {
		this.documentNumber = documentNumber;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
}
