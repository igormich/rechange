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
			message = new Answer("������ ����", Scene.LIGTH_BLUE1);
			add(new Answer("��� ��?", Color.ORANGE, () -> answerCode=1));
			add(new Answer("�� ��� �������?", Color.ORANGE, () -> answerCode=2));
			add(new Answer("� ��� � �������?", Color.ORANGE, () -> answerCode=3));
		} else if(i==1) {
			if(answerCode == 1){
				message = new Answer("� ��� �������, ���� ������ ��� � ��� �������.", Scene.LIGTH_BLUE1);
				add(new Answer("� ��� � �������?", Color.ORANGE, () -> answerCode=3));
				add(new Answer("��� � ������?", Color.ORANGE, () -> answerCode=3));
				add(new Answer("� ��� ���� ��������?", Color.ORANGE, () -> answerCode=3));
			}
			if(answerCode == 2){
				message = new Answer("��, ���� ������ ��� � ��� �������.", Scene.LIGTH_BLUE1);
				add(new Answer("� ��� � �������?", Color.ORANGE, () -> answerCode=3));
				add(new Answer("��� � ������?", Color.ORANGE, () -> answerCode=3));
				add(new Answer("� ��� ���� ��������?", Color.ORANGE, () -> answerCode=3));
			}
			if(answerCode == 3){
				message = new Answer("�� ��, ���� �� ������ ��� � ��� �������.", Scene.LIGTH_BLUE1);
				add(new Answer("� �� ��?", Color.ORANGE, () -> answerCode=1));
				add(new Answer("��������� �� �����?", Color.ORANGE, () -> answerCode=1));
				add(new Answer("��� � ������?", Color.ORANGE, () -> answerCode=3));
			}
		} else if(i==2) {
			message = new Answer("��� �������� � �������� ���������.", Scene.LIGTH_BLUE1);
			add(new Answer("���!", Color.ORANGE, () -> answerCode=1));
			add(new Answer("�� ������?", Color.ORANGE, () -> answerCode=1));
			add(new Answer("�� �� ����?", Color.ORANGE, () -> answerCode=3));
		} else if(i==3) {
			message = new Answer("��� ����� ��� �������� ����� � �����.", Scene.LIGTH_BLUE1);
			add(new Answer("� ��� ������?", Color.ORANGE, () -> answerCode=1));
			add(new Answer("�� � ������ �� �����", Color.ORANGE, () -> answerCode=2));
			add(new Answer("����?", Color.ORANGE, () -> answerCode=3));
		} else if(i==4) {
			if(answerCode == 1){
				message = new Answer("���� ����������� ����� ����������", Scene.LIGTH_BLUE1);
				add(new Answer("�� ���?", Color.ORANGE, () -> answerCode=2));
				add(new Answer("��� ��������", Color.ORANGE, () -> answerCode=2));
				add(new Answer("� ���� �������", Color.ORANGE, () -> answerCode=2));
				node--;
			}
			if(answerCode == 2){
				message = new Answer("����� ���� ���� ����� ������ ������������?", Scene.LIGTH_BLUE1);
				add(new Answer("������ �� �����", Color.ORANGE, () -> {
					DataHolder.set("girl","fail");
					answerCode=1;
				}));
				add(new Answer("������� �� ���� �5", Color.ORANGE, () -> {
					answerCode=2;	
				}));
				add(new Answer("������� �� ���� �3", Color.ORANGE, () -> {
					DataHolder.set("girl","fail");
					answerCode=2;
				}));
				add(new Answer("������� �� ���� �7", Color.ORANGE, () -> {
					DataHolder.set("girl","fail");
					answerCode=1;
				}));
			}
			if(answerCode == 3){
				message = new Answer("����������, ��� ������ ���� �������.", Scene.LIGTH_BLUE1);
				add(new Answer("� ��� ������?", Color.ORANGE, () -> answerCode=1));
				add(new Answer("�� � ������ �� �����", Color.ORANGE, () -> answerCode=2));
				node--;
			}
		} else if(i==5) {
			if(answerCode == 1){
				message = new Answer("����� ����, ���� ��� ������ ��������� ����������� �������", Scene.LIGTH_BLUE1);
				add(new Answer("������", Color.ORANGE, () -> answerCode=1));
			}
			if(answerCode == 2){
				message = new Answer("� �������� � ��� ���������", Scene.LIGTH_BLUE1);
				add(new Answer("�������", Color.ORANGE, () -> answerCode=1));
			}
		}

	}

}
