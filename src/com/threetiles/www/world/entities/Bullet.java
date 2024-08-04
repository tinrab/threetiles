package com.threetiles.www.world.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import com.threetiles.www.util.Vec2;

public abstract class Bullet extends Entity {

	protected float speed = 1.0f;
	protected Vec2 direction;
	protected Entity owner;

	public Bullet(Entity owner, Vec2 position, Vec2 direction) {
		super(Type.BULLET, position);
		this.direction = direction;
		this.owner = owner;
	}

	public abstract void onHitWorld();

	public abstract void onHitPlayer();

	public abstract Vec2 getCenterPos();

	public void update(GameContainer gc, StateBasedGame game, int delta) {
		position.translate(direction.getX() * speed * delta, direction.getY() * speed * delta);

		float dx = owner.getPosition().getX() - position.getX();
		float dy = owner.getPosition().getY() - position.getY();

		Vec2 center = getCenterPos();
		int tx = (int) (center.getX()) / 48;
		int ty = (int) (center.getY()) / 48;

		if (world.getTileId(tx, ty) > 2) {
			onHitWorld();
			return;
		}

		if (world.getPlayer() != null) {
			float px = world.getPlayer().getPosition().getX() + 24;
			float py = world.getPlayer().getPosition().getY() + 24;
			dx = position.getX() - px;
			dy = position.getY() - py;

			if (dx * dx + dy * dy < 1681) {
				onHitPlayer();
			}
		}
	}

	public abstract void render(GameContainer gc, StateBasedGame game, Graphics gf);

}
