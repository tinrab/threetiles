package com.threetiles.www.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;
import com.threetiles.www.Resources;
import com.threetiles.www.util.Camera;

public class Starfield {

	private static Image[] images = new Image[4];
	static {
		Image img = Resources.get("gfx/stars.png");
		for (int i = 0; i < 4; i++) {
			images[i] = img.getSubImage(i * 16, 0, 16, 16);
		}
	}

	private List<Star> stars;
	private Random random;
	private int width;
	private int height;

	public Starfield(int width, int height) {
		this.width = width;
		this.height = height;
		random = new Random();
		stars = new ArrayList<Star>();

		for (int i = 0; i < (width * height) / 8000; i++) {
			float x = random.nextFloat() * (height * 2) - width / 2;
			float y = random.nextFloat() * (height * 2) - height / 2;
			stars.add(new Star(x, y, random.nextInt(3) + 1, random.nextInt(4), .95f + random.nextFloat() * .1f));
		}
	}

	public void update(GameContainer gc, StateBasedGame game, int delta) {
		for (Star s : stars) {
			s.y -= -0.03f * (float) delta * s.speed;
			if (s.y > height * 1.5) {
				s.y = -random.nextFloat() * (height * 2) - height / 2;
				s.x = random.nextFloat() * (height * 2) - width / 2;
			}
		}
	}

	public void render(GameContainer gc, StateBasedGame game, Graphics g, Camera c) {
		for (Star s : stars) {
			g.drawImage(images[s.type], s.x - c.getX() - (2 << s.layer), s.y - c.getY() - (2 << s.layer), new Color(1, 1, 1, (3 - s.layer) / 3f));
		}
	}

	private class Star {

		public float x, y, speed;
		public int layer, type;

		public Star(float x, float y, int layer, int type, float speed) {
			this.x = x;
			this.y = y;
			this.speed = speed;
			this.layer = layer;
			this.type = type;
		}
	}

}
