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
			message = new Answer("�� �������!", Color.YELLOW);
			add(new Answer("������", Color.ORANGE));
			if (DataHolder.hasKey("flower"))
				add(new Answer("� ����� �����", Color.ORANGE, () -> next = "flower"));
			if (DataHolder.hasKey("photo"))
				add(new Answer("����� �����������", Color.ORANGE, () -> next = "photo"));
			if (DataHolder.hasKey("ticket"))
				add(new Answer("����� ������", Color.ORANGE, () -> next = "ticket"));
			add(new Answer("� �������", Color.ORANGE, () -> next = "sleep"));
		}
		if (i == 1) {
			if (next.equals("flower")) {
				if (DataHolder.is("flower", "fail")) {
					message = new Answer("��� � ������ ��� �� ���� �� � �������!", Color.YELLOW);
					DataHolder.dec("girl");
					add(new Answer("��� �����", Color.ORANGE));
					add(new Answer("������", Color.ORANGE));
					node--;
				} else {
					message = new Answer("�� ������ � ���� ������.", Color.YELLOW);
					DataHolder.inc("girl");
					add(new Answer("��� �� �������", Color.ORANGE));
					if (!DataHolder.hasKey("photo"))
						add(new Answer("�������, � ����� �����������", Color.ORANGE, () -> next = "nophoto"));
					if (!DataHolder.hasKey("ticket"))
						add(new Answer("�������, � ����� ������", Color.ORANGE, () -> next = "noticket"));
				}
			} else if (next.equals("sleep")) {
				message = new Answer("�� ������� ���� ������.", Color.YELLOW);
				add(new Answer("��� �� �������", Color.ORANGE));
				if (!DataHolder.hasKey("photo"))
					add(new Answer("������� � ����� �����������", Color.ORANGE, () -> next = "nophoto"));
				if (!DataHolder.hasKey("ticket"))
					add(new Answer("������� � ����� ������", Color.ORANGE, () -> next = "noticket"));
			} else {
				message = new Answer("�� ������...", Color.YELLOW);
				add(new Answer("��� �� �������", Color.ORANGE));
				if (!DataHolder.hasKey("photo"))
					add(new Answer("������� � ����� �����������", Color.ORANGE, () -> next = "nophoto"));
				if (!DataHolder.hasKey("ticket"))
					add(new Answer("������� � ����� ������", Color.ORANGE, () -> next = "noticket"));
			} 
			next="";
		}
		if (i == 2) {
			if (next.equals("noticket")) {
				DataHolder.set("girl","fail");
				message = new Answer("� ������ �� ���� �� �����? �����!", Color.YELLOW);
				add(new Answer("��� �����", Color.ORANGE));
				add(new Answer("������", Color.ORANGE));
			} else if (next.equals("nophoto")) {
				message = new Answer("� ������ �� ���� �� �����? �� ��� ���", Color.YELLOW);
				add(new Answer("��� �����", Color.ORANGE));
				add(new Answer("������", Color.ORANGE));
				add(new Answer("������, �������", Color.ORANGE, () -> DataHolder.inc("girl")));
			} else {
				message = new Answer("�� �� ����?", Color.YELLOW);
				if (!DataHolder.hasKey("photo"))
					add(new Answer("� ����� �����������", Color.ORANGE, () -> next = "nophoto"));
				if (!DataHolder.hasKey("ticket"))
					add(new Answer("� ����� ������", Color.ORANGE, () -> next = "noticket"));
				add(new Answer("�� ��", Color.ORANGE));
				node--;
			}
			next="";
		}
	}

}
