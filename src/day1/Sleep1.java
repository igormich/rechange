package day1;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectStreamException;

import javax.imageio.ImageIO;

import base.ActionZone;
import base.Character;
import base.DataHolder;
import base.MoveDir;
import base.Scene;

public class Sleep1 extends Scene{

	private static Scene4 nextScene;

	public static void main(String[] args) throws Exception {
		Scene scene = new Sleep1();
		scene.load();		
		scene.init();
		scene.display();
		scene.run();
	}
	private static final long serialVersionUID = 992016467090546094L;
	private transient BufferedImage street;
	private Character girl;
	private int girlDir;
	private int stepFase = -1;
	private float decay;
	private boolean noMessage =true;
	

	@Override
	public void init() throws Exception {
		player = new Character("hero", 3, 250, 1200, this);
		girl = new Character("girl", 3, 4100, 1400, this);
		actions.add(new ActionZone(250, 1100, "Это мой дом"));
		actions.add(new ActionZone(160, 1400, "Её дом в другой стороне"));
		actions.add(new ActionZone(1290, 1100, "Нарвать цветов"));
		actions.add(new ActionZone(1600, 1100, "Нарвать цветов"));
		actions.add(new ActionZone(2800, 1100, "Нарвать цветов "));
		actions.add(new ActionZone(3000, 1100, "Нарвать цветов "));	
	}

	@Override
	public void render(Graphics2D g, int x, int y, int cx, int cy) {
		AffineTransform baseTransform = g.getTransform();
		g.translate(x - cx, y - cy);
		g.drawImage(street, 0, 0, street.getWidth(null) * 4, street.getHeight(null) * 4, null);
		g.setTransform(baseTransform);
		g.translate(x - cx, y - cy);
		g.translate(girl.getX(), girl.getY());
		if((girl.getX()<4350)&&(girl.getY()>1200))
			girl.draw(g);
		g.setTransform(baseTransform);
		g.translate(WIDTH / 2 - cx, HEIGHT / 2 - cy);
		if((player.getX()<4350)&&((girl.getY()>1200)||!DataHolder.is("girl","home")))
			player.draw(g);

		g.setTransform(baseTransform);
		g.setColor(new Color(8, 16, 32, (int)decay));
		g.fillRect(0, 0, WIDTH, HEIGHT);
	}

	@Override
	public void customTick(float tick) throws Exception {
		if(stepFase == -1) {
			setDialog(new Sleep1Dialog());
			stepFase = 0;
		}
		if(stepFase == 0) {
		if(player.getX()+100>girl.getX()){
			setDialog(new GirlDialog1());
			frezeePlayer = true;
			stepFase = 1;
		} 
		} else if(stepFase == 1) {
			if(DataHolder.is("girl","fail")){
				addMessage(girl.getX(), girl.getYHead(), "Видеть тебя не хочу!", 4, Color.YELLOW);
				girl.step(MoveDir.UP);
				if(girl.getY()<1200) {
					frezeePlayer = false;
					stepFase = 10;
					actions.stream().filter(a -> a.getDescription().equals("Это мой дом")).forEach(a -> a.setDescription("Вернуться домой"));
					actions.stream().filter(a -> a.getDescription().equals("Её дом в другой стороне")).forEach(a -> a.setDescription("Пойти кого нибудь избить"));
				}		
			} else {
				girl.step(MoveDir.RIGTH);
				player.step(MoveDir.RIGTH);
				if(player.getX()>4350) {
					player.setY(1500);
					stepFase = 2;
				}
			}
		} else  if(stepFase == 2) {
			decay+=tick*20;
			if(decay>128)
				stepFase = 3;
		} else if(stepFase == 3) {
			girl.step(MoveDir.LEFT);
			player.step(MoveDir.LEFT);
			if(player.getX()<4050) {
				stepFase = 4;
				girl.step(MoveDir.DOWN);
				player.step(MoveDir.UP);
				setDialog(new GirlDialog2());
			}
		} else if(stepFase == 4) {
			if(DataHolder.is("girl","home")){
				girl.step(MoveDir.UP);
				player.step(MoveDir.UP);
				if(player.getY()<1200){
					stepFase = 6;
				}
			} else
			if (DataHolder.is("girl", "fail")) {
				if(noMessage ){
					addMessage(girl.getX(), girl.getYHead(), "Видеть тебя не хочу!", 4, Color.YELLOW);
					noMessage= false;
				}
				girl.step(MoveDir.UP);
				if (girl.getY() < 1200) {
					frezeePlayer = false;
					stepFase = 10;
					actions.stream().filter(a -> a.getDescription().equals("Это мой дом")).forEach(a -> a.setDescription("Вернуться домой"));
					actions.stream().filter(a -> a.getDescription().equals("Её дом в другой стороне")).forEach(a -> a.setDescription("Схожу кое-куда..."));
				}
			} else {
				if(noMessage ){
					addMessage(girl.getX(), girl.getYHead(), "Уже слишком поздно, доброй ночи!", 4, Color.YELLOW);
					noMessage= false;
				}
				girl.step(MoveDir.UP);
				if (girl.getY() < 1200) {
					frezeePlayer = false;
					stepFase = 10;
					actions.stream().filter(a -> a.getDescription().equals("Это мой дом")).forEach(a -> a.setDescription("Вернуться домой"));
					actions.stream().filter(a -> a.getDescription().equals("Её дом в другой стороне")).forEach(a -> a.setDescription("Схожу кое-куда..."));
				}
			}
		} else if(stepFase == 6) {
			decay+=tick*20;
			if(decay>255)
				nextScene.run();
		}
	}

	@Override
	public void customAction(ActionZone action) throws Exception {
		if (action.getDescription().equals("Нарвать цветов ")) {
			DataHolder.set("flower", "fail");
			addMessage(action.makeMessage("Вы сорвали цветы", Color.WHITE));
			actions.stream().filter(a -> a.getDescription().equals("Нарвать цветов ")).forEach(a -> a.setDescription(""));
			actions.stream().filter(a -> a.getDescription().equals("Нарвать цветов")).forEach(a -> a.setDescription(""));
		} else if (action.getDescription().equals("Нарвать цветов")) {
			DataHolder.set("flower", "good");
			addMessage(action.makeMessage("Вы сорвали цветы", Color.WHITE));
			actions.stream().filter(a -> a.getDescription().equals("Нарвать цветов ")).forEach(a -> a.setDescription(""));
			actions.stream().filter(a -> a.getDescription().equals("Нарвать цветов")).forEach(a -> a.setDescription(""));
		}  else if (action.getDescription().equals("Вернуться домой")) {
			stepFase = 6;
			frezeePlayer = true;
		} if (action.getDescription().equals("Схожу кое-куда...")) {
			DataHolder.inc("kills");
			stepFase = 6;
			frezeePlayer = true;
		}
	}

	@Override
	public void load() throws IOException {
		street = ImageIO.read(new File("locations/sleep1/street.png"));
		moveMap = ImageIO.read(new File("locations/sleep1/moveMap.png"));	
	}

	@Override
	public void postRun() {
		nextScene = new Scene4();
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
