package day1;

import java.awt.Color;

import base.Answer;
import base.DataHolder;
import base.SimpleDialog;

public class GirlDialog2 extends SimpleDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 262327703397020182L;
	private String next = "";

	@Override
	protected void next(int i) {
		if (i == 0) {
			if (!DataHolder.hasKey("ticket")) {
				DataHolder.set("girl","fail");
				message = new Answer("��������! ��� �� ��� ������ ������", Color.YELLOW);
				add(new Answer("������", Color.ORANGE));
				add(new Answer("������", Color.ORANGE));
				add(new Answer("� ����������", Color.ORANGE));
			} else if (DataHolder.getInt("girl") == 2) {
					message = new Answer("�� ����� �����! ������ �� ��� ������ ���?", Color.YELLOW);
					add(new Answer("�������", Color.ORANGE,() ->DataHolder.set("girl", "home")));
					add(new Answer("������ ������� ������", Color.ORANGE,() ->DataHolder.set("girl", "friend")));
					add(new Answer("������ ���� ������ ����", Color.ORANGE,() ->DataHolder.set("girl", "friend")));
			} else {
				message = new Answer("�� ����� �����! ��������� ����", Color.YELLOW);
				add(new Answer("�� � �����?", Color.ORANGE));
				add(new Answer("����� � ���� � �����?", Color.ORANGE));
				add(new Answer("����� ������ ���?", Color.ORANGE));
			}
		}
	}

}
