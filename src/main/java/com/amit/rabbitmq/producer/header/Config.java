package com.amit.rabbitmq.producer.header;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration("NotificationConfig")
public class Config {
	
	public static final String SKYPE = "Skype";

	public static final String PUBLIC = "public";

	public static final String HANGOUT = "hangout";

	public static final String SLACK = "slack";

	public static final String MODE = "mode";

	public static final String CHANNEL = "channel";
	
	public static final String[] channels = {Config.HANGOUT, Config.SKYPE, Config.SLACK};
	
	public static final String[] mode = {Config.PUBLIC, "private"};

	@Value("${application.notification.exchange}")
	public String notificationExchangeName;
	
	@Value("${application.notification.slack-queue}")
	public String slackQueueName;
	
	@Value("${application.notification.hangout-queue}")
	public String hangoutQueueName;
	
	@Value("${application.notification.all-queue}")
	public String allNotificationQueueName;
	
	@Value("${application.notification.any-queue}")
	public String anyNotificationQueueName;
	
	@Bean
	public HeadersExchange notificationExchange() {
		 return new HeadersExchange(notificationExchangeName , true, false);
	}
	
	@Bean
	public Queue slackQueue() {
		return QueueBuilder.durable(slackQueueName).build();
	}
	
	@Bean
	public Queue hangoutQueue() {
		return QueueBuilder.durable(hangoutQueueName).build();
	}
	
	@Bean
	public Queue allNotificationQueue() {
		return QueueBuilder.durable(allNotificationQueueName).build();
	}
	
	@Bean
	public Queue anyNotificationQueue() {
		return QueueBuilder.durable(anyNotificationQueueName).build();
	}
	
	@Bean
	public Binding slackBinding() {
		return BindingBuilder.bind(slackQueue()).to(notificationExchange()).where(CHANNEL).matches(SLACK);
	}
	
	@Bean
	public Binding hangoutBinding() {
		return BindingBuilder.bind(hangoutQueue()).to(notificationExchange()).where(CHANNEL).matches(HANGOUT);
	}
	
	@Bean
	public Binding exactAllNotificationBinding() {
		Map<String, Object> map = new HashMap<>();
		map.put(CHANNEL, SKYPE);
		map.put(MODE, PUBLIC);
		return BindingBuilder.bind(allNotificationQueue()).to(notificationExchange()).whereAll(map).match();
	}
	
	@Bean
	public Binding anyNotificationBinding() {
		return BindingBuilder.bind(anyNotificationQueue()).to(notificationExchange()).whereAny(CHANNEL, MODE).exist();
	}
	
	
}
