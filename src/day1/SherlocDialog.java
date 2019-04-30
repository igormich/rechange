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
			if(DataHolder.is("work","����") && DataHolder.is("name","����")) {
				DataHolder.set("sherlock","1");
				message = new Answer("������ �����, ������� ����.", Scene.BROWN);
			} else if(DataHolder.is("work","��������"))
				message = new Answer("������ �����, ������� �������.", Scene.BROWN);
			else 
				message = new Answer("������ �����", Scene.BROWN);
			add(new Answer("��� ��?", Color.ORANGE, () -> answerCode=1));
			add(new Answer("������ �����", Color.ORANGE));
			add(new Answer("��� �� ����� �������?", Color.ORANGE, () -> answerCode=1));
		} else if(i==1) {
			if(answerCode == 1)
				message = new Answer("� ������ ����� � ������ ��� �� ������ ���.", Scene.BROWN);
			else 
				message = new Answer("���� ��� ���� �������� ���� ������ ���.", Scene.BROWN);
			answerCode=0;
			add(new Answer("��� ���� ������?", Color.ORANGE));
			add(new Answer("�������� ����!.", Color.ORANGE, () -> answerCode=1));
			add(new Answer("������ � �����?", Color.ORANGE, () -> answerCode=2));
			add(new Answer("� ��� � �������?", Color.ORANGE, () -> answerCode=2));
		} else if(i==2) {
			if(answerCode==1) {
				message = new Answer("�� �� ��������� ����� ������ ��� ����������", Scene.BROWN);
				add(new Answer("��� ����� ������", Color.ORANGE));
				add(new Answer("������ � �����?", Color.ORANGE, () -> answerCode=2));
				add(new Answer("� ��� � �������?", Color.ORANGE, () -> answerCode=2));
				node--;
			} else if(answerCode==2) {
				message = new Answer("��� ��� � ��������� ��������.", Scene.BROWN);
				add(new Answer("�� ���?", Color.ORANGE));
				add(new Answer("��� ���� ������?", Color.ORANGE));
				add(new Answer("�������� ����!.", Color.ORANGE, () -> answerCode=1));
				node--;
			} else {
				message = new Answer("��� ������ ��������� ����� ����������.", Scene.BROWN);
				add(new Answer("�� ���?", Color.ORANGE));
				add(new Answer("��� ��������", Color.ORANGE));
				add(new Answer("� ���� �������", Color.ORANGE));
			}
			answerCode = 0;
		}
		if(i==3) {
			message = new Answer("�������� ���� ��� ������� ���", Scene.BROWN);
			add(new Answer("���?", Color.ORANGE));
			add(new Answer("���� �����-��", Color.ORANGE));
			add(new Answer("� ��������", Color.ORANGE));
		}
		if(i==4) {
			message = new Answer("� ������ ���������� � �����!", Scene.BROWN);
			add(new Answer("���?!", Color.ORANGE));
		}
	}

}
