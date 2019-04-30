package day1;

import java.awt.Color;

import base.Answer;
import base.DataHolder;
import base.SimpleDialog;

public class DocDialog1 extends SimpleDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 262327703397020182L;

	@Override
	protected void next(int i) {
		if(i==0) {
			message = new Answer("Вы помните как вас зовут?", Color.GREEN);
			add(new Answer("Не помню", Color.ORANGE));
			add(new Answer("Родион", Color.ORANGE,() -> DataHolder.set("name","Родион")));
			add(new Answer("Ганс", Color.ORANGE,() -> DataHolder.set("name","Ганс")));
			add(new Answer("Джон", Color.ORANGE,() -> DataHolder.set("name","Джон")));
			add(new Answer("Джек", Color.ORANGE,() -> DataHolder.set("name","Джек")));
		}
		if(i==1) {
			if(DataHolder.hasKey("name"))
				message = new Answer("А чем вы занимаетесь можете сказать?", Color.GREEN);
			else
				message = new Answer("Очень хорошо, а чем вы занимаетесь, помните?", Color.GREEN);
			add(new Answer("Не помню", Color.ORANGE));
			add(new Answer("Врач", Color.ORANGE,() -> DataHolder.set("work","врач")));
			add(new Answer("Частный детектив", Color.ORANGE,() -> DataHolder.set("work","детектив")));
			add(new Answer("Охранник", Color.ORANGE,() -> DataHolder.set("work","охранник")));
			add(new Answer("Студент", Color.ORANGE,() -> DataHolder.set("work","студент")));;
		}
		if(i==2) {
			if(DataHolder.is("work","врач")) {
				message = new Answer("Отлично, коллега, и последний вопрос: вы помните, что случилось?", Color.GREEN);
			} else
				message = new Answer("Вы помните, как попали сюда?", Color.GREEN);
			add(new Answer("Не помню", Color.ORANGE));
			add(new Answer("На меня напали?", Color.ORANGE));
			add(new Answer("Я упал?", Color.ORANGE));
			add(new Answer("Я потерял сознание?", Color.ORANGE));
			add(new Answer("Я Поскользнулся?", Color.ORANGE));
		}
	}

}
