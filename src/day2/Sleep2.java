package day2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import base.ActionZone;
import base.Character;
import base.DataHolder;
import base.MoveDir;
import base.Scene;

public class Sleep2 extends Scene {

	private static final long serialVersionUID = -710684965058453170L;
	private static final int R_BORDER = 1470;
	private transient Image back;
	private transient BufferedImage front1;
	private transient BufferedImage front2;
	private Character prodavec;
	private Character pank;
	private List<Character> humans = new ArrayList<>();
	private Character sherlock;
	private Character bich;
	private int stepFase;
	private Object pankMessage;
	private int panckAgree;
	private transient BufferedImage smoke;
	private boolean figth;
	private int fx;
	private int fy;
	private double angle;
	private boolean playerWin;
	private boolean postFigth;
	private Scene nextScene1;
	private Scene nextScene2;
	private Character cop2;
	private Character cop1;
	private Character medic1;
	private Character medic2;

	public static void main(String[] args) throws Exception {
		Scene scene = new Sleep2();
		scene.init();
		scene.load();
		scene.display();
		scene.run();
	}

	@Override
	public void init() throws Exception {
		player = new Character("hero", 3, 350, 700, this);
		prodavec = new Character("prodavec", 3, 1350, 725, this);
		prodavec.step(MoveDir.UP);
		sherlock = new Character("sherlock", 3, 1400, 550, this);
		sherlock.step(MoveDir.RIGTH);
		humans.add(sherlock);
		bich = new Character("bich", 3, 1250, 550, this);
		bich.step(MoveDir.RIGTH);
		humans.add(bich);
		cop1 = new Character("scop1", 3, 1450, 530, this);
		humans.add(cop1);
		pank = new Character("pank", 3, 1100, 550, this);
		pank.step(MoveDir.RIGTH);
		humans.add(pank);
		cop2 = new Character("scop2", 3, 1450, 570, this);
		humans.add(cop2);
		medic1 = new Character("smedic1", 3, 1450, 530, this);
		humans.add(medic1);
		medic2 = new Character("smedic2", 3, 1450, 570, this);
		humans.add(medic2);
	}

	@Override
	public void render(Graphics2D g, int x, int y, int cx, int cy) {
		AffineTransform baseTransform = g.getTransform();
		g.translate(x - cx, y - cy);
		g.drawImage(back, 0, 0, back.getWidth(null) * 4, back.getHeight(null) * 4, null);

		g.setTransform(baseTransform);
		g.translate(WIDTH / 2 - cx, HEIGHT / 2 - cy);
		if (!figth && !postFigth)
			player.draw(g);

		for (Character character : humans) {
			if (character.getX() > R_BORDER)
				continue;
			if (figth || postFigth && character == pank)
				continue;
			g.setTransform(baseTransform);
			g.translate(x - cx, y - cy);
			g.translate(character.getX(), character.getY());
			character.draw(g);
		}
		if (figth) {
			g.setTransform(baseTransform);
			g.translate(x - cx, y - cy);
			g.translate(fx, fy);

			AffineTransform transform = g.getTransform();
			g.rotate(angle);
			g.translate(-20 * Math.cos(angle) - 30, 50);
			player.draw(g);
			g.setTransform(transform);
			g.rotate(angle);
			g.translate(20 * Math.sin(angle) + 30, 50);
			pank.draw(g);
			g.setTransform(transform);
			g.rotate(angle);
			g.drawImage(smoke, -200, -200, 400, 400, null);
			g.rotate(-angle);
			g.drawImage(smoke, -200, -200, 400, 400, null);
		}
		if (postFigth) {
			g.setTransform(baseTransform);
			g.translate(x - cx, y - cy);
			g.translate(fx, fy);
			AffineTransform transform = g.getTransform();
			if (playerWin) {
				g.translate(-50, 50);
				player.draw(g);
				g.setTransform(transform);
				g.rotate(Math.PI / 2);
				g.translate(50, 50);
				pank.draw(g, false);
			} else {
				g.translate(-50, 50);
				g.rotate(-Math.PI / 2);
				player.draw(g, false);
				g.setTransform(transform);
				g.translate(50, 50);
				pank.draw(g);
			}

		}

		g.setTransform(baseTransform);
		g.translate(x - cx, y - cy);
		g.drawImage(front1, 0, 0, front1.getWidth(null) * 4, front1.getHeight(null) * 4, null);

		g.setTransform(baseTransform);
		g.translate(x - cx, y - cy);
		g.translate(prodavec.getX(), prodavec.getY());
		prodavec.draw(g);

		g.setTransform(baseTransform);
		g.translate(x - cx, y - cy);
		g.drawImage(front2, 0, 0, front2.getWidth(null) * 4, front2.getHeight(null) * 4, null);
	}

	@Override
	public void customTick(float tick) throws Exception {
		if (stepFase == 0) {
			setDialog(new Sleep2Dialog());
			stepFase = 1;
		}
		if (stepFase == 1) {
			if(DataHolder.hasKey("axe") && DataHolder.is("work","Студент") && DataHolder.is("name","Родион")){
				addAchivement("Раскольников");
			}
			stepFase = 2;
			addMessage(sherlock.getX(), sherlock.getY(), "", 2, Scene.BROWN, () -> {
				stepFase = 3;
			});
		}
		if (stepFase == 3) {
			for (Character character : humans) {
				if (character.getX() <= R_BORDER) {
					character.setX(character.getX() + 1);
					character.step(MoveDir.RIGTH);
				}
			}
			if (bich.getX() > 1350) {
				bich.step(MoveDir.DOWN);
				stepFase = 4;
				addMessage(bich.getX(), bich.getY(), "Самое дешевое курево", 4, Scene.DYELOOW, () -> {
					//if (!figth)
						addMessage(bich.getX(), bich.getY(), "Такое подойдёт?", 4, Color.GREEN, () -> {
							//if (!figth)
								addMessage(bich.getX(), bich.getY(), "А дешевле нет?", 4, Scene.DYELOOW, () -> {
									//if (!figth)
										addMessage(bich.getX(), bich.getY(), "Куда уж дешевле", 4, Color.GREEN, () -> {
											//if (!figth)
												addMessage(bich.getX(), bich.getY(), "Ну хорошо давай", 4, Scene.DYELOOW, () -> {
													//if (!figth)
														stepFase = 5;
												});
										});
								});
						});
				});
			}
		}
		if (stepFase == 5) {
			for (Character character : humans) {
				if (character.getX() <= R_BORDER) {
					character.setX(character.getX() + 1);
					character.step(MoveDir.RIGTH);
				}
			}
			if (pank.getX() > 1350) {
				stepFase = 6;
				moveColor.add(0xff0000);
				addMessage(pank.getX(), pank.getY(), "У меня тут два пакета молока.", 4, Color.PINK, () -> {
					if (!figth)
						addMessage(pank.getX(), pank.getY(), "То есть 150 и 150?.", 4, Color.GREEN, () -> {
							if (!figth)
								addMessage(pank.getX(), pank.getY(), "В сумме 300?.", 4, Color.GREEN, () -> {
									if (!figth)
										addMessage(pank.getX(), pank.getY(), "Hasta la vista.", 4, Color.PINK, () -> {
											if (!figth)
												addMessage(pank.getX(), pank.getY(), "И вам до свидания.", 4, Color.GREEN, () -> {
													stepFase = 7;
												});
										});
								});
						});
				});
			}
		}
		if ((stepFase == 4) || (stepFase == 5) || (stepFase == 6)) {
			if ((pank.distanceTo(player) < 150) && (pankMessage == null)) {
				panckAgree++;
				if (panckAgree == 1)
					pankMessage = addMessage(pank.getX(), pank.getYHead(), "Что ты ко мне жмёшься?", 5, Color.PINK, () -> {
						pankMessage = null;
					});
				if (panckAgree == 2)
					pankMessage = addMessage(pank.getX(), pank.getYHead(), "Дурак что ли?", 5, Color.PINK, () -> {
						pankMessage = null;
					});
				if (panckAgree == 3)
					pankMessage = addMessage(pank.getX(), pank.getYHead(), "Хорош пристраиваться!", 5, Color.PINK, () -> {
						pankMessage = null;
					});
				if (panckAgree == 4)
					pankMessage = addMessage(pank.getX(), pank.getYHead(), "Сейчас огребешь!", 5, Color.PINK, () -> {
						pankMessage = null;
					});
				if (panckAgree == 5) {
					stepFase = 100;
					player.step(MoveDir.RIGTH);
					addMessage(bich.getX(), bich.getY(), "ОЙ!", 4, Scene.DYELOOW);
					addMessage(pank.getX(), pank.getY(), "", 4, Color.GREEN, () -> {
						addMessage(pank.getX(), pank.getY(), "Я вызываю полицию!", 4, Color.GREEN, () ->{
							addMessage(pank.getX(), pank.getY(), "И скорую!", 4, Color.GREEN);
						});
					});
					figth = true;
					frezeePlayer = true;
					pank.step(MoveDir.LEFT);
					bich.setX(R_BORDER + 1);
					fx = player.getX() / 2 + pank.getX() / 2;
					fy = player.getYHead() / 2 + pank.getYHead() / 2;
					moveColor.add(0xff0000);
					moveColor.add(0x00ff00);
				}
			}
		}
		if (stepFase == 7) {
			for (Character character : humans) {
				if (character.getX() <= R_BORDER) {
					character.setX(character.getX() + 1);
					character.step(MoveDir.RIGTH);
				}
			}
			if (pank.getX() > R_BORDER) {
				stepFase = 8;
				moveColor.add(0x00ff00);
			}
		}
		if (figth)
			angle += tick * 2;
		if (angle > Math.PI * 3) {
			figth = false;
			postFigth = true;
			playerWin = DataHolder.is("work1", "охранник") || DataHolder.is("work2", "охранник");
		}
		if (player.getX() >= R_BORDER) {
			nextScene1.run();
		}
		if (postFigth && playerWin) {
			cop1.step(MoveDir.LEFT);
			cop2.step(MoveDir.LEFT);
			if (cop1.getX() < 1350) {
				DataHolder.set("second", "arest");
				addMessage(cop1.getX(), cop1.getYHead(), "Вы задержаны!", 5, Scene.LIGTH_BLUE, () -> nextScene1.run());
			}
		}
		if (postFigth && !playerWin) {
			pank.step(MoveDir.RIGTH);
			if (pank.getX() > R_BORDER) {
				stepFase = 9;
			}
		}
		if (stepFase == 9) {
			medic1.step(MoveDir.LEFT);
			medic2.step(MoveDir.LEFT);
			if (medic1.getX() < 1350) {
				DataHolder.set("second", "medic");
				nextScene2.run();
			}
		}
	}

	@Override
	public void customAction(ActionZone action) throws Exception {

	}

	@Override
	public void load() throws IOException {
		back = ImageIO.read(new File("locations/sleep2/back.png"));
		front1 = ImageIO.read(new File("locations/sleep2/front1.png"));
		front2 = ImageIO.read(new File("locations/sleep2/front2.png"));
		moveMap = ImageIO.read(new File("locations/sleep2/moveMap.png"));
		smoke = ImageIO.read(new File("characters/smoke.png"));
	}

	@Override
	public void postRun() {
		nextScene1 = new day3.Scene3();
		nextScene2 = new Sleep3();
		try {
			nextScene1.init();
			nextScene1.load();
			nextScene2.init();
			nextScene2.load();
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
