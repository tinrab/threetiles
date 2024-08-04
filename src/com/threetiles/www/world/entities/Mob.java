package com.threetiles.www.world.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import com.threetiles.www.util.Vec2;

public abstract class Mob extends Entity {

	protected float speed = 1.0f;
	protected Vec2 velocity = new Vec2();
	protected boolean isMoving;
	protected boolean disabled;
	protected long time, duration;

	public Mob(Vec2 position) {
		super(Type.MOB, position);
	}

	public void move(float dx, float dy, int delta) {
		if (System.currentTimeMillis() > time + duration) {
			disabled = false;
		}
		if (disabled) return;
		if (dx != 0 && dy != 0) {
			move(dx, 0, delta);
			move(0, dy, delta);
			return;
		}

		dx = dx * speed * delta;
		dy = dy * speed * delta;

		if (canMoveTo(dx, dy)) {
			position.translate(dx, dy);
		}
	}

	public void disable(long time) {
		disabled = true;
		this.time = System.currentTimeMillis();
		duration = time;
	}

	public boolean isMoving() {
		return isMoving;
	}

	public abstract Vec2 getCenterPos();

	public abstract boolean canMoveTo(float dx, float dy);

	public abstract void render(GameContainer gc, StateBasedGame game, Graphics g);

}
