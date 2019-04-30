package day2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.ObjectStreamException;

import base.ActionZone;
import base.Character;
import base.DataHolder;
import base.Scene;
import day1.Scene1;

public class Sleep3 extends Scene1 {
	
	public static void main(String[] args) throws Exception {
		Scene scene = new Sleep3();
		scene.init();
		scene.load();
		scene.display();
		scene.run();
	}
	private static final long serialVersionUID = 1130927382132077278L;
	private Scene nextScene;
	@Override
	public void init() throws Exception {
		player = new Character("hero", 3,  550, 600, this);
		actions.add(new ActionZone(600, 400, "Выглянуть"));
		actions.add(new ActionZone(430, 550, "Лечь спать"));
	}
	@Override
	public void customAction(ActionZone action) throws Exception {
		if (action.getDescription().equals("Выглянуть")) {
			addMessage(action.makeMessage("Кажется можно вылезти", Color.ORANGE));
			action.setDescription("Вылезти");
		} else if (action.getDescription().equals("Вылезти")) {
			DataHolder.inc("kills");
			addMessage(action.makeMessage("Схожу улажу маленькое дельце...", Color.ORANGE))
				.addPostAction(() -> {
						if((DataHolder.getInt("kills") >= 2) && DataHolder.is("name","Джек")){
							addAchivement("Потрошитель");
						}
						nextScene.run();
				});
		}if (action.getDescription().equals("Лечь спать")) {
			nextScene.run();
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

			g.setTransform(baseTransform);
			g.translate(x - cx, y - cy);
			if (player.getY() < 240 * 4) {
				g.drawImage(front1, 0, 0, front1.getWidth(null) * 4, front1.getHeight(null) * 4, null);
				if (!door_open)
					g.drawImage(door, 0, 0, front1.getWidth(null) * 4, front1.getHeight(null) * 4, null);
			}
			g.setTransform(baseTransform);
			g.setColor(new Color(8, 16, 32, 64));
			g.fillRect(0, 0, WIDTH, HEIGHT);
		}
	@Override
	public void postRun() {
		nextScene = new day3.Scene1();
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
