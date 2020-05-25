package com.amit.rabbitmq.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RabbitMQProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitMQProducerApplication.class, args);
	}
	

}
