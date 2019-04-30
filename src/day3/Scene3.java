package day3;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.util.List;

import javax.imageio.ImageIO;

import base.Achivements;
import base.ActionZone;
import base.Character;
import base.DataHolder;
import base.MoveDir;
import base.Scene;
import day2.Sleep3;

public class Scene3 extends Scene {

	private static final long serialVersionUID = -710684965058453170L;
	private transient BufferedImage back;
	private transient BufferedImage front1;
	private Character sud0;
	private Character sud1;
	private Character sud2;
	private int stepPhase;
	private Character advokat;
	private int alibi;
	private Palata palata;

	public static void main(String[] args) throws Exception {
		Scene3 scene = new Scene3();
		scene.init();
		scene.load();
		scene.display();
		scene.run();
	}

	@Override
	public void init() throws Exception {
		frezeePlayer = true;
		player = new Character("player_sick", 4, 100, 900, this);
		player.step(MoveDir.RIGTH);

		advokat = new Character("advokat", 4, 200, 1100, this);
		advokat.step(MoveDir.RIGTH);
		sud0 = new Character("sud1", 4, 710, 720, this);
		sud0.step(MoveDir.DOWN);

		sud1 = new Character("sud2", 4, 590, 730, this);
		sud1.step(MoveDir.DOWN);

		sud2 = new Character("sud2", 4, 840, 730, this);
		sud2.step(MoveDir.DOWN);
	}

	@Override
	public void render(Graphics2D g, int x, int y, int cx, int cy) {
		AffineTransform baseTransform = g.getTransform();
		g.translate(x - cx, y - cy);
		g.drawImage(back, 0, 0, back.getWidth(null) * 4, back.getHeight(null) * 4, null);
		g.setTransform(baseTransform);
		g.translate(WIDTH / 2 - cx, HEIGHT / 2 - cy);
		player.draw(g);

		g.setTransform(baseTransform);
		g.translate(x - cx, y - cy);
		g.translate(sud0.getX(), sud0.getY());
		sud0.draw(g);

		g.setTransform(baseTransform);
		g.translate(x - cx, y - cy);
		g.translate(sud1.getX(), sud1.getY());
		sud1.draw(g);

		g.setTransform(baseTransform);
		g.translate(x - cx, y - cy);
		g.translate(sud2.getX(), sud2.getY());
		sud2.draw(g);

		g.setTransform(baseTransform);
		g.translate(x - cx, y - cy);
		g.drawImage(front1, 0, 0, front1.getWidth(null) * 4, front1.getHeight(null) * 4, null);

		g.setTransform(baseTransform);
		g.translate(x - cx, y - cy);
		g.translate(advokat.getX(), advokat.getY());
		advokat.draw(g);

	}

	@Override
	public void customTick(float tick) throws Exception {
		
		if (stepPhase == 0) {
			stepPhase = 1;
			addMessage(sud0.getX(), sud0.getY(), "�������� ������ ��������� ����", 4, Color.LIGHT_GRAY, () -> {
				addMessage(sud0.getX(), sud0.getY(), "�� ���� � ����� �������", 4, Color.LIGHT_GRAY, () -> {
					addMessage(sud0.getX(), sud0.getY(), "���������� " + DataHolder.getString("name", "������"), 4, Color.LIGHT_GRAY, () -> {
						addMessage(sud0.getX(), sud0.getY(), "�������� ���������� ������� �������", 4, Color.LIGHT_GRAY, () -> {
							addMessage(sud0.getX(), sud0.getY(), "��� ���������� ����� ���� ��������� ������", 4, Color.LIGHT_GRAY, () -> {
								addMessage(advokat.getX() + 200, advokat.getY(), "���� �����, � ����� ������������ �������", 4, Scene.LIGTH_BLUE1, () -> {
									addMessage(sud0.getX(), sud0.getY(), "��� ���������� ���.", 4, Color.LIGHT_GRAY, () -> {
										addMessage(sud0.getX(), sud0.getY(), "��� �� ������ ������� � ������", 4, Color.LIGHT_GRAY, () -> {
											stepPhase = 2;
										});
									});
								});
							});
						});
					});
				});
			});
		} else if (stepPhase == 2) {
			stepPhase = 3;
			if (DataHolder.is("girl", "2") || DataHolder.is("girl", "home")) {
				addMessage(advokat.getX() + 200, advokat.getY(), "���� ��������� ��� �������", 4, Scene.LIGTH_BLUE1, () -> {
					addMessage(advokat.getX() + 200, advokat.getY(), "��� ���� � �� ���� �� ����� ������� ��������", 4, Scene.LIGTH_BLUE1, () -> {
						addMessage(sud0.getX(), sud0.getY(), "������, ��� ����� ���������� ������������", 4, Color.LIGHT_GRAY, () -> {
							addMessage(sud0.getX(), sud0.getY(), "������, ��� ����� ���� �����", 4, Color.LIGHT_GRAY, () -> {
								addMessage(sud0.getX(), sud0.getY(), "��� ��� ������?", 4, Color.LIGHT_GRAY, () -> {
									stepPhase = 4;
									alibi += 1;
								});
							});
						});
					});
				});
			} else {
				stepPhase = 4;
			}
		} else if (stepPhase == 4) {
			stepPhase = 5;
			if (DataHolder.is("second", "medic")) {
				addMessage(advokat.getX() + 200, advokat.getY(), "�� ����� ������� �������� " + DataHolder.getString("name", "������"), 4, Scene.LIGTH_BLUE1, () -> {
					addMessage(advokat.getX() + 200, advokat.getY(), "��� � ��������.", 4, Scene.LIGTH_BLUE1, () -> {
						addMessage(advokat.getX() + 200, advokat.getY(), "��� ������� �� �������", 4, Scene.LIGTH_BLUE1, () -> {
							addMessage(sud0.getX(), sud0.getY(), "�� ��� �� �������� ������������ �����", 4, Color.LIGHT_GRAY, () -> {
								stepPhase = 6;
								alibi += 2;
							});
						});
					});
				});
			} else if (DataHolder.is("second", "arest")) {
				addMessage(advokat.getX() + 200, advokat.getY(), "�� ����� ������� �������� " + DataHolder.getString("name", "������"), 4, Scene.LIGTH_BLUE1, () -> {
					addMessage(advokat.getX() + 200, advokat.getY(), "��� � �������, �������� �� �����", 4, Scene.LIGTH_BLUE1, () -> {
						addMessage(advokat.getX() + 200, advokat.getY(), "��� ������� �� �������", 4, Scene.LIGTH_BLUE1, () -> {
							addMessage(sud0.getX(), sud0.getY(), "�� ��� �� �������� ������������ �����", 4, Color.LIGHT_GRAY, () -> {
								stepPhase = 6;
								alibi += 2;
							});
						});
					});
				});
			} else {
				stepPhase = 6;
			}
		} else if (stepPhase == 6) {
			stepPhase = 7;
			if (alibi < 2)
				addMessage(advokat.getX() + 200, advokat.getY(), "� ��������� ������ ���� �����", 4, Scene.LIGTH_BLUE1, () -> {
					stepPhase = 8;
				});
			else
				stepPhase = 8;
		} else if (stepPhase == 8) {
			stepPhase = 9;
			if ((alibi < 3) && (DataHolder.getInt("pills") < 2)) {
				addMessage(sud0.getX(), sud0.getY(), "��������������� ���������� ����:", 4, Color.LIGHT_GRAY, () -> {
					addMessage(sud0.getX(), sud0.getY(), "����������� �������� ��������", 4, Color.LIGHT_GRAY, () -> {
						addMessage(sud0.getX(), sud0.getY(), "� �������� ��� ����������� ���������", 4, Color.LIGHT_GRAY, () -> {
							addMessage(sud0.getX(), sud0.getY(), "��������� � ���������� ���", 4, Color.LIGHT_GRAY, () -> {
								addMessage(sud0.getX(), sud0.getY(), "�� ������ ����������� ����", 4, Color.LIGHT_GRAY, () -> {
									palata.run();
								});
							});
						});
					});
				});
			} else if (alibi == 0)
				addMessage(sud0.getX(), sud0.getY(), "��������������� ���������� ����:", 4, Color.LIGHT_GRAY, () -> {
					addMessage(sud0.getX(), sud0.getY(), "����������� �������� ��������", 4, Color.LIGHT_GRAY, () -> {
						addMessage(sud0.getX(), sud0.getY(), "� ��������� ��� ������", 4, Color.LIGHT_GRAY, () -> {
							addMessage(sud0.getX(), sud0.getY(), "�� ������ ����������� ����", 4, Color.LIGHT_GRAY, () -> {
								Achivements.addEnding("���������");
								addMessage(advokat.getX() + 400, player.getY(), "��������: ���������", 20, Color.WHITE, () -> {System.exit(0);});
							});
						});
					});
				});
			else if (alibi < 3) {
				addMessage(sud0.getX(), sud0.getY(), "��������������� ���������� ����:", 4, Color.LIGHT_GRAY, () -> {
					addMessage(sud0.getX(), sud0.getY(), "����������� �������� ��� �����������", 4, Color.LIGHT_GRAY, () -> {
						addMessage(sud0.getX(), sud0.getY(), "� ��������� ��� �������� � ��������", 4, Color.LIGHT_GRAY, () -> {
							Achivements.addEnding("��� ����������");
							addMessage(advokat.getX() + 400, player.getY(), "��������: ��� ����������", 10000, Color.WHITE, () -> {System.exit(0);});
						});
					});
				});
			} else if (alibi == 3)
				addMessage(sud0.getX(), sud0.getY(), "��������������� ���������� ����:", 4, Color.LIGHT_GRAY, () -> {
					addMessage(sud0.getX(), sud0.getY(), "����������� �������� ����������", 4, Color.LIGHT_GRAY, () -> {
						addMessage(sud0.getX(), sud0.getY(), "� ���������� � ���� ����", 4, Color.LIGHT_GRAY, () -> {	
							if(DataHolder.getInt("kills") >=  2) {
								Achivements.addEnding("��� ������ ����� �������");
								addMessage(advokat.getX() + 400, player.getY(), "��������: ��� ������ ����� �������", 20, Color.WHITE, () -> {System.exit(0);});
							} else {
								Achivements.addEnding("�������");
								addMessage(advokat.getX() + 400, player.getY(), "��������: �������", 20, Color.WHITE, () -> {System.exit(0);});	
							}
						});
					});
				});
		}
	}

	@Override
	public void customAction(ActionZone action) throws Exception {

	}

	@Override
	public void load() throws IOException {
		back = ImageIO.read(new File("locations/sud/sud.png"));
		front1 = ImageIO.read(new File("locations/sud/front1.png"));
		moveMap = ImageIO.read(new File("locations/sud/moveMap.png"));
	}

	@Override
	public void postRun() {
		palata = new Palata();
		try {
			palata.load();
			palata.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Object readResolve() throws ObjectStreamException {
		try {
			load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}
}
