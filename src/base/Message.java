package base;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.io.File;
import java.io.IOException;

public class Message extends Entry {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5943183233781734772L;
	private static Font font;
	static {
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("font.ttf"));
			font = font.deriveFont(32f);
			// font = font.deriveFont(Font.BOLD);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static final Color GRAY1 = new Color(0, 0, 0, 16);
	private String description;
	private float time;
	private Color color;
	private Runnable action;
	private boolean toScreen;
	private boolean useAlpha = true;
	private Color backColor;

	public void setBackColor(Color backColor) {
		this.backColor = backColor;
	}

	public Message(int x, int y, String description, float time, Color color) {
		super();
		this.setX(x);
		this.setY(y);
		this.description = description;
		this.time = time;
		this.color = color;
		backColor = GRAY1;
	}

	public void draw(Graphics2D g) {
		if (description.length() == 0)
			return;
		Composite old = g.getComposite();
		if ((time < 1) && isUseAlpha()) {
			float alpha = time - 0.1f;
			if (alpha < 0)
				alpha = 0;
			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
			g.setComposite(ac);
		}
		g.translate(getX(), getY());
		FontRenderContext frc = g.getFontRenderContext();
		TextLayout textTl = new TextLayout(description, font, frc);
		Shape outline = textTl.getOutline(null);
		Rectangle bounds = outline.getBounds();
		int w = (int) bounds.getWidth();
		int h = (int) bounds.getHeight();
		
		g.setColor(backColor);
		g.fillOval(bounds.x - bounds.width / 2, bounds.y - bounds.height / 2, bounds.width, bounds.height);
		bounds.width += 10;
		bounds.height += 5;
		g.fillOval(bounds.x - bounds.width / 2, bounds.y - bounds.height / 2, bounds.width, bounds.height);
		bounds.width += 10;
		bounds.height += 5;
		g.fillOval(bounds.x - bounds.width / 2, bounds.y - bounds.height / 2, bounds.width, bounds.height);
		g.translate(-w / 2, -h / 2);
		// g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

		g.setColor(color);
		g.setStroke(new BasicStroke(1.2f));
		g.fill(outline);
		g.setColor(darkestColor(color));
		g.draw(outline);
		g.setComposite(old);

	}

	private Color darkestColor(Color color) {
		return new Color(color.getRed() / 2, color.getGreen() / 2, color.getBlue() / 2);
	}

	public void setTime(float time) {
		this.time = time;
	}

	public float getTime() {
		return time;
	}

	public void decTime(float tick) {
		time -= tick;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void addPostAction(SerRunnable action) {
		this.action = action;
	}

	public void move(float camXd, float camYd) {
		this.setX(this.getX() + camXd);
		this.setY(this.getY() + camYd);
	}

	public void toScreen(boolean toScreen) {
		this.toScreen = toScreen;
	}

	public boolean isToScreen() {
		return toScreen;
	}

	public boolean isUseAlpha() {
		return useAlpha;
	}

	public void setUseAlpha(boolean useAlpha) {
		this.useAlpha = useAlpha;
	}
	public boolean isTimeout(){
		return getTime()<=0;
	}

	public Color getColor() {
		return color;
	}
	public void apply() {
		if (action != null) {
			action.run();
			action = null;
		}
	}
}
