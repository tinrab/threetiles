package com.threetiles.www.gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import com.threetiles.www.Resources;

public class Button extends Element {

	private String text;
	private float s = 4;

	public Button(String text, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.text = text;
	}

	public void render(GameContainer gc, StateBasedGame game, Graphics g) {
		g.scale(2, 2);
		g.drawImage(Resources.BUTTON[0], x / 2f, y / 2f);
		g.resetTransform();
		g.setFont(Resources.BITFONT);
		g.scale(s, s);
		float tw = g.getFont().getWidth(text) * s;
		g.drawString(text, (x + width / 2 - tw / 2) / s, (y + 4) / s);
		g.resetTransform();
	}

}
