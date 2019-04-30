package base;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ElevatorDoors extends Entry{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1751077758342261119L;
	private transient Image edoor1;
	private transient Image edoor2;
	private float opening = 1;

	public ElevatorDoors(int x, int y) throws IOException {
		this.setX(x);
		this.setY(y);
		load();
	}

	private void load() throws IOException {
			edoor1 = ImageIO.read(new File("locations/edoor1.png"));
			edoor2 = ImageIO.read(new File("locations/edoor2.png"));	
	}

	public void draw(Graphics2D g) {
		int open = (int) (edoor1.getWidth(null) * opening);
		g.drawImage(edoor1, getX(), getY() - edoor1.getHeight(null) * 4, getX() + open * 4, getY(), 0, 0, open, edoor1.getHeight(null), null);
		int x2 = getX() + edoor1.getWidth(null) * 4;
		open = (int) (edoor2.getWidth(null) * (1 - opening));
		g.drawImage(edoor2, x2 + open * 4, getY() - edoor2.getHeight(null) * 4, x2 + edoor1.getWidth(null) * 4, getY(), open, 0, edoor1.getWidth(null), edoor2.getHeight(null), null);
	}

	public void open(float tick) {
		opening -= tick;
		if (opening < 0)
			opening = 0;
	}

	public void close(float tick) {
		opening += tick;
		if (opening > 1)
			opening = 1;
	}

	public boolean isOpen() {
		return opening == 0;
	}

	public boolean isClosed() {
		return !isOpen();
	}
	private Object readResolve() throws java.io.ObjectStreamException {
		try {
			load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}
}
