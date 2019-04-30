package base;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Character extends Entry{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6243842106435197239L;
	private transient Image body;
	private transient Image shadow;
	private int w4;
	private int h4;
	private int fh;
	private int fw;
	private int animDir = 1;
	private int frames;
	private MoveDir moveDir;
	private int stepsPerFrame = 3*5;
	private int step = 2;
	private int stepsPerFrameCounter = 0;
	private Scene scene;
	private Message message;
	private String filename;

	public Character(String filename, int frames, int x, int y, Scene scene) throws IOException {
		this.filename = filename;
		this.frames = frames;
		this.scene = scene;
		load();
		w4 = body.getWidth(null) / frames;
		h4 = body.getHeight(null) / 4;

		fh = 0;
		fw = 0;
		this.setX(x);
		this.setY(y);
	}

	private void load() throws IOException {
		body = ImageIO.read(new File("characters/" + filename + ".png"));
		shadow = ImageIO.read(new File("characters/shadow.png"));
	}

	public void draw(Graphics2D g, boolean renderShadow) {
		if(renderShadow){
			if (frames == 3)
				g.drawImage(shadow, -w4 * 2, -h4/2, w4 * 4, h4, null);
			else
				g.drawImage(shadow, -w4 * 2, -h4, w4 * 4, h4, null);
		}
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.drawImage(body, -w4 * 2, -h4 * 4, w4 * 2, 0, w4 * fw, h4 * fh, w4 * (fw + 1), h4 * (fh + 1), null);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
	}
	public void draw(Graphics2D g) {
		draw(g,true);
	}

	public void step(MoveDir moveDir) {
		switch (moveDir) {
		case UP:
			fh = 3;
			break;
		case DOWN:
			fh = 0;
			break;
		case LEFT:
			fh = 1;
			break;
		case RIGTH:
			fh = 2;
			break;
		default:
			break;
		}
		if ((moveDir == this.moveDir) && (moveDir != MoveDir.NO)) {
			if (move())
				stepsPerFrameCounter++;
			else
				fw = frames == 4 ? 0 : 1;
			if (stepsPerFrameCounter == stepsPerFrame) {
				stepsPerFrameCounter = 0;
				fw += animDir;
				if (frames == 4)
					fw = fw % frames;
				else if ((fw == frames - 1) || (fw == 0))
					animDir = -animDir;
			}
		} else {
			if (frames == 4)
				fw = 0;
			else
				fw = 1;
		}
		this.moveDir = moveDir;
	}

	private boolean move() {
		int ny = getY();
		int nx = getX();
		switch (moveDir) {
		case UP:
			ny -= step;
			break;
		case DOWN:
			ny += step;
			break;
		case LEFT:
			nx -= step;
			break;
		case RIGTH:
			nx += step;
			break;
		default:
			break;
		}
		if(message!=null){
			message.move(nx-getX(),ny-getY());
		}
		if (scene.canMoveTo(nx, ny)) {
			setX(nx);
			setY(ny);
			return true;
		}
		return false;
	}

	public int getYHead() {
		return getY() - h4 * 3;
	}

	public void setMessage(Message message) {
		this.message = message;	
	}

	public void baseFrame() {
		//fw = frames == 4 ? 1 : 1;
		fw = 1;
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
