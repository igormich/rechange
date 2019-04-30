package day1;

import java.awt.Color;

import base.Answer;
import base.DataHolder;
import base.SimpleDialog;

public class GirlDialog1 extends SimpleDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 262327703397020182L;
	private String next = "";

	@Override
	protected void next(int i) {
		if (i == 0) {
			message = new Answer("Ты опоздал!", Color.YELLOW);
			add(new Answer("Извини", Color.ORANGE));
			if (DataHolder.hasKey("flower"))
				add(new Answer("Я принёс цветы", Color.ORANGE, () -> next = "flower"));
			if (DataHolder.hasKey("photo"))
				add(new Answer("Искал фотоаппарат", Color.ORANGE, () -> next = "photo"));
			if (DataHolder.hasKey("ticket"))
				add(new Answer("Искал билеты", Color.ORANGE, () -> next = "ticket"));
			add(new Answer("Я проспал", Color.ORANGE, () -> next = "sleep"));
		}
		if (i == 1) {
			if (next.equals("flower")) {
				if (DataHolder.is("flower", "fail")) {
					message = new Answer("Ага я видела как ты рвал их у соседей!", Color.YELLOW);
					DataHolder.dec("girl");
					add(new Answer("Так вышло", Color.ORANGE));
					add(new Answer("Извини", Color.ORANGE));
					node--;
				} else {
					message = new Answer("Ну хорошо я тебя прощаю.", Color.YELLOW);
					DataHolder.inc("girl");
					add(new Answer("Идём на концерт", Color.ORANGE));
					if (!DataHolder.hasKey("photo"))
						add(new Answer("Кажется, я забыл фотоаппарат", Color.ORANGE, () -> next = "nophoto"));
					if (!DataHolder.hasKey("ticket"))
						add(new Answer("Кажется, я забыл билеты", Color.ORANGE, () -> next = "noticket"));
				}
			} else if (next.equals("sleep")) {
				message = new Answer("По крайней мере честно.", Color.YELLOW);
				add(new Answer("Идём на концерт", Color.ORANGE));
				if (!DataHolder.hasKey("photo"))
					add(new Answer("Кажется я забыл фотоаппарат", Color.ORANGE, () -> next = "nophoto"));
				if (!DataHolder.hasKey("ticket"))
					add(new Answer("Кажется я забыл билеты", Color.ORANGE, () -> next = "noticket"));
			} else {
				message = new Answer("Ну хорошо...", Color.YELLOW);
				add(new Answer("Идём на концерт", Color.ORANGE));
				if (!DataHolder.hasKey("photo"))
					add(new Answer("Кажется я забыл фотоаппарат", Color.ORANGE, () -> next = "nophoto"));
				if (!DataHolder.hasKey("ticket"))
					add(new Answer("Кажется я забыл билеты", Color.ORANGE, () -> next = "noticket"));
			} 
			next="";
		}
		if (i == 2) {
			if (next.equals("noticket")) {
				DataHolder.set("girl","fail");
				message = new Answer("А голову ты дома не забыл? Идиот!", Color.YELLOW);
				add(new Answer("Так вышло", Color.ORANGE));
				add(new Answer("Извини", Color.ORANGE));
			} else if (next.equals("nophoto")) {
				message = new Answer("А голову ты дома не забыл? Ну идём так", Color.YELLOW);
				add(new Answer("Так вышло", Color.ORANGE));
				add(new Answer("Прости", Color.ORANGE));
				add(new Answer("Хорошо, любимая", Color.ORANGE, () -> DataHolder.inc("girl")));
			} else {
				message = new Answer("Ты всё взял?", Color.YELLOW);
				if (!DataHolder.hasKey("photo"))
					add(new Answer("Я забыл фотоаппарат", Color.ORANGE, () -> next = "nophoto"));
				if (!DataHolder.hasKey("ticket"))
					add(new Answer("Я забыл билеты", Color.ORANGE, () -> next = "noticket"));
				add(new Answer("Да всё", Color.ORANGE));
				node--;
			}
			next="";
		}
	}

}
