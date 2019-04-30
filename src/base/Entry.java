package base;

import java.io.Serializable;

public class Entry implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4723719502943517083L;
	private float x;
	private float y;
	
	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}
	public void setX(double x) {
		this.x = (float) x;
	}

	public void setY(double y) {
		this.y = (float) y;
	}
	public double distanceTo(Character character) {
		return Math.sqrt((character.getX() - getX()) * (character.getX() - getX()) + (character.getYHead() - getY()) * (character.getYHead() - getY()));
	}
	public double distanceTo(Entry entry) {
		return Math.sqrt((entry.getX() - getX()) * (entry.getX() - getX()) + (entry.getY() - getY()) * (entry.getY() - getY()));
	}
	public double distanceTo(float x,float y) {
		return Math.sqrt((x - getX()) * (x - getX()) + (y - getY()) * (y - getY()));
	}
}

