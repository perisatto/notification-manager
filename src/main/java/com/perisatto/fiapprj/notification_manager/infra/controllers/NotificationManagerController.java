package com.perisatto.fiapprj.notification_manager.infra.controllers;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Controller;

import com.perisatto.fiapprj.notification_manager.application.usecases.CreateNotificationUseCase;
import com.perisatto.fiapprj.notification_manager.domain.entities.Notification;

@Controller
@RabbitListener(queues = "notifications")
public class NotificationManagerController {
	private final CreateNotificationUseCase createNotificationUseCase;

	public NotificationManagerController(CreateNotificationUseCase createNotificationUseCase) {
		this.createNotificationUseCase = createNotificationUseCase;
	}
	
	@RabbitHandler	
	public void processFileRequest(Notification notification) throws Exception {
		createNotificationUseCase.sendEmail(notification);
	}
}