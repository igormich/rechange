package day1;

import java.awt.Color;

import base.Answer;
import base.DataHolder;
import base.Scene;
import base.SimpleDialog;

public class SherlocDialog extends SimpleDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 262327703397020182L;
	
	private int answerCode;
	@Override
	protected void next(int i) {
		if(i==0) {
			if(DataHolder.is("work","врач") && DataHolder.is("name","Джон")) {
				DataHolder.set("sherlock","1");
				message = new Answer("Добрый вечер, дорогой друг.", Scene.BROWN);
			} else if(DataHolder.is("work","детектив"))
				message = new Answer("Добрый вечер, дорогой коллега.", Scene.BROWN);
			else 
				message = new Answer("Добрый вечер", Scene.BROWN);
			add(new Answer("Кто вы?", Color.ORANGE, () -> answerCode=1));
			add(new Answer("Добрый вечер", Color.ORANGE));
			add(new Answer("Что вы здесь делаете?", Color.ORANGE, () -> answerCode=1));
		} else if(i==1) {
			if(answerCode == 1)
				message = new Answer("Я Шерлок Холмс и пришёл что бы помочь вам.", Scene.BROWN);
			else 
				message = new Answer("Один ваш друг попросил меня помочь вам.", Scene.BROWN);
			answerCode=0;
			add(new Answer("Что надо делать?", Color.ORANGE));
			add(new Answer("Вытащите меня!.", Color.ORANGE, () -> answerCode=1));
			add(new Answer("Почему я здесь?", Color.ORANGE, () -> answerCode=2));
			add(new Answer("В чём я виноват?", Color.ORANGE, () -> answerCode=2));
		} else if(i==2) {
			if(answerCode==1) {
				message = new Answer("Вы же понимаете прямо сейчас это невозможно", Scene.BROWN);
				add(new Answer("Что тогда делать", Color.ORANGE));
				add(new Answer("Почему я здесь?", Color.ORANGE, () -> answerCode=2));
				add(new Answer("В чём я виноват?", Color.ORANGE, () -> answerCode=2));
				node--;
			} else if(answerCode==2) {
				message = new Answer("Это нам и предстоит выяснить.", Scene.BROWN);
				add(new Answer("Но как?", Color.ORANGE));
				add(new Answer("Что надо делать?", Color.ORANGE));
				add(new Answer("Вытащите меня!.", Color.ORANGE, () -> answerCode=1));
				node--;
			} else {
				message = new Answer("Для начала попробуем найти свидетелей.", Scene.BROWN);
				add(new Answer("Но как?", Color.ORANGE));
				add(new Answer("Это непросто", Color.ORANGE));
				add(new Answer("У меня амнезия", Color.ORANGE));
			}
			answerCode = 0;
		}
		if(i==3) {
			message = new Answer("Возможно ваши сны помогут вам", Scene.BROWN);
			add(new Answer("Сны?", Color.ORANGE));
			add(new Answer("Бред какой-то", Color.ORANGE));
			add(new Answer("Я попробую", Color.ORANGE));
		}
		if(i==4) {
			message = new Answer("А теперь проснитесь и пойте!", Scene.BROWN);
			add(new Answer("Что?!", Color.ORANGE));
		}
	}

}
