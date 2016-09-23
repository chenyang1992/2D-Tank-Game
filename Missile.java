package tankWar;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Missile {
	public static final int XSPEED = 10;
	public static final int YSPEED = 10;

	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;

	int x, y;
	Direction dir;

	// Check if the bullet is ours
	private boolean good;

	// Check if the bullet has been out of bound
	private boolean live = true;

	private TankClient tc;

	private static Toolkit tk = Toolkit.getDefaultToolkit();

	private static Image[] MissileImages = null;
	private static Map<String, Image> imgs = new HashMap<String, Image>();

	static {
		MissileImages = new Image[] { tk.getImage(Tank.class.getClassLoader().getResource("images/missileL.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/missileLU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/missileU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/missileRU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/missileR.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/missileRD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/missileD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/missileLD.gif")) };

		imgs.put("L", MissileImages[0]);
		imgs.put("LU", MissileImages[1]);
		imgs.put("U", MissileImages[2]);
		imgs.put("RU", MissileImages[3]);
		imgs.put("R", MissileImages[4]);
		imgs.put("RD", MissileImages[5]);
		imgs.put("D", MissileImages[6]);
		imgs.put("LD", MissileImages[7]);
	}

	public Missile(int x, int y, Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}

	public Missile(int x, int y, boolean good, Direction dir, TankClient tc) {
		this(x, y, dir);
		this.good = good;
		this.tc = tc;
	}

	public void drawMissile(Graphics g) {
		if (!live) {
			tc.missiles.remove(this);
			return;
		}

		switch (dir) {
		case L:
			g.drawImage(imgs.get("L"), x, y, null);
			break;
		case LU:
			g.drawImage(imgs.get("LU"), x, y, null);
			break;
		case U:
			g.drawImage(imgs.get("U"), x, y, null);
			break;
		case RU:
			g.drawImage(imgs.get("RU"), x, y, null);
			break;
		case R:
			g.drawImage(imgs.get("R"), x, y, null);
			break;
		case RD:
			g.drawImage(imgs.get("RD"), x, y, null);
			break;
		case D:
			g.drawImage(imgs.get("D"), x, y, null);
			break;
		case LD:
			g.drawImage(imgs.get("LD"), x, y, null);
			break;
		}

		move();
	}

	private void move() {
		switch (dir) {
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		}

		if (x < 0 || y < 0 || x > tc.GAME_WIDTH || y > tc.GAME_HEIGHT) {
			live = false;
		}
	}

	public boolean isLive() {
		return live;
	}

	public Rectangle getRect() {
		// A Rectangle specifies an area in a coordinate space that is enclosed
		// by (x,y), its width, and its height.
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	// Chech is the bullet hits the tank
	public boolean hitTank(Tank t) {
		if (this.live && this.getRect().intersects(t.getRect()) && t.isLive() && this.good != t.isGood()) {
			if (t.isGood()) {
				t.setLife(t.getLife() - 20);
				if (t.getLife() <= 0) {
					t.setLive(false);
				}
			} else {
				t.setLive(false);
			}
			this.live = false;
			Explode e = new Explode(x, y, tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}

	public boolean hitTanks(List<Tank> tanks) {
		for (int i = 0; i < tanks.size(); i++) {
			if (hitTank(tanks.get(i))) {
				return true;
			}
		}
		return false;
	}

	public boolean hitWall(Wall w) {
		if (this.live && this.getRect().intersects(w.getRect())) {
			this.live = false;
			return true;
		}
		return false;
	}

}
