package com.amit.rabbitmq.producer.fanout;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("FifaConfig")
public class Config {

	@Value("${application.fifa.exchange}")
	public String fifaUpdateExchangeName;

	@Value("${application.fifa.dlq-exchange}")
	public String fifaDlqExchangeName;

	@Value("${application.fifa.espn-queue}")
	public String espnChannelQueueName;

	@Value("${application.fifa.star-queue}")
	public String starChannelQueueName;

	@Value("${application.fifa.star-failure-queue}")
	public String starChannelfailureQueueName;

	@Value("${application.fifa.espn-failure-queue}")
	public String espnChannelfailureQueueName;

	@Value("${application.fifa.espn-failure-withdlq-queue}")
	public String espnChannelFailureWithDlqQueueName;

	@Value("${application.fifa.espn-failure-dlq}")
	public String espnChannelFailureDlqName;

	@Value("${application.fifa.star-failure-withdlqttl-queue}")
	public String starChannelFailureWithDlqTtlQueueName;

	@Value("${application.fifa.star-failure-dlq}")
	public String starChannelFailureDlqName;

	public static final String FIFA_MESSAGE_ROUTING_KEY = "fifa.update.key";

	public static final String FIFA_MESSAGE_ESPN_DLQ_ROUTING_KEY = "fifa.espn.dlq.key";

	public static final String FIFA_MESSAGE_STAR_DLQ_ROUTING_KEY = "fifa.star.dlq.key";

	@Bean
	public FanoutExchange fifaUpdateExchange() {
		return new FanoutExchange(fifaUpdateExchangeName, true, false);
	}

	@Bean
	public DirectExchange fifaDlqExchange() {
		return new DirectExchange(fifaDlqExchangeName, true, false);
	}

	@Bean
	public Queue espnChannelQueue() {
		return QueueBuilder.durable(espnChannelQueueName).build();
	}

	@Bean
	public Queue starChannelQueue() {
		return QueueBuilder.durable(starChannelQueueName).build();
	}

	@Bean
	public Queue startChannelFailureQueue() {
		return QueueBuilder.durable(starChannelfailureQueueName).build();
	}

	@Bean
	public Queue espnChannelFailureQueue() {
		return QueueBuilder.durable(espnChannelfailureQueueName).build();
	}

	@Bean
	public Queue espnChannelFailureWithDlqQueue() { //ESPN DLQ Listener consumes the message in end
		return QueueBuilder.durable(espnChannelFailureWithDlqQueueName)
				.withArgument("x-dead-letter-exchange", fifaDlqExchangeName)
				.withArgument("x-dead-letter-routing-key", FIFA_MESSAGE_ESPN_DLQ_ROUTING_KEY).build();
	}

	@Bean
	public Queue espnChannelFailureDlq() {
		return QueueBuilder.durable(espnChannelFailureDlqName).build();
	}

	@Bean
	public Queue starChannelFailureWithDlqTtlQueue() { //No Listener here, and in DLQ we have to wait for message and in end we have to pass it to normal working queue
		return QueueBuilder.durable(starChannelFailureWithDlqTtlQueueName)
				.withArgument("x-dead-letter-exchange", fifaDlqExchangeName)
				.withArgument("x-dead-letter-routing-key", FIFA_MESSAGE_STAR_DLQ_ROUTING_KEY).build();
	}

	@Bean
	public Queue starChannelFailureDlq() {
		// Put this message in DLQ for 60s
		return QueueBuilder.durable(starChannelFailureDlqName).withArgument("x-dead-letter-exchange", "")
				.withArgument("x-message-ttl", 60000)
				.withArgument("x-dead-letter-routing-key", starChannelFailureWithDlqTtlQueueName).build();
	}

	@Bean
	public Binding espnChannelBinding() {
		return BindingBuilder.bind(espnChannelQueue()).to(fifaUpdateExchange());
	}

	@Bean
	public Binding starChannelBinding() {
		return BindingBuilder.bind(starChannelQueue()).to(fifaUpdateExchange());
	}

	@Bean
	public Binding starChannelFailureBinding() {
		return BindingBuilder.bind(startChannelFailureQueue()).to(fifaUpdateExchange());
	}

	@Bean
	public Binding espnChannelFailureBinding() {
		return BindingBuilder.bind(espnChannelFailureQueue()).to(fifaUpdateExchange());
	}

	@Bean
	public Binding espnChannelFailureWithDqlQuqeueBinding() {
		return BindingBuilder.bind(espnChannelFailureWithDlqQueue()).to(fifaUpdateExchange());
	}

	@Bean
	public Binding espnChannelFailureDlqBinding() {
		return BindingBuilder.bind(espnChannelFailureDlq()).to(fifaDlqExchange())
				.with(FIFA_MESSAGE_ESPN_DLQ_ROUTING_KEY);
	}

	@Bean
	public Binding starChannelFailureWithDqlTtlQuqeueBinding() {
		return BindingBuilder.bind(starChannelFailureWithDlqTtlQueue()).to(fifaUpdateExchange());
	}

	@Bean
	public Binding starChannelFailureDlqBinding() {
		return BindingBuilder.bind(starChannelFailureDlq()).to(fifaDlqExchange())
				.with(FIFA_MESSAGE_STAR_DLQ_ROUTING_KEY);
	}

}
