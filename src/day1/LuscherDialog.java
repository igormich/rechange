package day1;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import base.Answer;
import base.Dialog;
import base.Message;
import base.SerRunnable;

public class LuscherDialog implements Serializable, Dialog {

	private static final long serialVersionUID = 2716687379347527101L;
	private static final Color TRANSPARENT = new Color(0, 0, 0, 0);
	protected Message message;
	private float y;
	private float x;
	private float timer;
	private int selected = 0;
	private List<Color> cards = new ArrayList<>();
	private SerRunnable action;
	private boolean forseExit;

	public LuscherDialog() {
		timer = 1;
		message = new Answer("Выберите наиболее привлекательный цвет", Color.GREEN);
		cards.add(Color.RED);
		cards.add(Color.BLUE);
		cards.add(Color.GREEN);
		cards.add(Color.GRAY);
		cards.add(Color.YELLOW);
		cards.add(Color.MAGENTA);
		cards.add(Color.BLACK);
		cards.add(Color.WHITE);
		
	}

	public void draw(Graphics2D g) {
		AffineTransform baseTransform = g.getTransform();
		g.translate(0, -200);
		message.draw(g);
		g.setTransform(baseTransform);
		g.translate(-150, 0);
		for(int i=0;i<4;i++) {
			g.setColor(cards.get(i));
			g.fillRect(-50, -75, 100, 150);
			if(i==selected){
				g.setColor(Color.ORANGE);
				g.drawRect(-50, -75, 100, 150);	
			}
			g.translate(110, 0);
		}
		g.setTransform(baseTransform);
		g.translate(-150, 200);
		for(int i=4;i<8;i++) {
			
			g.setColor(cards.get(i));
			g.fillRect(-50, -75, 100, 150);
			if(i==selected){
				g.setColor(Color.ORANGE);
				g.drawRect(-50, -75, 100, 150);	
			}
			g.translate(110, 0);
		}
		g.setTransform(baseTransform);
		g.translate(x, y);
		int rad = 32;
		g.setColor(Color.ORANGE);
		g.fillOval(-rad, -rad, rad * 2, rad * 2);
	}

	public void tick(float tick, Set<Integer> keys) {
		if ((keys.contains(KeyEvent.VK_W)) || (keys.contains(KeyEvent.VK_UP)))
			y -= 5;
		if ((keys.contains(KeyEvent.VK_S)) || (keys.contains(KeyEvent.VK_DOWN)))
			y += 5;
		if ((keys.contains(KeyEvent.VK_A)) || (keys.contains(KeyEvent.VK_LEFT)))
			x -= 5;
		if ((keys.contains(KeyEvent.VK_D)) || (keys.contains(KeyEvent.VK_RIGHT)))
			x += 5;
		selected = -1;
		int cx=-150, cy = 0;
		for(int i=0;i<4;i++) {
			if((x>cx-50)&&(x<cx+50)&&(y>cy-75)&&(y<cy+75)&&(cards.get(i)!=TRANSPARENT)){
				selected = i;
			}
			cx+=100;
		}
		cx=-150;
		cy = 200;
		for(int i=4;i<8;i++) {
			if((x>cx-50)&&(x<cx+50)&&(y>cy-75)&&(y<cy+75)&&(cards.get(i)!=TRANSPARENT)){
				selected = i;
			}
			cx+=110;
		}
		if (keys.contains(KeyEvent.VK_CONTROL)) 
			forseExit = true;
		if (keys.contains(KeyEvent.VK_SPACE)) {
			if(selected!=-1) {
				cards.set(selected, TRANSPARENT);
				int count = (int) cards.stream().filter(c -> c!=TRANSPARENT).count();
				switch (count) {
				case 7:
				case 6:
				case 5:
					message = new Answer("Очень хорошо, продолжайте.", Color.GREEN);
					break;
				case 4:
				case 3:
					message = new Answer("Замечательно!", Color.GREEN);
					break;
				case 2:
					message = new Answer("Теперь выберите наиболее неприятный цвет из двух", Color.GREEN);
					break;
				default:
					break;
				}	
			}
			
		}
	}
	public boolean finished(){
		return forseExit || (cards.stream().filter(c -> c!=TRANSPARENT).count()==1);
	}

	public void addPostAction(SerRunnable action){
		this.action = action;
	}

	@Override
	public void apply() {
		if(action!=null)
			action.run();	
	}


}
