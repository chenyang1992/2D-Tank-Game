package tankWar;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Tank {
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	
	public static final int LIFE = 100;
	
	
	private static Random r = new Random();
	private BloodBar bb = new BloodBar();
	
	// Check if the tank is ours or enemy
	private boolean good;

	// Check if the tank is alive or not
	private boolean live = true;
	
	private int life = LIFE;

	TankClient tc;
	
	// Show the UpLeft point of the tank
	private int x , y;
	// Show the last point of the tank
	private int oldX , oldY;
	
	// Check if the keyboards have been pressed
	private boolean bL = false, bU = false, bR = false, bD = false;
	
	// Define the direction of tank
	private Direction dir = Direction.STOP;
	// Define the direction of tank's pole holder, default is up
	private Direction ptDir = Direction.U;
	
	// Define the steps which tank moves
	private int step = r.nextInt(12) + 3;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	private static Image[] TankImages = null;
	private static Map<String, Image> imgs = new HashMap<String, Image>();
	
	// Static code area, the code start here when call this class
	static{
		TankImages = new Image[] {
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankL.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankLU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankRU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankR.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankRD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankLD.gif"))	
		};
		
		imgs.put("L", TankImages[0]);
		imgs.put("LU", TankImages[1]);
		imgs.put("U", TankImages[2]);
		imgs.put("RU", TankImages[3]);
		imgs.put("R", TankImages[4]);
		imgs.put("RD", TankImages[5]);
		imgs.put("D", TankImages[6]);
		imgs.put("LD", TankImages[7]);
	}
		
	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.good = good;
		this.oldX = x;
		this.oldY =y;
	}
	
	public Tank(int x, int y, boolean good, Direction dir, TankClient tc) {
		this(x, y, good);
		this.dir = dir;
		this.tc = tc;
	}
	
	public void drawTank(Graphics g) {
		if(!live) {
			if(!good) {
				tc.tanks.remove(this);
			}
			return;
		}
		
		if (good) bb.draw(g);
		
		//Draw the direction of the pole holder
		switch (ptDir) {
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
		
		// Redraw the location of tank every time press the key, 
		move(); //每次按键都会重画，就会调用drawTank，在这里重画坦克的此时位置
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_F2:
			if (!this.live) {
				this.live = true;
				this.life = LIFE;
			}
			break;
		case KeyEvent.VK_LEFT:
			bL = true;
			break;
		case KeyEvent.VK_UP:
			bU = true;
			break;
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
		case KeyEvent.VK_DOWN:
			bD = true;
			break;
		}
		locateDraction();
		
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		// When press 1, call fire() to open fire
		case KeyEvent.VK_1:
			fire();
			break;
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break;
		// When press space, open fire
		case KeyEvent.VK_SPACE:
			superFire();
			break;
		}
		locateDraction();
	}

	//check the moving direction
	void locateDraction() {
		if(bL && !bU && !bR && !bD) dir =Direction.L;
		else if(bL && bU && !bR && !bD) dir =Direction.LU;
		else if(!bL && bU && !bR && !bD) dir =Direction.U;
		else if(!bL && bU && bR && !bD) dir =Direction.RU;
		else if(!bL && !bU && bR && !bD) dir =Direction.R;
		else if(!bL && !bU && bR && bD) dir =Direction.RD;
		else if(!bL && !bU && !bR && bD) dir =Direction.D;
		else if(bL && !bU && !bR && bD) dir =Direction.LD;
		else if(!bL && !bU && !bR && !bD) dir =Direction.STOP;
	}
	
	public void move() {
		oldX = x;
		oldY = y;
		
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
		case STOP:			
			break;
		}
		
		if(this.dir != Direction.STOP) {
			this.ptDir = this.dir;
		}
		
		//Avoid the tank out of bound
		if (x < 0) x = 0;
		if (y < 25) y = 25;
		if (x + Tank.WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - Tank.WIDTH;
		if (y + Tank.HEIGHT > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - Tank.HEIGHT;
	
		if (!good) {
			Direction[] dirs = Direction.values();	
			if (step == 0) {
				int rn = r.nextInt(dirs.length);				
				dir = dirs[rn];
				step = r.nextInt(12) + 3;
			}	
			step --;
			
			if(r.nextInt(40) > 37) this.fire();
		}
	}
	
	public void stay() {
		x = oldX;
		y = oldY;
	}
	
	//Create one new bullet every time tank fires
	private Missile fire() {
		if(!live) return null;		
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y, good, ptDir , this.tc);
		tc.missiles.add(m);
		return m;
	}
	
	private Missile fire(Direction dir) {
		if(!live) return null;		
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y, good, dir , this.tc);
		tc.missiles.add(m);
		return m;
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
	
	
	public boolean isGood() {
		return good;
	}
	
	//Check if a tank hit the wall
	public boolean collidesWithWall(Wall w) {
		if(this.live && this.getRect().intersects(w.getRect())) {
			//If it is, put it into the last location
			this.stay();
			return true;
		}
		return false;
	}
	
	//Check if the tank hit each other
	public boolean collidesWithTanks(java.util.List<Tank> tanks) {
		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);		
			if (this != t) {
				if(this.live && t.isLive() && this.getRect().intersects(t.getRect())) {
					this.stay();
					t.stay();
					return true;
				}
			}		
		}
		return false;
	}
	
	//superFire, open fire to all 8 directions
	private void superFire() { 
		Direction[] dirs = Direction.values();
		for (int i = 0; i < 8; i++) { 
			fire(dirs[i]);
		}
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public class BloodBar {
		public void draw(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x, y - 10, WIDTH, 10);
			int w = WIDTH * life/LIFE ;
			g.fillRect(x, y - 10, w, 10);
			g.setColor(c);
		}
	}
	
	public boolean eat(Blood b) {
		if(this.live && b.isLive() && this.getRect().intersects(b.getRect())) {
			b.setLive(false);
			life = LIFE;
			return true;
		}
		return false;
	}
}
