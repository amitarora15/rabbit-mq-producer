package com.amit.rabbitmq.producer.header;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NotificationPublisher {

	@Value("${application.notification.exchange}")
	public String notificationExchangeName;
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@Scheduled(initialDelay = 1000, fixedDelay = 20000)
	public void publish() {
		Notification match = new Notification();
		
		String channel = Config.channels[ThreadLocalRandom.current().nextInt(3)];
		String mode = Config.mode[ThreadLocalRandom.current().nextInt(2)];
		
		match.setId(String.valueOf(System.currentTimeMillis()));
		match.setMesssage("FIFA match update Result to be published on channel " + channel + " with mode " + mode);
		log.info("Sending the FIFA match Notification update to channels - " + match);
		
		amqpTemplate.convertAndSend(notificationExchangeName, "", match, new MessagePostProcessor() {
			
			@Override
			public Message postProcessMessage(Message message) throws AmqpException {
				message.getMessageProperties().setHeader(Config.CHANNEL, channel);
				message.getMessageProperties().setHeader(Config.MODE, mode);
				return message;
			}
		});
	}
	
}
