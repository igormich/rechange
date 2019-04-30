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
			message = new Answer("������� ������� ������������� �������� ������", Color.GREEN);
			add(new Answer("������", Color.ORANGE));
			add(new Answer("����� ���?", Color.ORANGE,() -> DataHolder.set("name","������")));
			add(new Answer("���� ����� ��", Color.ORANGE,() -> DataHolder.set("name","����")));
		}
		if(i==1) {
			message = new Answer("����, �� ������� ��� ��� �����?", Color.GREEN);
			add(new Answer("�� �����", Color.ORANGE));
			add(new Answer("������", Color.ORANGE,() -> DataHolder.set("name1","������")));
			add(new Answer("����", Color.ORANGE,() -> DataHolder.set("name1","����")));
			add(new Answer("����", Color.ORANGE,() -> DataHolder.set("name1","����")));
			add(new Answer("����", Color.ORANGE,() -> DataHolder.set("name1","����")));
		}
		if(i==2) {
			if(DataHolder.hasKey("name1"))
				message = new Answer("� ��� �� ����������� ������ �������?", Color.GREEN);
			else
				message = new Answer("����� ������, � ��� �� ����������� �������?", Color.GREEN);
			add(new Answer("�� �����", Color.ORANGE));
			add(new Answer("����", Color.ORANGE,() -> DataHolder.set("work1","����")));
			add(new Answer("������� ��������", Color.ORANGE,() -> DataHolder.set("work1","��������")));
			add(new Answer("��������", Color.ORANGE,() -> DataHolder.set("work1","��������")));
			add(new Answer("�������", Color.ORANGE,() -> DataHolder.set("work1","�������")));;
		}
	}

}
