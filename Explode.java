package tankWar;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Explode {

	int x, y;
	private TankClient tc;

	private boolean live = true;

	// check if the image has been loaded
	private boolean init = false;

	// Toolkit can retrieve information of disk easily
	private static Toolkit tk = Toolkit.getDefaultToolkit();

	private static Image[] imgs = { tk.getImage(Explode.class.getClassLoader().getResource("images/1.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/2.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/3.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/4.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/5.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/6.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/7.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/8.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/9.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/10.gif")) };

	// show which step the explosion has been at
	int step = 0;

	public Explode(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}

	public void draw(Graphics g) {

		if (!init) {
			for (int i = 0; i < imgs.length; i++) {
				// Draw the explosion image where invisible
				g.drawImage(imgs[i], -100, -100, null);
			}
			init = true;
		}

		if (!live) {
			tc.explodes.remove(this);
			return;
		}

		if (step == imgs.length) {
			live = false;
			step = 0;
			return;
		}

		g.drawImage(imgs[step], x, y, null);

		step++;

	}

}