package com.perisatto.fiapprj.notification_manager;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class NotificationManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationManagerApplication.class, args);
	}
}
