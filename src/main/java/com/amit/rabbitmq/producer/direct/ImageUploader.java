package com.amit.rabbitmq.producer.direct;

import javax.annotation.PostConstruct;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ImageUploader {

	@Value("${application.image.upload.exchange}")
	private String imageUploadExchange;
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@PostConstruct
	public void imageUpload() {
		Image image = new Image();
		image.setImageId("1");
		image.setPath("/test/path");
		log.info("Sending image upload to processor - " + image);
		amqpTemplate.convertAndSend(imageUploadExchange, Config.IMAGE_UPLOAD_ROUTING_KEY, image);
	}
	
}
