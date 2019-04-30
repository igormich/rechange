package day1;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.ObjectStreamException;

import base.ActionZone;
import base.Scene;
import day3.Scene3;

public class Learn extends Scene{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1421689899620334171L;
	private Scene3 nextScene1;

	@Override
	public void init() throws Exception {
		
	}

	@Override
	public void render(Graphics2D g, int x, int y, int cx, int cy) {
		
	}

	@Override
	public void customTick(float tick) throws Exception {
		
	}

	@Override
	public void customAction(ActionZone action) throws Exception {
		
	}

	@Override
	public void load() throws IOException {
		
	}
	@Override
	public void postRun() {
		nextScene1 = new Scene3();
		try {
			nextScene1.init();
			nextScene1.load();
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
