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

public class Scene1 extends Scene {

	private static final long serialVersionUID = 7909471277741433471L;

	private transient Scene nextScene;
	protected transient Image back;
	protected transient Image door;
	protected transient Image front1;
	protected transient Image front2;
	protected Character cop;
	protected boolean door_open = false;
	protected boolean dressed = false;
	protected int door_counter;
	protected ElevatorDoors elevatorDoors;

	public static void main(String[] args) throws Exception {
		Scene scene = new Scene1();
		scene.load();		
		scene.init();
		scene.display();
		scene.run();
	}

	@Override
	public void init() throws Exception {
		actions.add(new ActionZone(700, 550, "Осмотреть"));// тумбочка
		actions.add(new ActionZone(430, 550, "Лечь"));
		actions.add(new ActionZone(600, 350, "Выглянуть"));
		actions.add(new ActionZone(200, 780, "Порыться"));// урна
		actions.add(new ActionZone(600, 800, "Постучать"));// дверь
		elevatorDoors = new ElevatorDoors(605 * 4, 227 * 4);
		player = new Character("player_sick_nude", 4, 550, 600, this);
		cop = new Character("cop1", 4, 500, 960, this);
	}

	@Override
	public void render(Graphics2D g, int x, int y, int cx, int cy) {
		AffineTransform baseTransform = g.getTransform();
		g.translate(x - cx, y - cy);
		g.drawImage(back, 0, 0, back.getWidth(null) * 4, back.getHeight(null) * 4, null);
		if (player.getY() > elevatorDoors.getY())
			elevatorDoors.draw(g);
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

		g.setTransform(baseTransform);
		g.translate(WIDTH / 2 - cx, HEIGHT / 2 - cy);

		player.draw(g);

		g.setTransform(baseTransform);
		g.translate(x - cx, y - cy);
		if (player.getY() < 240 * 4) {
			g.drawImage(front1, 0, 0, front1.getWidth(null) * 4, front1.getHeight(null) * 4, null);
			if (!door_open)
				g.drawImage(door, 0, 0, front1.getWidth(null) * 4, front1.getHeight(null) * 4, null);
		}
		g.translate(cop.getX(), cop.getY());
		cop.draw(g);
		g.setTransform(baseTransform);
		g.translate(x - cx, y - cy);
		if (player.getY() < elevatorDoors.getY())
			elevatorDoors.draw(g);

	}

	@Override
	public void customTick(float tick) throws IOException {
		if ((player.getY() > 235 * 4) && !frezeePlayer) {
			frezeePlayer = true;
			player = new Character("player_sick_hand_back", 4, player.getX(), player.getY(), this);
			addMessage(cop.getX(), cop.getYHead(), "Руки за спину и пошли.", 4, LIGTH_BLUE);
		}
		if (frezeePlayer) {
			if ((lastKey != 0) && !haveMessages()) {
				Message message = CopAngry.nextMessage(cop);
				message.toScreen(true);
				addMessage(message);
			}
			if (player.getX() >= 2450) {
				if ((player.getY() < 240 * 4) && (player.getY() > 220 * 4))
					elevatorDoors.open(tick);
				if (player.getY() < 220 * 4)
					elevatorDoors.close(tick);
				if (player.getY() >= 240 * 4) {
					player.step(MoveDir.UP);
					cop.step(MoveDir.UP);
				} else if (player.getX() <= 2560) {
					player.step(MoveDir.RIGTH);
					cop.step(MoveDir.RIGTH);
				} else if ((elevatorDoors.isOpen()) && (player.getX() >= 2560)) {
					player.step(MoveDir.UP);
					cop.step(MoveDir.UP);
				}
				if ((player.getX() >= 2560) && (elevatorDoors.isClosed())) {
					nextScene.run();
				}
			} else {
				if (player.getY() <= 260 * 4) {
					player.step(MoveDir.DOWN);
				}
				if ((player.getY() > 240 * 4) && (player.getY() <= 260 * 4)) {
					cop.step(MoveDir.DOWN);
				}
				if (player.getY() > 260 * 4) {
					cop.step(MoveDir.RIGTH);
					player.step(MoveDir.RIGTH);
				}
			}
		}

	}

	@Override
	public void customAction(ActionZone action) throws Exception {
		{
			if (action.getDescription().equals("Лечь")) {
				addMessage(action.makeMessage("Я не хочу спать", Color.ORANGE));
			} else if (action.getDescription().equals("Выглянуть")) {
				addMessage(action.makeMessage("За окном темно и ничего не видно", Color.ORANGE));
			} else if (action.getDescription().equals("Осмотреть")) {
				addMessage(action.makeMessage("В тумбочке лежит больничная одежда", Color.ORANGE));
				action.setDescription("Одеться");
			} else if (action.getDescription().equals("Одеться")) {
				actions.remove(action);
				dressed = true;
				player = new Character("player_sick", 4, player.getX(), player.getY(), this);
			} else if (action.getDescription().equals("Порыться")) {
				addMessage(action.makeMessage("В мусорке пусто", Color.ORANGE));
			} else if (action.getDescription().equals("Постучать")) {
				switch (door_counter++) {
				case 0:
					addMessage(action.makeMessage("По голове себе постучи.", LIGTH_BLUE));
					break;
				case 1:
					addMessage(action.makeMessage("Мне запрещенно с тобой разговаривать.", LIGTH_BLUE));
					action.setDescription("Почему я заперт?");
					action.setColor(Color.ORANGE);
					break;
				default:
					if (dressed) {
						actions.remove(action);
						moveColor.add(0xff0000);
						door_open = true;
					} else {
						if (door_counter == 2)
							addMessage(action.makeMessage("Сперва оденься.", LIGTH_BLUE));
						else if (door_counter == 3)
							addMessage(action.makeMessage("Одежда в тумбочке у окна.", LIGTH_BLUE));
						else if (door_counter == 4)
							addMessage(action.makeMessage("Ты что совсем тупой?", LIGTH_BLUE));
						else
							addMessage(action.makeMessage("Ну и сиди тут, если одеваться не хочешь.", LIGTH_BLUE));
					}
					break;
				}
			} else if (action.getDescription().equals("Почему я заперт?")) {
				addMessage(action.makeMessage("Ты что ничего не помнишь?", LIGTH_BLUE));
				action.setDescription("Даже имя не помню.");
			} else if (action.getDescription().equals("Даже имя не помню.")) {
				Message message = addMessage(action.makeMessage("Допустим я тебе поверю и отведу к врачу.", LIGTH_BLUE));
				if (dressed) {
					actions.remove(action);
					moveColor.add(0xff0000);
					door_open = true;
				} else {
					action.setDescription("");
					message.addPostAction(() -> {
						addMessage(action.makeMessage("Только сперва оденься, одежда лежит в тумбочке.", LIGTH_BLUE));
						action.setColor(Color.WHITE);
						action.setDescription("Постучать");
					});
				}
			}
		}
	}

	@Override
	public void load() throws IOException {
		back = ImageIO.read(new File("locations/scene1/palata.png"));
		door = ImageIO.read(new File("locations/scene1/door.png"));
		front1 = ImageIO.read(new File("locations/scene1/front1.png"));
		front2 = ImageIO.read(new File("locations/scene1/front2.png"));
		moveMap = ImageIO.read(new File("locations/scene1/moveMap.png"));
	}

	@Override
	public void postRun() {
		nextScene = new Scene2();
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
