package com.threetiles.www.world.entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import com.threetiles.www.util.Vec2;

public class Poof extends Effect {

	private float scale = 0.0f;
	private Color color;

	public Poof(Vec2 position, Color color) {
		super(position);
		this.color = color;
	}

	public void render(GameContainer gc, StateBasedGame game, Graphics g) {
		g.setColor(color);
		g.drawRect(position.getX() - scale / 2f, position.getY() - scale / 2f, scale, scale);
	}

	public void update(GameContainer gc, StateBasedGame game, int delta) {
		scale += 0.4f * delta;
		if (scale > 60) world.remove(this);
	}

}
