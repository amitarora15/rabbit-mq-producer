package com.amit.rabbitmq.producer.fanout;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Match {
	
	public static enum PLAYING_COUNTRIES{US, ENG, BRAZIL, SWEDEN, GER, ITALY, PORTUGAL};
	
	private PLAYING_COUNTRIES opponent1;
	
	private PLAYING_COUNTRIES opponent2;
	
	private String score;
	
	private String time;
	
}
