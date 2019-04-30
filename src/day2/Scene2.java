package day2;

import java.awt.AlphaComposite;
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
		player = new Character("player_sick_hand_back", 4, 2500, 600, this);
		player.step(MoveDir.RIGTH);
		player.baseFrame();
		cop = new Character("cop1", 4, 2500, 1100, this);
		doc = new Character("med8", 4, 716 * 4, 184 * 4, this);
		doc.step(MoveDir.LEFT);
		frezeePlayer = true;
		playerOnSofa = true;
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
		if(stepFase == 0) {
			setDialog(new DocDialog2());
			stepFase = 1;
		}else if(stepFase == 1) {
			setDialog(new LuscherDialog());
			stepFase = 2;
		}else if(stepFase == 2) {
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
