package day1;

import base.Character;
import base.DataHolder;
import base.Message;
import base.Scene;

public class CopAngry {


	private static final String CopAngryCounter = "CopAngryCounter";

	public static Message nextMessage(Character cop) {
		Message message;
		switch (DataHolder.getInt(CopAngryCounter)) {
		case 0:
			message = new Message(cop.getX(), cop.getYHead(), "Не дергайся!", 4, Scene.LIGTH_BLUE);
			break;
		case 1:
			message = new Message(cop.getX(), cop.getYHead(), "Куда вырываешься?", 4, Scene.LIGTH_BLUE);
			break;
		case 2:
			message = new Message(cop.getX(), cop.getYHead(), "Спокойно себя веди!", 4, Scene.LIGTH_BLUE);
			break;
		case 3:
			message = new Message(cop.getX(), cop.getYHead(), "Ты что бешенный?!", 4, Scene.LIGTH_BLUE);
			break;
		default:
			DataHolder.set(CopAngryCounter, 0);
			message = new Message(cop.getX(), cop.getYHead(), "Тебя ударить что ли?", 4, Scene.LIGTH_BLUE);
			break;
		}
		DataHolder.inc(CopAngryCounter);
		return message;
	}

}
