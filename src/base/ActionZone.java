package base;
import java.awt.Color;

public class ActionZone extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1551983856499113613L;

	public ActionZone(int x, int y, String description) {
		super(x, y, description, 0, Color.white);
		setUseAlpha(false);
	}

	public void activate() {

	}

	public Message makeMessage(String message, Color color) {
		setTime(4);
		return new Message(getX(), getY() - 30, message, 4, color);
	}

}
