package com.amit.rabbitmq.producer.direct;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("ImageConfig")
public class Config {

	@Value("${application.image.upload.exchange}")
	private String imageUploadExchange;
	
	@Value("${application.image.operation.queue}")
	private String imageOperationQueue;
	
	public static final String IMAGE_UPLOAD_ROUTING_KEY = "image.upload.key";
	
	@Bean
	public DirectExchange imageOperationExchange() {
		return new DirectExchange(imageUploadExchange, true, false);
	}

	@Bean
	public Queue imageOperationlQueue() {
		return QueueBuilder.durable(imageOperationQueue).build();
	}

	@Bean
	public Binding imageOperationBinding() {
		return BindingBuilder.bind(imageOperationlQueue()).to(imageOperationExchange())
				.with(IMAGE_UPLOAD_ROUTING_KEY);
	}

}
