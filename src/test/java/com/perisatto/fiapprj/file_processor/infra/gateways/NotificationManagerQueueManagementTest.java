package com.perisatto.fiapprj.file_processor.infra.gateways;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.perisatto.fiapprj.file_processor.domain.entities.Notification;

@ActiveProfiles(value = "test")
public class NotificationManagerQueueManagementTest {
	@InjectMocks
	private NotificationManagerQueueManagement notificationManagerQueueManagement;
	
	@Mock
	private RabbitTemplate template;
	
	@Mock
	private Queue pendingRequests;
	
	AutoCloseable openMocks;
	
	@BeforeEach
	void setUp() {
		openMocks = MockitoAnnotations.openMocks(this);		
	}
	
	@AfterEach
	void tearDown() throws Exception {
		openMocks.close();
	}
	
	@Test
	void givenRequest_ThenPublishMessage() {		
		when(pendingRequests.getName()).thenReturn("pendingRequests");
		
		Notification notification = new Notification();		
		
		notificationManagerQueueManagement.createNotification(notification);;
		
		verify(template, times(1)).convertAndSend(any(String.class), any(Notification.class));
	}
}
