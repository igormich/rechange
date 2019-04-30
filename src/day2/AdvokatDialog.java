package day2;

import java.awt.Color;

import base.Answer;
import base.DataHolder;
import base.Dialog;
import base.Scene;
import base.SimpleDialog;

public class AdvokatDialog extends SimpleDialog implements Dialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3092314130355881510L;
	private int answerCode;

	@Override
	protected void next(int i) {
		if(i==0) {
			message = new Answer("Доброе утро", Scene.LIGTH_BLUE1);
			add(new Answer("Кто вы?", Color.ORANGE, () -> answerCode=1));
			add(new Answer("Вы мой адвокат?", Color.ORANGE, () -> answerCode=2));
			add(new Answer("В чём я виноват?", Color.ORANGE, () -> answerCode=3));
		} else if(i==1) {
			if(answerCode == 1){
				message = new Answer("Я ваш адвокат, врач сказал что у вас амнезия.", Scene.LIGTH_BLUE1);
				add(new Answer("В чём я виноват?", Color.ORANGE, () -> answerCode=3));
				add(new Answer("Что я сделал?", Color.ORANGE, () -> answerCode=3));
				add(new Answer("В чём меня обвиняют?", Color.ORANGE, () -> answerCode=3));
			}
			if(answerCode == 2){
				message = new Answer("Да, врач сказал что у вас амнезия.", Scene.LIGTH_BLUE1);
				add(new Answer("В чём я виноват?", Color.ORANGE, () -> answerCode=3));
				add(new Answer("Что я сделал?", Color.ORANGE, () -> answerCode=3));
				add(new Answer("В чём меня обвиняют?", Color.ORANGE, () -> answerCode=3));
			}
			if(answerCode == 3){
				message = new Answer("Ах да, врач же сказал что у вас амнезия.", Scene.LIGTH_BLUE1);
				add(new Answer("И всё же?", Color.ORANGE, () -> answerCode=1));
				add(new Answer("Насколько всё плохо?", Color.ORANGE, () -> answerCode=1));
				add(new Answer("Что я сделал?", Color.ORANGE, () -> answerCode=3));
			}
		} else if(i==2) {
			message = new Answer("Вас обвиняют в серийных убийствах.", Scene.LIGTH_BLUE1);
			add(new Answer("Ого!", Color.ORANGE, () -> answerCode=1));
			add(new Answer("Но почему?", Color.ORANGE, () -> answerCode=1));
			add(new Answer("Из за чего?", Color.ORANGE, () -> answerCode=3));
		} else if(i==3) {
			message = new Answer("Вас нашли без сознания рядом с телом.", Scene.LIGTH_BLUE1);
			add(new Answer("И что делать?", Color.ORANGE, () -> answerCode=1));
			add(new Answer("Но я ничего не помню", Color.ORANGE, () -> answerCode=2));
			add(new Answer("Чьим?", Color.ORANGE, () -> answerCode=3));
		} else if(i==4) {
			if(answerCode == 1){
				message = new Answer("Надо постараться найти свидетелей", Scene.LIGTH_BLUE1);
				add(new Answer("Но как?", Color.ORANGE, () -> answerCode=2));
				add(new Answer("Это непросто", Color.ORANGE, () -> answerCode=2));
				add(new Answer("У меня амнезия", Color.ORANGE, () -> answerCode=2));
				node--;
			}
			if(answerCode == 2){
				message = new Answer("Может быть хоть какие нибудь воспоминания?", Scene.LIGTH_BLUE1);
				add(new Answer("Ничего не помню", Color.ORANGE, () -> {
					DataHolder.set("girl","fail");
					answerCode=1;
				}));
				add(new Answer("Девушка из дома №5", Color.ORANGE, () -> {
					answerCode=2;	
				}));
				add(new Answer("Девушка из дома №3", Color.ORANGE, () -> {
					DataHolder.set("girl","fail");
					answerCode=2;
				}));
				add(new Answer("Девушка из дома №7", Color.ORANGE, () -> {
					DataHolder.set("girl","fail");
					answerCode=1;
				}));
			}
			if(answerCode == 3){
				message = new Answer("Бездомного, все убитые были бомжами.", Scene.LIGTH_BLUE1);
				add(new Answer("И что делать?", Color.ORANGE, () -> answerCode=1));
				add(new Answer("Но я ничего не помню", Color.ORANGE, () -> answerCode=2));
				node--;
			}
		} else if(i==5) {
			if(answerCode == 1){
				message = new Answer("Очень жаль, если что нибудь вспомните обязательно скажите", Scene.LIGTH_BLUE1);
				add(new Answer("Хорошо", Color.ORANGE, () -> answerCode=1));
			}
			if(answerCode == 2){
				message = new Answer("Я попробую с ней связаться", Scene.LIGTH_BLUE1);
				add(new Answer("Спасибо", Color.ORANGE, () -> answerCode=1));
			}
		}

	}

}
