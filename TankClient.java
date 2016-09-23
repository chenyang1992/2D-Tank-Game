package tankWar;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

//Main class
public class TankClient extends Frame {
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;

	Tank myTank = new Tank(350, 520, true, Direction.STOP, this);
	Wall w1 = new Wall(100, 300, 20, 100, this), w2 = new Wall(300, 250, 150, 20, this);
	Blood b = new Blood(this);

	// Create a set to put in bullets
	List<Missile> missiles = new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	List<Tank> tanks = new ArrayList<Tank>();

	Image offScreenImage = null;

	@Override
	public void paint(Graphics g) {
		if (tanks.size() <= 0) {
			for (int i = 0; i < Integer.parseInt(ProperMgr.getProperty("reProduceTank")); i++) {
				tanks.add(new Tank(50 + 40 * (i + 1), 50, false, Direction.D, this));
			}
		}

		g.drawString("missiles Count: " + missiles.size(), 10, 50);
		g.drawString("explodes Count: " + explodes.size(), 10, 70);
		g.drawString("tanks Count: " + tanks.size(), 10, 90);
		g.drawString("myTank life: " + myTank.getLife(), 10, 110);

		// Traverse the set to draw the bullets in it
		for (int i = 0; i < missiles.size(); i++) {
			Missile m = missiles.get(i);
			m.hitTanks(tanks);
			m.hitTank(myTank);
			m.hitWall(w1);
			m.hitWall(w2);
			m.drawMissile(g);
		}

		for (int i = 0; i < explodes.size(); i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}

		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			t.collidesWithWall(w1);
			t.collidesWithWall(w2);
			t.collidesWithTanks(tanks);
			t.drawTank(g);
		}

		myTank.drawTank(g);
		myTank.eat(b);
		w1.draw(g);
		w2.draw(g);
		b.draw(g);
	}

	@Override
	public void update(Graphics g) {
		if (offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.BLACK);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	public void luanchFrame() {
		int initTankCount = Integer.parseInt(ProperMgr.getProperty("initTankCount"));

		for (int i = 0; i < initTankCount; i++) {
			tanks.add(new Tank(50 + 40 * (i + 1), 50, false, Direction.D, this));
		}

		this.setLocation(300, 50);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setTitle("Tank Game - Help(Resurrection: F2; Fire: Key 1; SuperFire: WhiteSpace)");
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setBackground(Color.BLACK);

		this.addKeyListener(new KeyMonitor());

		setVisible(true);

		new Thread(new PaintThread()).start();
	}

	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.luanchFrame();
	}

	public class PaintThread implements Runnable {

		public void run() {
			while (true) {
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public class KeyMonitor extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}
	}

}