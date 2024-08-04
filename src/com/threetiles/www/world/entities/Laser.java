package com.threetiles.www.world.entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import com.threetiles.www.Resources;
import com.threetiles.www.util.Vec2;

public class Laser extends Bullet {

	public Laser(Entity owner, Vec2 position, Vec2 direction) {
		super(owner, position, direction);
	}

	public Laser(Entity owner, Vec2 position, Vec2 direction, float speed) {
		super(owner, position, direction);
		this.speed = speed;
	}

	public void onHitWorld() {
		world.remove(this);
		world.add(new Poof(getCenterPos(), Color.white));
		Resources.play(1);
	}

	public void onHitPlayer() {
		Player player = world.getPlayer();
		if (player.getScore() < 150) {
			player.addScore((float) -(3.0f + Math.random() * 2 - 1));
		} else {
			player.addScore((float) (3.0f + Math.random() * 2 - 1));
		}
		player.getCamera().addShake(3f);
		world.remove(this);
		world.add(new Poof(getCenterPos(), Color.red));
		Resources.play(1);
	}

	public Vec2 getCenterPos() {
		return new Vec2(position.getX() + 5, position.getY() + 5);
	}

	public void render(GameContainer gc, StateBasedGame game, Graphics g) {
		g.setColor(Color.white);
		g.drawRect(position.getX(), position.getY(), 10, 10);
	}

}
