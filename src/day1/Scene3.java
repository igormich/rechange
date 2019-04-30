package day1;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectStreamException;

import base.ActionZone;
import base.Character;
import base.DataHolder;
import base.ElevatorDoors;
import base.Message;
import base.MoveDir;
import base.Scene;

public class Scene3 extends Scene1 {

	Scene nextScene;
	public static void main(String[] args) throws Exception {
		Scene scene = new Scene3();
		scene.load();		
		scene.init();
		scene.display();
		scene.run();
	}
	private static final long serialVersionUID = -1337829637555604227L;
	private int stepFase = 0;
	@Override
	public void init() throws Exception {
		actions.add(new ActionZone(430, 550, "Проглотить пилюлю"));
		actions.add(new ActionZone(600, 350, "Выкинуть таблетки"));
		actions.add(new ActionZone(200, 780, "Выкинуть таблетки"));// урна
		actions.add(new ActionZone(600, 800, "Постучать"));// дверь
		elevatorDoors = new ElevatorDoors(605 * 4, 227 * 4);
		player = new Character("player_sick_hand_back", 4, 2500, 850, this);
		cop = new Character("cop1", 4, 2580, 850, this);
		frezeePlayer = true;
		moveColor.add(0xff0000);
	}
	@Override
	public void customTick(float tick) throws IOException {
		
		if ((lastKey != 0) && !haveMessages() && stepFase < 3) {
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
			if (player.getY() > 1050)
				stepFase = 1;
		}
		if (stepFase == 1) {
			player.step(MoveDir.LEFT);
			cop.step(MoveDir.LEFT);
			if(player.getX() < 560){
				stepFase = 2;
				door_open = true;
			}
		}
		
		if (stepFase == 2) {
			player.step(MoveDir.UP);
			cop.step(MoveDir.LEFT);
			if(player.getY() < 880){
				moveColor.remove(0xff0000);
				stepFase = 3;
				frezeePlayer = false;
				door_open = false;
				addMessage(cop.getX(), cop.getYHead(), "А теперь отдыхай!", 4, Scene.LIGTH_BLUE);
			}
		}
		if (stepFase == 3) {
			cop.step(MoveDir.UP);
			if(cop.getX() < 920){
				stepFase = 4;
				cop.step(MoveDir.DOWN);
			}
		}
	}
	@Override
	public void customAction(ActionZone action) throws Exception {
		 if (action.getDescription().equals("Постучать")) {
			 addMessage(cop.getX(), cop.getYHead(), "Давай кушай таблетки и ложись спать", 4, Scene.LIGTH_BLUE);
		 } else
		 if (action.getDescription().equals("Выкинуть таблетки")) {
			 addMessage(action.makeMessage("Вы выбросили таблетки", Color.WHITE));
			 actions.stream().filter(a -> a.getDescription().equals("Выкинуть таблетки")).forEach(a -> a.setDescription(""));
			 actions.stream().filter(a -> a.getDescription().equals("Проглотить пилюлю")).forEach(a -> a.setDescription("Лечь спать"));
		 } else
		 if (action.getDescription().equals("Проглотить пилюлю")) {
			 addMessage(action.makeMessage("Вы приняли лекарство", Color.WHITE));
			 DataHolder.inc("pills");
			 actions.stream().filter(a -> a.getDescription().equals("Выкинуть таблетки")).forEach(a -> a.setDescription(""));
			 actions.stream().filter(a -> a.getDescription().equals("Проглотить пилюлю")).forEach(a -> a.setDescription("Лечь спать"));
		 } else
		 if (action.getDescription().equals("Лечь спать")) {
			 addMessage(action.makeMessage("Вы засыпаете", Color.WHITE))
			 	.addPostAction(() -> nextScene.run());
			 action.setDescription("");
		 } 
	}
	@Override
	public void postRun() {
		nextScene = new Sleep1();
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
