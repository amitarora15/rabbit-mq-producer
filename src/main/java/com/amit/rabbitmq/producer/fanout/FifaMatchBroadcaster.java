package com.amit.rabbitmq.producer.fanout;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.amit.rabbitmq.producer.fanout.Match.PLAYING_COUNTRIES;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FifaMatchBroadcaster {

	@Value("${application.fifa.exchange}")
	public String fifaUpdateExchangeName;
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@Scheduled(initialDelay = 1000, fixedDelay = 20000)
	public void broadCast() {
		int opponent1 = ThreadLocalRandom.current().nextInt(0, Match.PLAYING_COUNTRIES.values().length);
		int opponent2 = ThreadLocalRandom.current().nextInt(0, Match.PLAYING_COUNTRIES.values().length);
		while(opponent1 == opponent2) {
			opponent2 = ThreadLocalRandom.current().nextInt(0, Match.PLAYING_COUNTRIES.values().length);
		}
		Match match = new Match();
		match.setOpponent1(PLAYING_COUNTRIES.values()[opponent1]);
		match.setOpponent2(PLAYING_COUNTRIES.values()[opponent2]);
		match.setTime(Instant.now().toString());
		int score1 = ThreadLocalRandom.current().nextInt(0, 4);
		int score2 = ThreadLocalRandom.current().nextInt(0, 4);
		match.setScore(score1 + "-" + score2);
		log.info("Sending the match update to all channels - " + match);
		amqpTemplate.convertAndSend(fifaUpdateExchangeName, Config.FIFA_MESSAGE_ROUTING_KEY, match);
	}
	
}
