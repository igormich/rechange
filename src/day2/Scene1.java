package day2;

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
	protected Character advokat;
	protected boolean door_open = false;
	protected boolean dressed = false;
	protected int door_counter;
	protected ElevatorDoors elevatorDoors;

	private int stepfase;

	public static void main(String[] args) throws Exception {
		Scene scene = new Scene1();
		scene.load();
		scene.init();
		scene.display();
		scene.run();
	}

	@Override
	public void init() throws Exception {
		actions.add(new ActionZone(700, 550, "Одеться"));// тумбочка
		//actions.add(new ActionZone(200, 780, "Порыться"));// урна
		actions.add(new ActionZone(600, 800, "Постучать"));// дверь
		elevatorDoors = new ElevatorDoors(605 * 4, 227 * 4);
		player = new Character("player_sick_nude", 4, 550, 600, this);
		cop = new Character("cop0", 4, 500, 1000, this);
		advokat = new Character("advokat", 4, 2500, 1200, this);
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
		g.translate(advokat.getX(), advokat.getY());
		advokat.draw(g);

	}

	@Override
	public void customTick(float tick) throws IOException {
		if(stepfase==0){
			advokat.step(MoveDir.LEFT);
			if(advokat.getX()<550)
				stepfase = 1;
		}
		if(stepfase==1){
			advokat.step(MoveDir.UP);
			if(advokat.getY()<1100)
				stepfase = 2;
		}
		if(stepfase==2){
			stepfase = 3;
			actions.stream().filter(a -> a.getDescription().equals("Постучать")).forEach(a -> a.setDescription(""));
			addMessage(advokat.getX(),advokat.getYHead(),"Доброе утро.",4,Scene.LIGTH_BLUE1,() ->{
				addMessage(advokat.getX(),advokat.getYHead(),"Могу я увидеть своего подзащитного?",4,Scene.LIGTH_BLUE1,() ->{
					addMessage(cop.getX(),cop.getYHead(),"Да конечно, я отойду.",4,Scene.LIGTH_BLUE,() ->{
						addMessage(cop.getX(),cop.getYHead(),"И вы сможете пообщаться через дверь",4,Scene.LIGTH_BLUE,() ->{
							stepfase = 4;
						});	
					});
				});
			});
		}
		if(stepfase==4){
			cop.step(MoveDir.RIGTH);
			advokat.step(MoveDir.UP);
			if(cop.getX()>1500){
				stepfase = 5;
				actions.stream().filter(a -> a.getDescription().equals("")).forEach(a -> a.setDescription("Говорить"));
			}
		}
		if(stepfase==6){
			cop.step(MoveDir.LEFT);
			advokat.step(MoveDir.RIGTH);
			if(advokat.getX()>1500){
				stepfase = 7;
				cop.step(MoveDir.UP);
				addMessage(cop.getX(),cop.getYHead(),"Док хочет тебя видеть",4,Scene.LIGTH_BLUE,() ->{
					addMessage(cop.getX(),cop.getYHead(),"Давай я тебя быстро отведу",4,Scene.LIGTH_BLUE,() ->{
						nextScene.run();
					});
				});
			}
		}
	}

	@Override
	public void customAction(ActionZone action) throws Exception {
		if (action.getDescription().equals("Одеться")) {
			actions.remove(action);
			dressed = true;
			player = new Character("player_sick", 4, player.getX(), player.getY(), this);
		} else if (action.getDescription().equals("Порыться")) {
			addMessage(action.makeMessage("В мусорке пусто", Color.ORANGE));
		} else if (action.getDescription().equals("Постучать")) {
			switch (door_counter++) {
			case 0:
				addMessage(action.makeMessage("Чего тебе?", LIGTH_BLUE));
				break;
			case 1:
				addMessage(action.makeMessage("Скоро адвокат придёт с ним и поговоришь", LIGTH_BLUE));
				break;
			default:
			}
		} else if (action.getDescription().equals("Говорить")) {
			action.setDescription("");
			setDialog(new AdvokatDialog());
			stepfase = 6;
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
