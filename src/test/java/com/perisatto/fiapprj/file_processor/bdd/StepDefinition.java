package com.perisatto.fiapprj.file_processor.bdd;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.equalTo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.perisatto.fiapprj.file_processor.infra.controllers.dtos.CreateRequestRequestDTO;
import com.perisatto.fiapprj.file_processor.infra.controllers.dtos.CreateRequestResponseDTO;
import com.perisatto.fiapprj.file_processor.infra.controllers.dtos.UpdateRequestRequestDTO;

import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

public class StepDefinition {

	private Response response;
	private List<CreateRequestRequestDTO> createRequestRequestDTOs = new ArrayList<>();
	private CreateRequestResponseDTO createRequestResponseDTO;
	private String newStatus;
	private final String ENDPOINT_REQUEST_API = "http://localhost:8080/request-manager/v1/users/me/requests";

	@DataTableType
	public CreateRequestRequestDTO createRequestEntry(Map<String, String> entry) {
		CreateRequestRequestDTO requestEntry = new CreateRequestRequestDTO();
		requestEntry.setInterval(Integer.parseInt(entry.get("interval")));
		requestEntry.setVideoFileName(entry.get("videoFileName"));    	
		return requestEntry;
	}

	@Given("request has the following attribuites")
	public void request_has_the_following_attribuites(List<CreateRequestRequestDTO> requestDataTable) {
		createRequestRequestDTOs = requestDataTable;
	}


	@When("create a new request")
	public CreateRequestResponseDTO create_a_new_request() {
		var createRequestRequest = createRequestRequestDTOs.get(0);

		response = given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(createRequestRequest)
				.when()
				.post(ENDPOINT_REQUEST_API);
		return response.then().extract().as(CreateRequestResponseDTO.class);
	}

	@Then("the request is successfully registered")
	public void the_customer_is_successfully_registered() {
		response.then()
		.statusCode(HttpStatus.CREATED.value());
	}

	@Then("should be showed")
	public void should_be_showed() {
		response.then()
		.body(matchesJsonSchemaInClasspath("./schemas/CreateRequestResponse.json"));
	}

	@Given("the request is already registered with the following attributes")
	public void the_request_is_already_registered_with_the_following_attributes(List<CreateRequestRequestDTO> requestDataTable) {
		createRequestRequestDTOs = requestDataTable;
		createRequestResponseDTO = create_a_new_request();
	}

	@When("ask for request information")
	public void ask_for_request_information() {
	    response = given()
	    		.contentType(MediaType.APPLICATION_JSON_VALUE)
	    		.when()
	    		.get(ENDPOINT_REQUEST_API + "/{requestId}", createRequestResponseDTO.getId());
	}

	@Then("the request is retrieved")
	public void the_request_is_retrieved() {
	    response.then()
	    .statusCode(HttpStatus.OK.value())
	    .body(matchesJsonSchemaInClasspath("./schemas/GetRequestResponse.json"));
	}

	@When("gives a new status")
	public void gives_a_new_status() {
		newStatus = "COMPLETED";
		
		UpdateRequestRequestDTO updateRequestRequestDTO = new UpdateRequestRequestDTO();
		
		updateRequestRequestDTO.setStatus(newStatus);		
		
		response = given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(updateRequestRequestDTO)
				.when()
				.patch(ENDPOINT_REQUEST_API + "/{requestId}", createRequestResponseDTO.getId());
	}

	@Then("update the request status")
	public void update_the_request_status() {
	    response.then()
	    .statusCode(HttpStatus.OK.value())
	    .body(matchesJsonSchemaInClasspath("./schemas/UpdateRequestResponse.json"))
	    .body("status", equalTo(newStatus));
	}
}