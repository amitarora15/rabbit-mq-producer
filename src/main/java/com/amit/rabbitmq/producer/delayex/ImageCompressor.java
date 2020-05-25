package com.amit.rabbitmq.producer.delayex;

import javax.annotation.PostConstruct;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ImageCompressor {

	@Value("${application.image.compression.exchange}")
	private String imageCompressionExchange;

	@Autowired
	private AmqpTemplate amqpTemplate;

	@PostConstruct
	public void imageUpload() {
		Image image = new Image();
		image.setImageId("2");
		image.setPath("/test/path2");
		log.info("Sending image to compressor - " + image);
		amqpTemplate.convertAndSend(imageCompressionExchange, Config.IMAGE_COMPRESSION_ROUTING_KEY, image,  new MessagePostProcessor() {
			
			@Override
			public Message postProcessMessage(Message message) throws AmqpException {
				message.getMessageProperties().setHeader("x-delay", 60000); //Adding 60 second delay
			            return message;
			}
		});
	}

}
