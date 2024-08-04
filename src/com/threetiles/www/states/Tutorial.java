package com.threetiles.www.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import com.threetiles.www.Resources;
import com.threetiles.www.world.World;

public class Tutorial extends BasicGameState {

	private int id;
	private World world;

	private int state = -1;
	private long last;
	//format:off
	private String[] text  = new String[] {
		"Welcom3 victim.",
		"/.To pass a level you must fly over a y3llow tile.",
		"In order to finish your energy meter must be at least 95% full.",
		"Red/Green tiles will increase/decrease your energy.",
		"If your energy becomes too low or too high, you will di3.",
		"./Have f%&.",
		""
	};
	
	private int[] times = new int[] {
		1000,
		3000,
		5000,
		5000,
		5000,
		5000,
		4000
	};
	//format:on

	public Tutorial(int id) {
		this.id = id;
	}

	public void init(GameContainer gc, StateBasedGame game) throws SlickException {
		world = new World("tutorial.tmx", "Tutorial");
		state = -1;
		world.reset();
		world.getPlayer().disable(24000);
		last = System.currentTimeMillis();
	}

	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
		world.render(gc, game, g);
		if (state >= 0 && state < 6) {
			g.setLineWidth(1);
			String s = text[state];
			int tw = Resources.BITFONT.getWidth(s);
			g.setColor(new Color(0, 0, 0, 0.8f));
			g.fillRect(gc.getWidth() / 2 - tw / 2 - 50, 90, tw + 100, 40);
			g.setColor(new Color(1, 1, 1, 0.3f));
			g.drawRect(gc.getWidth() / 2 - tw / 2 - 50, 90, tw + 100, 40);
			Resources.BITFONT.drawString(gc.getWidth() / 2 - tw / 2, 103, s);
		}
	}

	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
		world.update(gc, game, delta);
		if (state < text.length - 1) {
			if (System.currentTimeMillis() > last + times[state + 1]) {
				last = System.currentTimeMillis();
				state++;
			}
		}
		if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
			game.enterState(0);
		}
	}

	public int getID() {
		return id;
	}

}
