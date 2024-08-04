package com.threetiles.www.world.entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import com.threetiles.www.util.Vec2;

public class Boom extends Effect {

	private float scale = 0.0f;

	public Boom(Vec2 position) {
		super(position);
	}

	public void render(GameContainer gc, StateBasedGame game, Graphics g) {
		g.setColor(Color.red);
		float p = 360 / 8.0f;
		for (int i = 0; i < 8; i++) {
			g.drawArc(position.getX() - scale / 2f, position.getY() - scale / 2f, scale, scale, i * p + 15, p * (i + 1) - 15);
		}
	}

	public void update(GameContainer gc, StateBasedGame game, int delta) {
		scale += 0.3f * delta;
		if (scale > 100) world.remove(this);
	}

}
