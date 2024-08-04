package com.threetiles.www.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import com.threetiles.www.Resources;
import com.threetiles.www.world.World;

public class Play extends BasicGameState {

	private int id;
	private World world;

	public Play(int id) {
		this.id = id;
	}

	public void init(GameContainer gc, StateBasedGame game) throws SlickException {

	}

	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
		world.render(gc, game, g);
	}

	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
		world.update(gc, game, delta);
		Input in = gc.getInput();
		if (in.isKeyDown(Input.KEY_ESCAPE)) {
			world.finish();
			((GameOver) game.getState(5)).set(world.getPlayer());
			game.enterState(5);
			Resources.play(2);
		}
	}

	public int getID() {
		return id;
	}

	public void setWorld(World world) throws SlickException {
		this.world = world;
		this.world.reset();
	}

}
