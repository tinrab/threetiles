package com.threetiles.www.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import com.threetiles.www.Resources;
import com.threetiles.www.gui.Button;
import com.threetiles.www.gui.Element;

public class Menu extends BasicGameState {

	private int id;

	private Element[] buttons;
	private int selected;

	private Color red = new Color(1.0f, 0.2f, 0.2f);
	private Color green = new Color(0.2f, 1.0f, 0.2f);

	public Menu(int id) {
		this.id = id;
	}

	public void init(GameContainer gc, StateBasedGame game) throws SlickException {
		buttons = new Element[3];
		buttons[0] = new Button("Start", gc.getWidth() / 2 - 150, 260, 300, 64);
		buttons[1] = new Button("Tutorial", gc.getWidth() / 2 - 150, 340, 300, 64);
		buttons[2] = new Button("Exit", gc.getWidth() / 2 - 150, 420, 300, 64);
	}

	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
		if (!Resources.loaded()) {
			String s = "Loading...";
			g.scale(2, 2);
			Resources.BITFONT.drawString((gc.getWidth() / 2 - g.getFont().getWidth(s) / 2) / 2f, 80, s);
			g.resetTransform();
			return;
		}
		g.scale(4, 4);
		g.drawImage(Resources.TITLE, (gc.getWidth() / 2 - 296) / 4f, 50 / 4f);
		g.resetTransform();
		g.scale(2, 2);
		g.setColor(Color.white);
		String sub = "./minimalistic";
		Resources.BITFONT.drawString((gc.getWidth() / 2 - Resources.BITFONT.getWidth(sub)) / 2f, 80, sub);
		g.resetTransform();
		String copy = "Made by @PaidGEEK";
		Resources.BITFONT.drawString(gc.getWidth() / 2 - Resources.BITFONT.getWidth(copy) / 2, 510, copy);

		g.setLineWidth(1);
		for (int i = 0; i < buttons.length; i++) {
			Element b = buttons[i];
			b.render(gc, game, g);
			if (selected == i) {
				g.scale(2, 2);
				Resources.BUTTON[1].draw((b.getX() - 64) / 2f, b.getY() / 2f, (selected < 2) ? green : red);
				Resources.BUTTON[2].draw((b.getX() + b.getWidth()) / 2f, b.getY() / 2f, (selected < 2) ? green : red);
				g.resetTransform();
			}
		}
		Resources.BITFONT.drawString(40, 500, "Volume '+'/'-'");
	}

	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
		if (!Resources.loaded()) {
			Resources.load();
			Resources.MENU.playAsMusic(1, 1, false);
			return;
		}
		if (!Resources.MENU.isPlaying()) Resources.MENU.playAsMusic(1, 1, false);
		Input in = gc.getInput();
		if (in.isKeyPressed(Input.KEY_UP) || in.isKeyPressed(Input.KEY_W)) {
			Resources.play(4);
			selected--;
		}
		if (in.isKeyPressed(Input.KEY_DOWN) || in.isKeyPressed(Input.KEY_S)) {
			Resources.play(4);
			selected++;
		}
		if (selected < 0) selected = 2;
		else if (selected > 2) selected = 0;

		if (in.isKeyPressed(Input.KEY_ENTER)) {
			Resources.play(5);
			if (selected == 0) {
				game.enterState(2);
			} else if (selected == 1) {
				Resources.MENU.stop();
				((Tutorial) game.getState(1)).init(gc, game);
				game.enterState(1);
			} else if (selected == 2) {
				gc.exit();
			}
			selected = 0;
		}
		Resources.updateVolume(gc);
	}

	public int getID() {
		return id;
	}

}
