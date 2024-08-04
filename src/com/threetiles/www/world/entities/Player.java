package com.threetiles.www.world.entities;

import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;
import com.threetiles.www.Resources;
import com.threetiles.www.states.GameOver;
import com.threetiles.www.states.Win;
import com.threetiles.www.util.Camera;
import com.threetiles.www.util.MathX;
import com.threetiles.www.util.Vec2;
import com.threetiles.www.util.Vec3i;

public class Player extends Mob {

	private Camera camera;
	private Animation animation;
	private float score;
	List<Vec3i> near = new ArrayList<Vec3i>();

	private static Color shield = new Color(0.3f, 0.6f, 1.0f, 0.3f);
	private static Color shield2 = new Color(0.3f, 0.6f, 1.0f, 0.5f);

	public Player(float x, float y) {
		super(new Vec2(x, y));
		score = 150;
		speed = 0.3f;
		camera = new Camera();
		Image player = Resources.get("gfx/player.png");
		Image[] images = new Image[4];
		images[0] = player.getSubImage(0, 0, 16, 16);
		images[1] = player.getSubImage(16, 0, 16, 16);
		images[2] = player.getSubImage(48, 0, 16, 16);
		images[3] = player.getSubImage(32, 0, 16, 16);
		animation = new Animation(images, 120);
	}

	public void render(GameContainer gc, StateBasedGame game, Graphics g) {
		for (Vec3i v : near) {
			Color col = world.getTileImage(v.getX(), v.getY(), 0).getColor(1, 1);
			float sx = position.getX() + 24;
			float sy = position.getY() + 24;
			float ex = v.getX() * 48 + 24;
			float ey = v.getY() * 48 + 24;
			g.setColor(new Color(col.r, col.g, col.b, 0.2f));
			g.setLineWidth(4);
			g.drawLine(sx, sy, ex, ey);
			g.setColor(new Color(col.r, col.g, col.b, 0.5f));
			g.setLineWidth(2);
			g.drawLine(sx + 1, sy + 1, ex + 1, ey + 1);
		}
		g.pushTransform();
		g.scale(3, 3);
		g.drawImage(animation.getCurrentFrame(), position.getX() / 3f, position.getY() / 3f);
		g.popTransform();

		float offset = Math.abs(score - 150);
		float p = (150 - offset) / 150;
		if (p > 0.95) {
			g.setColor(shield2);
			g.setLineWidth(4);
		} else {
			g.setColor(shield);
			g.setLineWidth(2);
		}
		g.drawArc(position.getX() - 50 + 24, position.getY() - 50 + 24, 100, 100, 0, 360 * p);
	}

	public void update(GameContainer gc, StateBasedGame game, int delta) {
		isMoving = false;
		if (score <= 0 || score >= 300) {
			world.finish();
			((GameOver) game.getState(5)).set(this);
			game.enterState(5);
			Resources.play(8);
			return;
		}
		animation.update(delta);
		Input in = gc.getInput();

		if (in.isKeyDown(Input.KEY_A) || in.isKeyDown(Input.KEY_LEFT)) {
			move(-1, 0, delta);
			isMoving = true;
		}
		if (in.isKeyDown(Input.KEY_D) || in.isKeyDown(Input.KEY_RIGHT)) {
			isMoving = true;
			move(1, 0, delta);
		}
		if (in.isKeyDown(Input.KEY_W) || in.isKeyDown(Input.KEY_UP)) {
			isMoving = true;
			move(0, -1, delta);
		}
		if (in.isKeyDown(Input.KEY_S) || in.isKeyDown(Input.KEY_DOWN)) {
			isMoving = true;
			move(0, 1, delta);
		}

		for (Vec3i v : near) {
			float sx = position.getX() + 24;
			float sy = position.getY() + 24;
			float ex = v.getX() * 48 + 24;
			float ey = v.getY() * 48 + 24;
			float m = (float) Math.atan2(sx - ex, sy - ey);
			velocity.translate(-(float) Math.sin(m) * 2, -(float) Math.cos(m) * 2);
		}

		if (velocity.getX() != 0 && velocity.getY() != 0) move(velocity.getX() * 0.05f, velocity.getY() * 0.05f, delta);
		velocity.set(0, 0);

		camera.setTarget(position);
		camera.update();
		camera.transform();

		int x0 = (int) (position.getX() + 24) / 48;
		int y0 = (int) (position.getY() + 24) / 48;

		near.clear();

		for (int x = x0 - 4; x <= x0 + 4; x++) {
			for (int y = y0 - 4; y <= y0 + 4; y++) {
				int dx = x0 - x;
				int dy = y0 - y;
				if (dx * dx + dy * dy < 9) {
					int tile = world.getTileId(x, y);
					if (tile > 3) {
						near.add(new Vec3i(x, y, tile));
						if (tile == 4) score -= 0.008f * delta;
						else if (tile == 5) score += 0.008f * delta;
					}
				}
			}
		}
		score = MathX.clamp(score, 0, 300);

		Vec2 pos = getCenterPos();
		if (world.getTileId((int) (pos.getX() / 48), (int) (pos.getY() / 48)) == 3) {
			float p = (int) (((150 - Math.abs(score - 150)) / 150 * 100) * 100) / 100.0f;
			if (p > 95) {
				world.finish();
				Resources.play(9);
				((Win) game.getState(4)).set(this);
				game.enterState(4);
			}
		}
	}

	public Camera getCamera() {
		return camera;
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

		boolean t1 = world.isSolid(x0, y0);
		boolean t2 = world.isSolid(x1, y0);
		boolean t3 = world.isSolid(x1, y1);
		boolean t4 = world.isSolid(x0, y1);

		if (t1 || t2 || t3 || t4) {
			camera.addShake(0.5f);
			return false;
		}

		return true;
	}

	public float getScore() {
		return score;
	}

	public void addScore(float score) {
		this.score += score;
		this.score = MathX.clamp(this.score, 0, 300);
	}

	public Vec2 getCenterPos() {
		return new Vec2(position.getX() + 24, position.getY() + 24);
	}

}
