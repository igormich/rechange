package base;

import java.awt.Graphics2D;
import java.util.Set;

public interface Dialog {

	void draw(Graphics2D g);

	void tick(float tick, Set<Integer> keys);

	boolean finished();

	void addPostAction(SerRunnable object);
	void apply();
}
