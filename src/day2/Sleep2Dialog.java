package day2;

import java.awt.Color;

import base.Answer;
import base.DataHolder;
import base.SimpleDialog;

public class Sleep2Dialog extends SimpleDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 262327703397020182L;
	private String next = "";

	@Override
	protected void next(int i) {
		if (i == 0) {
			message = new Answer("Надо вспомнить что вы купили тут", Color.YELLOW);
			add(new Answer("Хлеб", Color.ORANGE));
			add(new Answer("Топор", Color.ORANGE, () -> DataHolder.set("axe",1)));
			add(new Answer("Напалм", Color.ORANGE, () -> DataHolder.set("napalm",1)));
			add(new Answer("Крысиный яд", Color.ORANGE, () -> DataHolder.set("poison",1)));
		}
	}

}
