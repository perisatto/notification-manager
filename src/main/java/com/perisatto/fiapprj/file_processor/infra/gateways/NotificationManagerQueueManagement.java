package com.perisatto.fiapprj.file_processor.infra.gateways;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.perisatto.fiapprj.file_processor.application.interfaces.NotificationManagement;
import com.perisatto.fiapprj.file_processor.domain.entities.Notification;

public class NotificationManagerQueueManagement implements NotificationManagement {	
	
    @Autowired
    private RabbitTemplate template;
    
    @Autowired
    private Queue notifications;
	
	@Override
	public void createNotification(Notification notification) {						
		template.convertAndSend(notifications.getName(), notification);
	}
}
