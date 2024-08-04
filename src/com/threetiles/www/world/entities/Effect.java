package com.threetiles.www.world.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import com.threetiles.www.util.Vec2;

public abstract class Effect extends Entity {

	public Effect(Vec2 position) {
		super(Type.EFFECT, position);
	}

	public abstract void render(GameContainer gc, StateBasedGame game, Graphics g);

	public abstract void update(GameContainer gc, StateBasedGame game, int delta);

}
