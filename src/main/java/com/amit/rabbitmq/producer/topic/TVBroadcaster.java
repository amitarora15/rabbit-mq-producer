package com.amit.rabbitmq.producer.topic;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.PostConstruct;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TVBroadcaster {
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@Value("${application.tvshow.exchange}")
	public String tvShowExchangeName;

	@PostConstruct
	public void publishShowSchedule() {

		Shows.SHOW_MAP.keySet().stream().forEach(channel -> {
			Arrays.asList(Shows.SHOW_MAP.get(channel)).stream().forEach(show -> {
				Telecast telecast = new Telecast();
				telecast.setShowName(show.getName());
				int hour = ThreadLocalRandom.current().nextInt(0, 23);
				int min = ThreadLocalRandom.current().nextInt(0, 59);
				telecast.setTime(hour + ":" + min);
				String routingKey = "tvshow.telecast." + channel.toString().toLowerCase() + "." + show.getType().toString().toLowerCase();
				log.info("Publishing telecast timing of show (" + routingKey + ")- " + telecast);
				amqpTemplate.convertAndSend(tvShowExchangeName, routingKey, telecast);
			});
		}

		);

	}

}
