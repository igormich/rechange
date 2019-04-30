package base;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public abstract class Scene extends Component implements KeyListener {

	public static final Color LIGTH_BLUE = new Color(128, 128, 255);
	public static final Color LIGTH_BLUE1 = new Color(192, 192, 255);
	public static final Color BROWN = new Color(255, 128, 0);
	public static final Color DYELOOW = new Color(128, 128, 0);
	protected static final int WIDTH = 1024;
	protected static final int HEIGHT = 768;
	protected static final int ACTION_DISTANCE = 100;
	private static JFrame frame;
	private static Scene scene;
	private static int speed = 1;
	

	static {
		if (frame == null) {
			frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setResizable(false);
			frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					if(scene!=null){
						File save = new File("game.sav");
						try {
							ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(save)));
							oos.writeObject(scene);
							oos.writeObject(DataHolder.data);
							oos.flush();
							oos.close();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}

				@Override
				public void windowLostFocus(WindowEvent arg0) {
					keys.clear();
				}
				

			});
		}
	}
	private static final long serialVersionUID = 4217006877160127542L;




	protected Character player;

	protected static int lastKey = 0;
	protected static Set<Integer> keys = new HashSet<>();
	protected transient BufferedImage[] buffer = new BufferedImage[2];
	private int activeBuffer = 0;
	protected transient BufferedImage moveMap;
	private transient BufferedImage dialogBackgroung;
	protected Set<Integer> moveColor = new HashSet<>();
	protected Set<ActionZone> actions = new HashSet<>();
	//protected Message message;
	protected Set<Message> messages = new HashSet<>();
	protected Message achivement;
	protected Set<Message> new_messages = new HashSet();
	protected boolean frezeePlayer = false;

	private float camX = -1;
	private float camY = -1;
	private float camXd = 0;
	private float camYd = 0;
	private transient Timer timer = new Timer();
	private transient Thread thread;
	private boolean active = true;
	private Dialog dialog;

	@Override
	public void paint(Graphics _g) {
		if (buffer != null)
			_g.drawImage(buffer[1 - activeBuffer], 0, 0, null);
	}

	public Scene() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		moveColor.add(0xffffff);
	}

	private void end() {
		timer.cancel();
		frame.remove(this);
		active = false;
	}

	public abstract void init() throws Exception;



	public void display() {
		frame.add(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.addKeyListener(this);
	}

	public void run() {
		if (scene == this) {
			return;
		}
		if (scene != null) {
			scene.end();
		}
		buffer = new BufferedImage[2];
		buffer[0] = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
		buffer[1] = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
		dialogBackgroung = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
		scene = this;
		frame.add(this);
		timer = new Timer(true);
		timer.scheduleAtFixedRate(new TimerTask() {
			long time = System.currentTimeMillis();

			@Override
			public void run() {
				try {
					//frame.setTitle("FPS " + (int) (1 / ((System.currentTimeMillis() - time) / 1000f)));
					sceneTick(speed * (System.currentTimeMillis() - time) / 1000f);
					if(frezeePlayer &&(lastKey == KeyEvent.VK_CONTROL)){
						for(int i=0;i<10;i++)
							sceneTick(speed * (System.currentTimeMillis() - time) / 1000f);
					}
					time = System.currentTimeMillis();

				} catch (Exception e) {
					JOptionPane.showMessageDialog(Scene.this, "Вы сломали игру", "Ошибка", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
					frame.dispose();
					end();
				}
			}
		}, 0, 10 / speed);
		thread = new Thread(new Runnable() {
			long time = System.currentTimeMillis();
			@Override
			public void run() {
				while (active) {				
					time = System.currentTimeMillis();
					render((Graphics2D) buffer[activeBuffer].getGraphics());
					activeBuffer = 1 - activeBuffer;
					revalidate();
					repaint();	
					frame.setTitle("FPS " + (int) (1 / ((System.currentTimeMillis() - time) / 1000f)));
				}
			}
		});
		thread.setDaemon(true);
		thread.start();
		postRun();
	}

	public abstract void postRun();

	protected void render(Graphics2D g) {
		if (camX == -1) {
			camX = player.getX();
			camY = player.getY();
		}
		if (camX + camXd < player.getX() - 5)
			camXd += 0.1f;
		else
			camXd -= 0.1f;
		if (camX - camXd > player.getX() + 5)
			camXd -= 0.1f;
		else
			camXd += 0.1f;
		if (camY + camYd < player.getY() - 5)
			camYd += 0.1f;
		else
			camYd -= 0.1f;
		if (camY - camYd > player.getY() + 5)
			camYd -= 0.1f;
		else
			camYd += 0.1f;
		camX += camXd;
		camY += camYd;

		camXd *= 0.95;
		camYd *= 0.95;

		camX = player.getX();
		camY = player.getY();

		int x = (int) (-camX + WIDTH / 2);
		int y = (int) (-camY + HEIGHT / 2);

		int cx = 0;
		int cy = 0;
		if (x > 0)
			cx = x;
		if (y > 0)
			cy = y;
		if (x - WIDTH < -moveMap.getWidth(null) * 4)
			cx = (x - WIDTH + moveMap.getWidth(null) * 4);
		if (y - HEIGHT < -moveMap.getHeight(null) * 4)
			cy = (y - HEIGHT + moveMap.getHeight(null) * 4);
		AffineTransform baseTransform = g.getTransform();
		Dialog dDialog =dialog;
		if (dDialog != null) {
			g.drawImage(dialogBackgroung, 0, 0, null);
			g.translate(WIDTH/2, HEIGHT/2);
			dDialog.draw(g);
		} else {
			this.render(g, x, y, cx, cy);
			Optional<ActionZone> action = actions.stream().sorted(Comparator.comparing(a -> a.distanceTo(player))).findFirst();
			if (action.isPresent() && action.get().distanceTo(player) < ACTION_DISTANCE && action.get().getTime() <= 0) {
				g.setTransform(baseTransform);
				g.translate(x - cx, y - cy);
				action.get().draw(g);
			}
			if(lastKey == KeyEvent.VK_CONTROL)
				for(ActionZone action1:actions) {
					g.setTransform(baseTransform);
					g.translate(x - cx, y - cy);
					action1.draw(g);
				}
		}
		for(Message message:messages){
			if (message.isToScreen())
				message.move(camXd, camYd);
			g.setTransform(baseTransform);
			g.translate(x - cx, y - cy);
			message.draw(g);
		}
		if(achivement!=null){
			g.setTransform(baseTransform);
			g.translate(WIDTH/2, 100);
			achivement.draw(g);
		}
	}

	public abstract void render(Graphics2D g, int x, int y, int cx, int cy);

	protected void sceneTick(float tick) throws Exception {
		
		if (dialog != null) {
			dialog.tick(tick, keys);
			if(dialog.finished()) {
				Dialog tdDialog = dialog;
				dialog = null;
				tdDialog.apply();
			}
			return;
		}
		playerMove();
		if ((lastKey == KeyEvent.VK_E) || (lastKey == KeyEvent.VK_SPACE) || (lastKey == KeyEvent.VK_ENTER)) {
			lastKey = 0;
			Optional<ActionZone> _action = actions.stream().sorted(Comparator.comparing(a -> a.distanceTo(player))).findFirst();
			if (_action.isPresent() && _action.get().distanceTo(player) < ACTION_DISTANCE && _action.get().getTime() <= 0) {
				ActionZone action = _action.get();
				customAction(action);
			}
		}
		actions.forEach(a -> a.decTime(tick));
		for(Message message:messages){
			message.decTime(tick);
			if (message.isTimeout()) {
				message.apply();
			}
		}
		messages = Stream.concat(
				messages.stream().filter(m -> !m.isTimeout()),
				new_messages.stream())
				.collect(Collectors.toSet());
		if(achivement!=null){
			achivement.decTime(tick);
		}
		customTick(tick);
	}

	public abstract void customTick(float tick) throws Exception;

	public abstract void customAction(ActionZone action) throws Exception;

	private void playerMove() {
		if (frezeePlayer)
			return;
		if ((lastKey == KeyEvent.VK_W) || (lastKey == KeyEvent.VK_UP))
			player.step(MoveDir.UP);
		else if ((lastKey == KeyEvent.VK_S) || (lastKey == KeyEvent.VK_DOWN))
			player.step(MoveDir.DOWN);
		else if ((lastKey == KeyEvent.VK_A) || (lastKey == KeyEvent.VK_LEFT))
			player.step(MoveDir.LEFT);
		else if ((lastKey == KeyEvent.VK_D) || (lastKey == KeyEvent.VK_RIGHT))
			player.step(MoveDir.RIGTH);
		else
			player.step(MoveDir.NO);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		lastKey = e.getKeyCode();
		keys.add(e.getKeyCode());
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		lastKey = 0;
		keys.remove(e.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public boolean canMoveTo(int x, int y) {
		int color = moveMap.getRGB(x / 4, y / 4) & 0xffffff;
		return moveColor.contains(color);
	}

	public int getcamX() {
		return (int) camX;
	}

	public int getcamY() {
		return (int) camY;
	}

	public Message addMessage(Message message) {
		this.new_messages.add(message);
		return message;
	}
	public Message addAchivement(String achivement) {
		Achivements.addAchivement(achivement);
		this.achivement = new Message(0, 0, "Достижение: " +achivement, 10, Color.ORANGE);
		return this.achivement;
	}
	public Message addMessage(int x, int y, String description, int time, Color color, SerRunnable action) {
		Message message = new Message(x, y, description, time, color);
		message.addPostAction(action);
		return addMessage(message);

	}

	public Message addMessage(int x, int y, String description, int time, Color color) {
		return addMessage(x, y, description, time, color, null);
	}

	public Dialog setDialog(Dialog dialog) {
		keys.clear();
		lastKey = 0;
		if(this.dialog==null){
			render((Graphics2D) dialogBackgroung.getGraphics());
			Graphics g = dialogBackgroung.getGraphics();
			g.setColor(new Color(32, 32, 32, 128));
			g.fillRect(0, 0, WIDTH, HEIGHT);
		}
		this.dialog = dialog;
		return dialog;
	}

	public abstract void load() throws IOException;

	public boolean haveMessages() {
		return messages.size()>0;
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