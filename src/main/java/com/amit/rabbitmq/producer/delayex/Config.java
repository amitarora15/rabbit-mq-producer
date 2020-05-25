package com.amit.rabbitmq.producer.delayex;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("ImageCompressionConfig")
public class Config {

	@Value("${application.image.compression.exchange}")
	private String imageCompressionExchange;
	
	@Value("${application.image.compression.queue}")
	private String imageCompressionQueue;
	
	public static final String IMAGE_COMPRESSION_ROUTING_KEY = "image.compression.key";
	
	@Bean
	public CustomExchange imageCompressionExchange() {
		Map<String, Object> args = new HashMap<>();
	    args.put("x-delayed-type", "direct");
		return new CustomExchange(imageCompressionExchange, "x-delayed-message", true, false, args);
	}

	@Bean
	public Queue imageCompressionQueue() {
		return QueueBuilder.durable(imageCompressionQueue).build();
	}

	@Bean
	public Binding imageCompressionBinding() {
		return BindingBuilder.bind(imageCompressionQueue()).to(imageCompressionExchange())
				.with(IMAGE_COMPRESSION_ROUTING_KEY).noargs();
	}

}
