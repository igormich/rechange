package day2;

import java.awt.Color;

import base.Answer;
import base.DataHolder;
import base.SimpleDialog;

public class DocDialog2 extends SimpleDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 262327703397020182L;

	@Override
	protected void next(int i) {
		if(i==0) {
			message = new Answer("Терапия амнезии подразумевает повторые опросы", Color.GREEN);
			add(new Answer("Хорошо", Color.ORANGE));
			add(new Answer("Зачем это?", Color.ORANGE,() -> DataHolder.set("name","Родион")));
			add(new Answer("Бред какой то", Color.ORANGE,() -> DataHolder.set("name","Ганс")));
		}
		if(i==1) {
			message = new Answer("Итак, вы помните как вас зовут?", Color.GREEN);
			add(new Answer("Не помню", Color.ORANGE));
			add(new Answer("Родион", Color.ORANGE,() -> DataHolder.set("name1","Родион")));
			add(new Answer("Ганс", Color.ORANGE,() -> DataHolder.set("name1","Ганс")));
			add(new Answer("Джон", Color.ORANGE,() -> DataHolder.set("name1","Джон")));
			add(new Answer("Джек", Color.ORANGE,() -> DataHolder.set("name1","Джек")));
		}
		if(i==2) {
			if(DataHolder.hasKey("name1"))
				message = new Answer("А чем вы занимаетесь можете сказать?", Color.GREEN);
			else
				message = new Answer("Очень хорошо, а чем вы занимаетесь помните?", Color.GREEN);
			add(new Answer("Не помню", Color.ORANGE));
			add(new Answer("Врач", Color.ORANGE,() -> DataHolder.set("work1","врач")));
			add(new Answer("Частный детектив", Color.ORANGE,() -> DataHolder.set("work1","детектив")));
			add(new Answer("Охранник", Color.ORANGE,() -> DataHolder.set("work1","охранник")));
			add(new Answer("Студент", Color.ORANGE,() -> DataHolder.set("work1","студент")));;
		}
	}

}
