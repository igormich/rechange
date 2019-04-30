package day1;

import java.awt.Color;

import base.Answer;
import base.DataHolder;
import base.SimpleDialog;

public class Sleep1Dialog extends SimpleDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 262327703397020182L;
	private String next = "";

	@Override
	protected void next(int i) {
		if (i == 0) {
			message = new Answer("Вы опаздываете на свидание, что взять?", Color.YELLOW);
			add(new Answer("Скорее бежать", Color.ORANGE));
			add(new Answer("Фотоаппарат", Color.ORANGE, () -> DataHolder.set("photo",1)));
			add(new Answer("Билеты", Color.ORANGE, () -> DataHolder.set("ticket",1)));
			add(new Answer("Галстук бабочку", Color.ORANGE, () -> DataHolder.set("",1)));
		}
	}

}
