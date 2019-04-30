package base;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public abstract class SimpleDialog implements Serializable, Dialog {

	private static final long serialVersionUID = 2716687379347527101L;
	protected Message message;
	private List<Answer> answers;
	private List<Answer> newAnswers = new ArrayList<>();
	private int[] rad = new int[] { 10, 140, 110, 170, 80, 190, 220 };
	private float delta = 0;
	private float y;
	private float x;
	private Answer rigthAnswer;
	private float timer;
	protected int node = 0;
	private SerRunnable action;
	private boolean forseExit;

	public SimpleDialog() {
		next(node++);
		answers = newAnswers;
		newAnswers = new ArrayList<>(); 
		timer = 1;
		
	}

	protected abstract void next(int i);
		

	protected void add(Answer answer) {
		newAnswers.add(answer);	
	}

	public void draw(Graphics2D g) {
		AffineTransform baseTransform = g.getTransform();
		g.translate(0, -200);
		message.draw(g);
		g.setTransform(baseTransform);
		g.translate(0, 100);
		AffineTransform transform = g.getTransform();
		float angle = (float) (Math.PI * 2 / (answers.size() - 1));
		angle += delta;
		for (int i = 0; i < answers.size(); i++) {
			int dir = 1 - (i % 2) * 2;
			g.setTransform(transform);
			float a = angle * (i + 1) * dir;

			double dist = answers.get(i).distanceTo(x, y);
			int alpha = (int) (128 - dist);
			if (alpha < 0)
				alpha = 0;
			Answer answer = answers.get(i);
			answer.setBackTrans(alpha);
			answer.setX(rad[i % rad.length] * Math.cos(a));
			answer.setY(rad[i % rad.length] * Math.sin(a));
			answer.draw(g);
		}
		g.setTransform(transform);
		g.translate(x, y);

		double dist = answers.stream().mapToDouble(e -> e.distanceTo(x, y)).min().orElseGet(() ->10000.);
		int rad = (int) (10 + Math.pow(dist, 0.7));
		if (rad > 127)
			rad = 127;
		g.setColor(new Color(Color.ORANGE.getRed(), Color.ORANGE.getGreen(), Color.ORANGE.getBlue(), 255 - rad * 2));
		g.fillOval(-rad, -rad, rad * 2, rad * 2);
	}

	public void tick(float tick, Set<Integer> keys) {
		delta += tick / 10;
		if ((keys.contains(KeyEvent.VK_W)) || (keys.contains(KeyEvent.VK_UP)))
			y -= 5;
		if ((keys.contains(KeyEvent.VK_S)) || (keys.contains(KeyEvent.VK_DOWN)))
			y += 5;
		if ((keys.contains(KeyEvent.VK_A)) || (keys.contains(KeyEvent.VK_LEFT)))
			x -= 5;
		if ((keys.contains(KeyEvent.VK_D)) || (keys.contains(KeyEvent.VK_RIGHT)))
			x += 5;
		if (keys.contains(KeyEvent.VK_SPACE) || keys.contains(KeyEvent.VK_E)) {
			Answer answer = answers.stream().sorted(Comparator.comparing(e -> e.distanceTo(x, y))).findFirst().get();
			if (answer.distanceTo(x, y) < 100) {
				rigthAnswer = answer;
			}
		}
		if (keys.contains(KeyEvent.VK_CONTROL)) 
			forseExit = true;
		if (rigthAnswer != null) {
			timer -= tick;
			answers.stream().filter(a -> a != rigthAnswer).forEach(a -> a.decTime(tick));
		}
		if (timer < 0) {
			rigthAnswer.apply();
			rigthAnswer= null;
			timer = 1;
			next(node++);
			answers = newAnswers;
			newAnswers = new ArrayList<>(); 
		}
		x = x - Math.signum(x) * x * x * 0.00001f - x * 0.01f;
		y = y - Math.signum(y) * y * y * 0.00001f - y * 0.01f;
	}
	public boolean finished(){
		return forseExit || answers.isEmpty();
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
