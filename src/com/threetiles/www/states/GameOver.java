package com.threetiles.www.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import com.threetiles.www.Resources;
import com.threetiles.www.world.entities.Player;

public class GameOver extends BasicGameState {

	private int id;

	private Player player;

	public GameOver(int id) {
		this.id = id;
	}

	public void set(Player player) {
		this.player = player;
	}

	public void init(GameContainer gc, StateBasedGame game) throws SlickException {}

	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
		g.scale(4, 4);
		Resources.BITFONT.drawString((gc.getWidth() / 2 - Resources.BITFONT.getWidth("Game Over!") * 2) / 4f, 30, "Game Over!", Color.red);
		g.resetTransform();

		String name = "Level name: " + player.getWorld().getName();
		Resources.BITFONT.drawString(gc.getWidth() / 2 - Resources.BITFONT.getWidth(name) / 2, 200, name);

		g.scale(2, 2);
		int bx = (gc.getWidth() / 2 - 150);
		int by = 200;
		g.drawImage(Resources.BUTTON[0], bx / 2f, by);
		g.resetTransform();

		g.setFont(Resources.BITFONT);
		g.scale(4, 4);
		float tw = g.getFont().getWidth("Ok") * 4;
		Resources.BITFONT.drawString((gc.getWidth() / 2 - tw / 2) / 4f, (by + 2) / 2f, "Ok");
		g.resetTransform();
	}

	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			player.getWorld().reset();
			game.enterState(0);
		}
	}

	public int getID() {
		return id;
	}

}
