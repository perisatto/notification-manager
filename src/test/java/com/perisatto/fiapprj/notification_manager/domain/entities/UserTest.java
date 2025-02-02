package com.perisatto.fiapprj.notification_manager.domain.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import com.perisatto.fiapprj.notification_manager.domain.entities.CPF;
import com.perisatto.fiapprj.notification_manager.domain.entities.User;

@ActiveProfiles(value = "test")
public class UserTest {

	@Test
	void givenValidCPF_thenRegisterUser() throws Exception {
		String userName = "Roberto Machado";
		String userEmail = "roberto.machado@bestmail.com";
		String documentNumber = "90779778057";

		CPF userCPF = new CPF(documentNumber);
		User user = new User(null, userCPF, userName, userEmail);

		assertThat(user.getName()).isEqualTo(userName);
		assertThat(user.getEmail()).isEqualTo(userEmail);
		assertThat(user.getDocumentNumber().getDocumentNumber()).isEqualTo(userCPF.getDocumentNumber());
	}

	@Test
	void givenInvalidCPF_thenRefusesToRegisterUser() throws Exception {
		try {
			String userName = "Roberto Machado";
			String userEmail = "roberto.machado@bestmail.com";
			String documentNumber = "90779778058";

			CPF userCPF = new CPF(documentNumber);
			User user = new User(null, userCPF, userName, userEmail);

			assertThat(user.getName()).isNull();
			assertThat(user.getEmail()).isNull();
			assertThat(user.getDocumentNumber()).isNull();
		}catch (Exception e) {
			assertThat(e.getMessage()).isEqualTo("Invalid document number");
		}
	}

	@Test
	void givenSequencialCharCPF_thenRefusesToRegisterUser() throws Exception {
		String userName = "Roberto Machado";
		String userEmail = "roberto.machado@bestmail.com";
		String[] documentNumber = {"00000000000","11111111111","22222222222","33333333333","44444444444",
				"55555555555","66666666666","77777777777","88888888888","99999999999","012346789"};

		for (String string : documentNumber) {
			try {
				CPF userCPF = new CPF(string);
				User user = new User(null, userCPF, userName, userEmail);
				assertThat(user.getName()).isNull();
				assertThat(user.getEmail()).isNull();
				assertThat(user.getDocumentNumber()).isNull();
			}catch (Exception e) {
				assertThat(e.getMessage()).isEqualTo("Invalid document number");
			}
		}
	}

	@Test
	void givenInvalidEmail_thenRefusesToRegisterUser() throws Exception {
		try { 
			String userName = "Roberto Machado";
			String userEmail = "roberto.machadobestmail.com";
			String documentNumber = "90779778057";

			CPF userCPF = new CPF(documentNumber);
			User user = new User(null, userCPF, userName, userEmail);

			assertThat(user.getName()).isEqualTo(userName);
			assertThat(user.getEmail()).isEqualTo(userEmail);
			assertThat(user.getDocumentNumber().getDocumentNumber()).isEqualTo(userCPF.getDocumentNumber());
			assertThat(user.getName()).isNull();
			assertThat(user.getEmail()).isNull();
			assertThat(user.getDocumentNumber()).isNull();
		}catch (Exception e) {
			assertThat(e.getMessage()).contains("invalid e-mail format");
		}
	}

	@Test
	void givenNullEmail_thenRefusesToRegisterUser() throws Exception {
		try { 
			String userName = "Roberto Machado";
			String documentNumber = "90779778057";

			CPF userCPF = new CPF(documentNumber);
			new User(null, userCPF, userName, null);
		}catch (Exception e) {
			assertThat(e.getMessage()).contains("Error validating user data");
		}
	}

	@Test
	void givenEmptyEmail_thenRefusesToRegisterUser() throws Exception {
		try { 
			String userName = "Roberto Machado";
			String documentNumber = "90779778057";
			String userEmail = "";

			CPF userCPF = new CPF(documentNumber);
			new User(null, userCPF, userName, userEmail);
		}catch (Exception e) {
			assertThat(e.getMessage()).contains("Error validating user data");
		}
	}

	@Test
	void givenBlankEmail_thenRefusesToRegisterUser() throws Exception {
		try { 
			String userName = "Roberto Machado";
			String documentNumber = "90779778057";
			String userEmail = " ";

			CPF userCPF = new CPF(documentNumber);
			new User(null, userCPF, userName, userEmail);
		}catch (Exception e) {
			assertThat(e.getMessage()).contains("Error validating user data");
		}
	}

	@Test
	void givenEmptyName_thenRefusesToRegisterUser() throws Exception {
		try { 
			String userName = "";
			String userEmail = "roberto.machadobestmail.com";
			String documentNumber = "90779778057";

			CPF userCPF = new CPF(documentNumber);
			User user = new User(null, userCPF, userName, userEmail);

			assertThat(user.getName()).isEqualTo(userName);
			assertThat(user.getEmail()).isEqualTo(userEmail);
			assertThat(user.getDocumentNumber().getDocumentNumber()).isEqualTo(userCPF.getDocumentNumber());
			assertThat(user.getName()).isNull();
			assertThat(user.getEmail()).isNull();
			assertThat(user.getDocumentNumber()).isNull();
		}catch (Exception e) {
			assertThat(e.getMessage()).contains("empty, null or blank name");
		}
	}

	@Test
	void givenBlankName_thenRefusesToRegisterUser() throws Exception {
		try { 
			String userName = " ";
			String userEmail = "roberto.machadobestmail.com";
			String documentNumber = "90779778057";

			CPF userCPF = new CPF(documentNumber);
			User user = new User(null, userCPF, userName, userEmail);

			assertThat(user.getName()).isEqualTo(userName);
			assertThat(user.getEmail()).isEqualTo(userEmail);
			assertThat(user.getDocumentNumber().getDocumentNumber()).isEqualTo(userCPF.getDocumentNumber());
			assertThat(user.getName()).isNull();
			assertThat(user.getEmail()).isNull();
			assertThat(user.getDocumentNumber()).isNull();
		}catch (Exception e) {
			assertThat(e.getMessage()).contains("empty, null or blank name");
		}
	}

	@Test
	void givenNullName_thenRefusesToRegisterUser() throws Exception {
		try { 
			String userName = null;
			String userEmail = "roberto.machadobestmail.com";
			String documentNumber = "90779778057";

			CPF userCPF = new CPF(documentNumber);
			User user = new User(null, userCPF, userName, userEmail);

			assertThat(user.getName()).isEqualTo(userName);
			assertThat(user.getEmail()).isEqualTo(userEmail);
			assertThat(user.getDocumentNumber().getDocumentNumber()).isEqualTo(userCPF.getDocumentNumber());
			assertThat(user.getName()).isNull();
			assertThat(user.getEmail()).isNull();
			assertThat(user.getDocumentNumber()).isNull();
		}catch (Exception e) {
			assertThat(e.getMessage()).contains("empty, null or blank name");
		}
	}

	@Test
	void givenNewData_updateUser() throws Exception {
		String userName = "Roberto Machado";
		String userEmail = "roberto.machado@bestmail.com";
		String documentNumber = "90779778057";

		CPF userCPF = new CPF(documentNumber);
		User user = new User(null, userCPF, userName, userEmail);

		user.setDocumentNumber(new CPF("23640699041"));
		user.setName("Roberto Facao");
		user.setEmail("robertofacao@bestmail.com");
		user.setId(1L);

		assertThat(user.getName()).isEqualTo("Roberto Facao");
		assertThat(user.getEmail()).isEqualTo("robertofacao@bestmail.com");
		assertThat(user.getDocumentNumber().getDocumentNumber()).isEqualTo("23640699041");
	}
}
