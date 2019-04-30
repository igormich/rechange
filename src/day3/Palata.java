package day3;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectStreamException;

import javax.imageio.ImageIO;

import base.Achivements;
import base.ActionZone;
import base.Character;
import base.DataHolder;
import base.MoveDir;
import base.Scene;

public class Palata extends Scene {

	private static final long serialVersionUID = -710684965058453170L;
	private transient BufferedImage back;
	private Character pank;
	private Character sherlock;
	private int stepPhase;

	public static void main(String[] args) throws Exception {
		Scene scene = new Palata();
		scene.init();
		scene.load();
		scene.display();
		scene.run();

	}

	@Override
	public void init() throws Exception {
		frezeePlayer = true;
		player = new Character("player_sick", 4, 900, 1200, this);
		player.step(MoveDir.RIGTH);
		if (DataHolder.hasKey("sherlock") && DataHolder.getInt("pills") == 0) {
			sherlock = new Character("sherlock", 3, 1100, 1200, this);
			sherlock.step(MoveDir.LEFT);
		} else {
			pank = new Character("pank", 3, 1100, 1200, this);
			pank.step(MoveDir.LEFT);
		}
	}

	@Override
	public void render(Graphics2D g, int x, int y, int cx, int cy) {
		AffineTransform baseTransform = g.getTransform();
		g.translate(x - cx, y - cy);
		g.drawImage(back, 0, 0, back.getWidth(null) * 4, back.getHeight(null) * 4, null);
		g.setTransform(baseTransform);
		g.translate(WIDTH / 2 - cx, HEIGHT / 2 - cy);
		player.draw(g);
		if (DataHolder.hasKey("sherlock") && DataHolder.getInt("pills") == 0) {
			g.setTransform(baseTransform);
			g.translate(x - cx, y - cy);
			g.translate(sherlock.getX(), sherlock.getY());
			sherlock.draw(g);
		} else {
			Composite old = g.getComposite();
			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) Math.max(0, 0.5));
			g.setComposite(ac);
			g.setTransform(baseTransform);
			g.translate(x - cx, y - cy);
			g.translate(pank.getX(), pank.getY());
			pank.draw(g);
			g.setComposite(old);
		}
	}

	@Override
	public void customTick(float tick) throws Exception {
		if (stepPhase == 0) {
			stepPhase = 1;
			if (DataHolder.hasKey("sherlock") && DataHolder.getInt("pills") == 0) {
				addMessage(sherlock.getX(), sherlock.getYHead(), "Ватсон-Ватсон, ну и угораздило вас.", 4, Scene.BROWN, () -> {
					addMessage(sherlock.getX(), sherlock.getYHead(), "Скорее садитесь в кэб", 4, Scene.BROWN, () -> {
						addMessage(sherlock.getX(), sherlock.getYHead(), "Мы едем на Бейкер стрит!", 4, Scene.BROWN, () -> {
						Achivements.addEnding("Мы едем на Бейкер стрит");
						addMessage(player.getX(), player.getYHead(), "Концовка: Мы едем на Бейкер стрит", 20, Color.WHITE, () -> {System.exit(0);});
						});
					});
				});
				
			} else {
				addMessage(pank.getX(), pank.getYHead(), "Здравствуй поросёночек", 4, Color.PINK, () -> {
					addMessage(pank.getX(), pank.getYHead(), "Ты теперь никуда от меня не денешься", 4, Color.PINK, () -> {
						Achivements.addEnding("Cумашествие");
						addMessage(player.getX(), player.getYHead(), "Концовка: сумашествие", 20, Color.WHITE, () -> {System.exit(0);});
					});
				});
			}
		}
	}

	@Override
	public void customAction(ActionZone action) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void load() throws IOException {
		back = ImageIO.read(new File("locations/end/palata.png"));
		moveMap = ImageIO.read(new File("locations/end/palata.png"));
	}

	@Override
	public void postRun() {
		try {

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
