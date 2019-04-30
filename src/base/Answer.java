package base;

import java.awt.Color;

public class Answer extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6656831118359088246L;

	public Answer(String description, Color color) {
		super(0, 0, description, 1, color);		
		setBackColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 32));
	}
	public Answer(String description, Color color, SerRunnable action) {
		super(0, 0, description, 1, color);		
		setBackColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 32));
		addPostAction(action);
	}
	public double distanceTo(float x,float y) {
		return super.distanceTo(x,y);
	}
	public void setBackTrans(int a) {
		Color color = super.getColor();
		setBackColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), a));
	}

}
