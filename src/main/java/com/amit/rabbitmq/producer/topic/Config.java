package com.amit.rabbitmq.producer.topic;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "TvshowConfig")
public class Config {

	@Value("${application.tvshow.exchange}")
	public String tvShowExchangeName;

	@Value("${application.tvshow.star-queue}")
	public String starChannelQueueName;

	@Value("${application.tvshow.sony-queue}")
	public String sonyChannelQueueName;

	public static final String SONY_MESSAGE_ROUTING_KEY = "tvshow.telecast.sony.*";

	public static final String STAR_MESSAGE_ROUTING_KEY = "tvshow.telecast.star.*";

	@Bean
	public TopicExchange tvShowExchange() {
		return new TopicExchange(tvShowExchangeName, true, false);
	}

	@Bean
	public Queue sonyChannelTvShowQueue() {
		return QueueBuilder.durable(sonyChannelQueueName).build();
	}

	@Bean
	public Queue starChannelTvShowQueue() {
		return QueueBuilder.durable(starChannelQueueName).build();
	}

	@Bean
	public Binding sonyTvShowBinding() {
		return BindingBuilder.bind(sonyChannelTvShowQueue()).to(tvShowExchange()).with(SONY_MESSAGE_ROUTING_KEY);
	}

	@Bean
	public Binding starTvShowBinding() {
		return BindingBuilder.bind(starChannelTvShowQueue()).to(tvShowExchange()).with(STAR_MESSAGE_ROUTING_KEY);
	}

}
