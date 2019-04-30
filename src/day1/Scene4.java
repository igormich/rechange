package day1;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.io.ObjectStreamException;

import javax.imageio.ImageIO;

import base.ActionZone;
import base.Character;
import base.DataHolder;
import base.ElevatorDoors;
import base.Scene;

public class Scene4 extends Scene1 {

	Scene nextScene;
	public static void main(String[] args) throws Exception {
		Scene scene = new Scene4();
		scene.load();		
		scene.init();
		scene.display();
		scene.run();
	}
	private static final long serialVersionUID = -1337829637555604227L;
	private int stepFase = 0;
	private Character sherlock;
	private int doorCounter;
	private boolean hideSherlok;
	private double alpha;
	@Override
	public void init() throws Exception {
		elevatorDoors = new ElevatorDoors(605 * 4, 227 * 4);
		player = new Character("player_sick_nude", 4, 550, 600, this);
		sherlock = new Character("sherlock", 3, 200, 600, this);
		sherlock.baseFrame();
		cop = new Character("cop1", 4, 500, 960, this);
		actions.add(new ActionZone(200, 600, "Говорить"));
		actions.add(new ActionZone(600, 800, "Постучать"));// дверь
	}
	@Override
	public void render(Graphics2D g, int x, int y, int cx, int cy) {
		AffineTransform baseTransform = g.getTransform();
		g.translate(x - cx, y - cy);
		g.drawImage(back, 0, 0, back.getWidth(null) * 4, back.getHeight(null) * 4, null);
		
		g.setTransform(baseTransform);
		g.translate(x - cx, y - cy);
		g.translate(sherlock.getX(), sherlock.getY());
		Composite old = g.getComposite();
		
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) Math.max(0, alpha));
		g.setComposite(ac);
		
		sherlock.draw(g);
		g.setComposite(old);
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
		g.setColor(new Color(8, 16, 32, 64));
		g.fillRect(0, 0, WIDTH, HEIGHT);
	}
	@Override
	public void customTick(float tick) throws IOException {	
		if(hideSherlok){
			alpha-=tick/2;
		}
	}
	@Override
	public void customAction(ActionZone action) throws Exception {
		if (action.getDescription().equals("Говорить")) {
			//if(DataHolder.is("work","врач") && DataHolder.is("name", "Джон"))
			{
				addAchivement("Британский акцент");
			}
			setDialog(new SherlocDialog()).addPostAction(() -> nextScene.run());
		}
		if (action.getDescription().equals("Постучать")) {
			doorCounter++;
			if(doorCounter == 1)
				addMessage(sherlock.getX(), sherlock.getYHead()+100, "Тише", 4, Scene.BROWN);
			if(doorCounter == 2)
				addMessage(sherlock.getX(), sherlock.getYHead()+100, "Не шумите", 4, Scene.BROWN);
			if(doorCounter == 3) {
				addMessage(cop.getX(), cop.getYHead(), "Ты что расшумелся ночью!", 4, Scene.LIGTH_BLUE);
				hideSherlok=true;
				actions.add(new ActionZone(430, 550, "Лечь спать"));
				actions.stream().filter(a -> a.getDescription().equals("Говорить")).forEach(a -> a.setDescription(""));
				
			}
			if(doorCounter > 3) {
				addMessage(cop.getX(), cop.getYHead(), "Иди спи!", 4, Scene.LIGTH_BLUE);
			}
		}
		 if (action.getDescription().equals("Лечь спать")) {
			 addMessage(action.makeMessage("Вы засыпаете", Color.WHITE))
			 	.addPostAction(() -> nextScene.run());
			 action.setDescription("");
		 } 
	}
	@Override
	public void load() throws IOException {
		super.load();
		moveMap = ImageIO.read(new File("locations/scene1/moveMap1.png"));
	}
	@Override
	public void postRun() {
		nextScene = new day2.Scene1();
		if (DataHolder.getInt("pills")>0) {
			alpha = 0.7;
		} else {
			alpha = 0.9;
		}
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
