package day1;

import java.awt.Color;

import base.Answer;
import base.DataHolder;
import base.SimpleDialog;

public class GirlDialog2 extends SimpleDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 262327703397020182L;
	private String next = "";

	@Override
	protected void next(int i) {
		if (i == 0) {
			if (!DataHolder.hasKey("ticket")) {
				DataHolder.set("girl","fail");
				message = new Answer("Придурок! Как ты мог забыть билеты", Color.YELLOW);
				add(new Answer("Извини", Color.ORANGE));
				add(new Answer("Прости", Color.ORANGE));
				add(new Answer("Я исправлюсь", Color.ORANGE));
			} else if (DataHolder.getInt("girl") == 2) {
					message = new Answer("Ты такой милый! Зайдёшь ко мне выпить чаю?", Color.YELLOW);
					add(new Answer("Конечно", Color.ORANGE,() ->DataHolder.set("girl", "home")));
					add(new Answer("Извини срочная работа", Color.ORANGE,() ->DataHolder.set("girl", "friend")));
					add(new Answer("Прости надо делать игру", Color.ORANGE,() ->DataHolder.set("girl", "friend")));
			} else {
				message = new Answer("Ты такой милый! Настоящий друг", Color.YELLOW);
				add(new Answer("Ну я пойду?", Color.ORANGE));
				add(new Answer("Можно к тебе в гости?", Color.ORANGE));
				add(new Answer("Может выпьем чаю?", Color.ORANGE));
			}
		}
	}

}
