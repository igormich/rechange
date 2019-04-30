package day1;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.io.ObjectStreamException;

import javax.imageio.ImageIO;

import base.ActionZone;
import base.Character;
import base.ElevatorDoors;
import base.Message;
import base.MoveDir;
import base.Scene;

public class Scene2 extends Scene {

	private static final long serialVersionUID = 7909471277741433471L;

	private transient Scene nextScene;
	private transient Image back;
	private transient Image door;
	private transient Image front1;
	private transient Image front2;
	private Character cop;
	private Character doc;
	private boolean door_open = false;
	private ElevatorDoors elevatorDoors;
	private int stepFase = 0;
	private boolean playerOnSofa = false;

	public static void main(String[] args) throws Exception {
		Scene scene = new Scene2();
		scene.init();
		scene.load();
		scene.display();
		scene.run();
	}

	@Override
	public void init() throws Exception {
		elevatorDoors = new ElevatorDoors(131 * 4, 227 * 4);
		player = new Character("player_sick_hand_back", 4, 690, 850, this);
		cop = new Character("cop1", 4, 590, 850, this);
		doc = new Character("med8", 4, 716 * 4, 184 * 4, this);
		doc.step(MoveDir.LEFT);
		frezeePlayer = true;
	}

	@Override
	public void render(Graphics2D g, int x, int y, int cx, int cy) {
		AffineTransform baseTransform = g.getTransform();
		g.translate(x - cx, y - cy);
		g.drawImage(back, 0, 0, back.getWidth(null) * 4, back.getHeight(null) * 4, null);
		if (player.getY() > elevatorDoors.getY())
			elevatorDoors.draw(g);
		if (player.getY() < 240 * 4) {
			g.setTransform(baseTransform);
			g.translate(x - cx, y - cy);
			g.translate(doc.getX(), doc.getY());
			doc.draw(g);
		}
		if (player.getY() > 228 * 4) {
			float alpha = 0.025f * (player.getY() - 228 * 4);
			if (alpha < 1) {
				Composite old = g.getComposite();
				AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
				g.setComposite(ac);
				g.drawImage(front2, 0, 0, front2.getWidth(null) * 4, front2.getHeight(null) * 4, null);
				g.setComposite(old);
			} else {
				g.drawImage(front2, 0, 0, front2.getWidth(null) * 4, front2.getHeight(null) * 4, null);
			}
			if (!door_open)
				g.drawImage(door, 0, 0, door.getWidth(null) * 4, door.getHeight(null) * 4, null);
		}
		if (cop.getY() < player.getY()) {
			g.setTransform(baseTransform);
			g.translate(x - cx, y - cy);
			g.translate(cop.getX(), cop.getY());
			cop.draw(g);
		}
		if (!playerOnSofa) {
			g.setTransform(baseTransform);
			g.translate(WIDTH / 2 - cx, HEIGHT / 2 - cy);
			player.draw(g);
		} else {
			g.setTransform(baseTransform);
			g.translate(x - cx, y - cy);
			g.translate(634 * 4, 128 * 4);
			g.rotate(-Math.PI / 2);
			player.draw(g);
		}

		if (player.getY() < 240 * 4) {
			g.setTransform(baseTransform);
			g.translate(x - cx, y - cy);
			g.drawImage(front1, 0, 0, front1.getWidth(null) * 4, front1.getHeight(null) * 4, null);
			if (!door_open)
				g.drawImage(door, 0, 0, door.getWidth(null) * 4, door.getHeight(null) * 4, null);
		}
		if (cop.getY() >= player.getY()) {
			g.setTransform(baseTransform);
			g.translate(x - cx, y - cy);
			g.translate(cop.getX(), cop.getY());
			cop.draw(g);
		}
		g.setTransform(baseTransform);
		g.translate(x - cx, y - cy);
		if (player.getY() < elevatorDoors.getY())
			elevatorDoors.draw(g);
	}

	@Override
	public void customTick(float tick) throws IOException {

		if ((lastKey != 0) && !haveMessages() && stepFase < 2) {
			Message message = CopAngry.nextMessage(cop);
			message.toScreen(true);
			addMessage(message);
		}
		if (stepFase == 0) {
			elevatorDoors.open(tick);
			if (elevatorDoors.isOpen()) {
				player.step(MoveDir.DOWN);
				cop.step(MoveDir.DOWN);
			}
			if (player.getY() > 1100)
				stepFase = 1;
		}
		if (stepFase == 1) {

			player.step(MoveDir.RIGTH);
			cop.step(MoveDir.RIGTH);
			if (player.getX() > 2450)
				stepFase = 2;
		}
		if (stepFase == 2) {
			player.step(MoveDir.UP);
			cop.step(MoveDir.UP);
			if (cop.getY() < 1000) {
				stepFase = 3;
				addMessage(cop.getX(), cop.getYHead(), "Доктор, к вам можно?", 4, LIGTH_BLUE, () -> {
					addMessage(doc.getX() - 200, doc.getYHead(), "Да, конечно. Что-то случилось?", 4, Color.GREEN);
					door_open = true;
					stepFase = 4;
				});
			}
		}
		if (stepFase == 4) {
			player.step(MoveDir.UP);
			cop.step(MoveDir.RIGTH);
			if (cop.getX() > 2450)
				stepFase = 5;
		}
		if (stepFase == 5) {
			player.step(MoveDir.UP);
			cop.step(MoveDir.UP);
			if (cop.getY() < 825) {
				player.step(MoveDir.RIGTH);
				cop.step(MoveDir.RIGTH);
				stepFase = 6;
				addMessage(cop.getX(), cop.getYHead(), "Этот тип утверждает, что ничего не помнит.", 4, LIGTH_BLUE, () -> {
					addMessage(doc.getX() - 200, doc.getYHead(), "Амнезия после травмы вполне возможна.", 4, Color.GREEN, () -> {
						addMessage(cop.getX(), cop.getYHead(), "Поговорите с ним, доктор, только...", 4, LIGTH_BLUE, () -> {
							addMessage(cop.getX(), cop.getYHead(), "Я лучше прикую его к кушетке.", 4, LIGTH_BLUE, () -> {
								addMessage(doc.getX() - 200, doc.getYHead(), "Учитывая обстоятельства, это разумно.", 4, Color.GREEN);
								stepFase = 7;
								player.step(MoveDir.RIGTH);
								player.baseFrame();
								playerOnSofa = true;
							});
						});
					});
				});
			}
		}
		if (stepFase == 7) {
			cop.step(MoveDir.DOWN);
			if (cop.getY() > 950)
				door_open = false;
			if (cop.getY() > 1000) {
				stepFase = 8;
				door_open = false;
				addMessage(doc.getX() - 200, doc.getYHead(), "Прошу извинить меня за эти неудобства.", 4, Color.GREEN, () -> {
					addMessage(doc.getX() - 200, doc.getYHead(), "Вы можете ответить на несколько вопросов?", 4, Color.GREEN, () -> {
						setDialog(new DocDialog1()).addPostAction(() -> {
							addMessage(doc.getX() - 200, doc.getYHead(), "И еще небольшой тест", 4, Color.GREEN, () -> {
								setDialog(new LuscherDialog()).addPostAction(() -> {
									addMessage(doc.getX() - 250, doc.getYHead(), "Пока я не могу сообщить вам результаты теста.", 4, Color.GREEN, () -> {
										addMessage(doc.getX() - 200, doc.getYHead(), "Могу только дать вам эти таблетки", 4, Color.GREEN, () -> {
											addMessage(doc.getX() - 200, doc.getYHead(), "Примите одну перед сном.", 4, Color.GREEN, () -> {
												addMessage(doc.getX() - 200, doc.getYHead(), "Эй! Отведи обратно его в палату.", 4, Color.GREEN, () -> {
													stepFase = 9;
													door_open = true;
												});
											});
										});
									});
								});
							});
						});
					});
				});
			}
		}
		if (stepFase == 9) {
			cop.step(MoveDir.UP);
			if (cop.getY() < 800) {
				playerOnSofa = false;
				stepFase = 10;
			}
		}
		if (stepFase == 10) {
			player.step(MoveDir.LEFT);
			if (player.getX() < 2350) {
				stepFase = 11;
				cop.step(MoveDir.LEFT);
			}
		}
		if (stepFase == 11) {
			player.step(MoveDir.DOWN);
			if (player.getY() > 860) {
				stepFase = 12;
				cop.step(MoveDir.DOWN);
			}
		}
		if (stepFase == 12) {
			player.step(MoveDir.RIGTH);
			if (player.getX() > 2450) {
				stepFase = 13;
			}
		}
		if (stepFase == 13) {
			cop.step(MoveDir.DOWN);
			player.step(MoveDir.DOWN);
			if (player.getY() > 1100)
				stepFase = 14;
		}
		if (stepFase == 13) {
			cop.step(MoveDir.DOWN);
			player.step(MoveDir.DOWN);
			if (player.getY() > 1100)
				stepFase = 14;
		}
		if (stepFase == 14) {
			cop.step(MoveDir.DOWN);
			player.step(MoveDir.LEFT);
			if (cop.getY() > 1100)
				stepFase = 15;
		}
		if (stepFase == 15) {
			cop.step(MoveDir.LEFT);
			player.step(MoveDir.LEFT);
			if (cop.getX() < 700)
				stepFase = 16;
		}
		if (stepFase == 16) {
			cop.step(MoveDir.UP);
			player.step(MoveDir.UP);
			if (cop.getY() < 900) {
				cop.step(MoveDir.DOWN);
				player.step(MoveDir.DOWN);
				stepFase = 17;

			}
		}
		if (stepFase == 17) {
			elevatorDoors.close(tick);
			if (elevatorDoors.isClosed())
				nextScene.run();
		}
	}

	@Override
	public void customAction(ActionZone action) throws Exception {

	}

	@Override
	public void load() throws IOException {
		back = ImageIO.read(new File("locations/scene2/back.png"));
		door = ImageIO.read(new File("locations/scene2/door.png"));
		front1 = ImageIO.read(new File("locations/scene2/front1.png"));
		front2 = ImageIO.read(new File("locations/scene2/front2.png"));
		moveMap = ImageIO.read(new File("locations/scene2/moveMap.png"));
	}

	@Override
	public void postRun() {
		nextScene = new Scene3();
		try {
			nextScene.init();
			nextScene.load();
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
