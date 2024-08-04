package com.threetiles.www.world.entities;

import java.util.Random;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.pathfinding.Path;
import com.threetiles.www.Resources;
import com.threetiles.www.util.MathX;
import com.threetiles.www.util.Vec2;

public class Drone extends Mob {

	private static Random rand = new Random();
	private static Image[] images = new Image[4];
	static {
		Image drone = Resources.get("gfx/drone.png");
		images[0] = drone.getSubImage(0, 0, 16, 16);
		images[1] = drone.getSubImage(16, 0, 16, 16);
		images[2] = drone.getSubImage(48, 0, 16, 16);
		images[3] = drone.getSubImage(32, 0, 16, 16);
	}

	private Animation animation;
	private long last;
	private Vec2 lastStart = new Vec2();
	private long lastSearch;
	private long found;
	private Path path;
	private int i;

	public Drone(float x, float y) {
		super(new Vec2(x, y));
		speed = 0.15f;
		animation = new Animation(images, 120);
	}

	public boolean canMoveTo(float dx, float dy) {
		int x0 = (int) (position.getX() + dx + 6);
		int y0 = (int) (position.getY() + dy + 6);
		int x1 = x0 + 48 - 12;
		int y1 = y0 + 48 - 12;

		x0 /= 48;
		x1 /= 48;
		y0 /= 48;
		y1 /= 48;

		boolean t1 = world.getTileId(x0, y0, 0) > 2;
		boolean t2 = world.getTileId(x1, y0, 0) > 2;
		boolean t3 = world.getTileId(x1, y1, 0) > 2;
		boolean t4 = world.getTileId(x0, y1, 0) > 2;

		if (t1 || t2 || t3 || t4) return false;
		return true;
	}

	public void render(GameContainer gc, StateBasedGame game, Graphics g) {
		g.pushTransform();
		g.scale(3, 3);
		g.drawImage(animation.getCurrentFrame(), position.getX() / 3f, position.getY() / 3f);
		g.popTransform();
	}

	public void update(GameContainer gc, StateBasedGame game, int delta) {
		if (position.distance(world.getPlayer().getPosition()) < 20000) {
			if (found == 0) found = System.currentTimeMillis();
			if (System.currentTimeMillis() > found + 3000 && ((path == null) ? true : path.getLength() < 5)) {
				int n = 32;
				for (int i = 0; i < n; i++) {
					float a = (float) ((2 * Math.PI) / n * (i + 1));
					Vec2 dir = new Vec2((float) Math.sin(a), (float) Math.cos(a));
					world.add(new Laser(this, new Vec2(position.getX() + 21.5f, position.getY() + 21.5f), dir, 0.5f));
				}
				world.remove(this);
				Resources.play(10);
				return;
			}
		}
		if (world.getPlayer() != null) {
			if (!(lastStart.equals(world.getPlayer().getPosition())) && System.currentTimeMillis() > lastSearch + 500) {
				lastSearch = System.currentTimeMillis();
				lastStart.set(world.getPlayer().getPosition());
				i = 0;
				Vec2 t = world.getPlayer().getCenterPos();
				Vec2 c = getCenterPos();
				path = world.getPath((int) (c.getX() / 48), (int) (c.getY() / 48), (int) (t.getX() / 48), (int) (t.getY() / 48), 15);
			} else if (path != null) {
				float tx = (path.getStep(i).getX() + 0.5f) * 48.0f;
				float ty = (path.getStep(i).getY() + 0.5f) * 48.0f;
				float dx = tx - getCenterPos().getX();
				float dy = ty - getCenterPos().getY();
				if (dx * dx + dy * dy < 1000) i++;
				i = MathX.clamp(i, 0, path.getLength() - 1);
				float angle = (float) Math.atan2(dx, dy);
				position.translate((float) Math.sin(angle) * speed * delta, (float) Math.cos(angle) * speed * delta);
			}

			if (System.currentTimeMillis() > last + 100 + (rand.nextInt(100)) && ((path == null) ? false : path.getLength() < 12)) {
				last = System.currentTimeMillis();
				Vec2 p = world.getPlayer().getPosition();
				float dx = position.getX() - p.getX();
				float dy = position.getY() - p.getY();

				float angle = (float) (Math.atan2(dx, dy) + Math.random() * 0.5 - 0.25);
				if (dx * dx + dy * dy < 400000) {
					world.add(new Laser(this, new Vec2(position.getX() + 21.5f, position.getY() + 21.5f), new Vec2((float) -Math.sin(angle), (float) -Math.cos(angle))));
					Resources.play(0);
				}
			}
		}
		animation.update(delta);

	}

	public Vec2 getCenterPos() {
		return new Vec2(position.getX() + 24, position.getY() + 24);
	}

}
