package com.amit.rabbitmq.producer.topic;

import java.util.EnumMap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class Shows {

	public static final EnumMap<CHANNEL_NAME, Show[]> SHOW_MAP = new EnumMap<>(CHANNEL_NAME.class);

	@Getter
	@Setter
	@AllArgsConstructor
	public static class Show {

		private String name;

		private SHOW_TYPE type;

	}

	static {

		Show show1 = new Show("Nazar", SHOW_TYPE.SERIAL);
		Show show2 = new Show("Mahabharat", SHOW_TYPE.SERIAL);
		Show show3 = new Show("Dance Plus", SHOW_TYPE.REALITY);
		Show show4 = new Show("Ishqbaaz", SHOW_TYPE.SERIAL);

		Show show5 = new Show("KBC", SHOW_TYPE.REALITY);
		Show show6 = new Show("CID", SHOW_TYPE.REALITY);
		Show show7 = new Show("Aahat", SHOW_TYPE.SERIAL);
		Show show8 = new Show("Indian Idol", SHOW_TYPE.REALITY);

		SHOW_MAP.put(CHANNEL_NAME.STAR, new Show[] { show1, show2, show3, show4 });
		SHOW_MAP.put(CHANNEL_NAME.SONY, new Show[] { show5, show6, show7, show8 });

	}

	public enum CHANNEL_NAME {
		STAR, SONY
	}

	public enum SHOW_TYPE {
		SERIAL, REALITY
	}

}
