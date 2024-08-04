package com.threetiles.www.world.entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import com.threetiles.www.Resources;
import com.threetiles.www.util.Vec2;

public class Bomb extends Bullet {

	public Bomb(Entity owner, Vec2 position, Vec2 direction, float speed) {
		super(owner, position, direction);
		this.speed = speed;
	}

	public void onHitWorld() {
		world.add(new Boom(getCenterPos()));
		world.remove(this);
		Resources.play(7);
	}

	public void onHitPlayer() {
		Player player = world.getPlayer();
		if (player.getScore() < 150) {
			player.addScore((float) -(12.0f + Math.random() * 4 - 2));
		} else {
			player.addScore((float) (12.0f + Math.random() * 4 - 2));
		}
		player.getCamera().addShake(3f);
		world.remove(this);
		Resources.play(7);
	}

	public Vec2 getCenterPos() {
		return new Vec2(position.getX() + 5, position.getY() + 5);
	}

	public void render(GameContainer gc, StateBasedGame game, Graphics g) {
		g.setColor(Color.red);
		g.drawOval(position.getX(), position.getY(), 10, 10);
	}

}
