package com.threetiles.www.states;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import com.threetiles.www.Logger;
import com.threetiles.www.Resources;
import com.threetiles.www.world.entities.Player;

public class Win extends BasicGameState {

	private int id;
	private Player player;

	private long newRecord = -1;

	public Win(int id) {
		this.id = id;
	}

	public void set(Player player) {
		this.player = player;
	}

	public void init(GameContainer gc, StateBasedGame game) throws SlickException {}

	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
		g.scale(4, 4);
		Resources.BITFONT.drawString((gc.getWidth() / 2 - Resources.BITFONT.getWidth("Level Complete!") * 2) / 4f, 11, "Level Complete!", Color.red);
		g.resetTransform();

		if (player.getWorld().getName().equals("Tutorial")) {
			String yay = "You have successfully completed tutorial level!";
			Resources.BITFONT.drawString(gc.getWidth() / 2 - Resources.BITFONT.getWidth(yay) / 2, 170, yay);
		} else {
			String name = "Level name: " + player.getWorld().getName();
			Resources.BITFONT.drawString(gc.getWidth() / 2 - Resources.BITFONT.getWidth(name) / 2, 170, name);

			double t = (player.getWorld().getTimeTook()) / 1000.0;
			String time = "Time: " + t + "s";
			Resources.BITFONT.drawString(gc.getWidth() / 2 - Resources.BITFONT.getWidth(time) / 2, 210, time);
			double currentRecord = LevelSelect.TIMES.get(LevelSelect.NAMES.indexOf(player.getWorld().getName()));

			if (currentRecord != -1) {
				double r = (currentRecord) / 1000.0;
				String best = "Best time: " + r + "s";
				Resources.BITFONT.drawString(gc.getWidth() / 2 - Resources.BITFONT.getWidth(best) / 2, 230, best);
			} else {
				newRecord = player.getWorld().getTimeTook();
				g.scale(2, 2);
				String record = "New record!";
				Resources.BITFONT.drawString((gc.getWidth() / 2 - Resources.BITFONT.getWidth(record)) / 2f, 150, record, Color.red);
				g.resetTransform();
			}
		}

		g.scale(2, 2);
		int bx = (gc.getWidth() / 2 - 150);
		int by = 225;
		g.drawImage(Resources.BUTTON[0], bx / 2f, by);
		g.drawImage(Resources.BUTTON[1], (bx - 64) / 2f, by, new Color(0.2f, 1.0f, 0.2f));
		g.drawImage(Resources.BUTTON[2], (bx + 300) / 2f, by, new Color(0.2f, 1.0f, 0.2f));
		g.resetTransform();

		g.setFont(Resources.BITFONT);
		g.scale(4, 4);
		float tw = g.getFont().getWidth("Ok") * 4;
		Resources.BITFONT.drawString((gc.getWidth() / 2 - tw / 2) / 4f, 227 / 2f, "Ok");
		g.resetTransform();
	}

	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			player.getWorld().reset();
			if (!player.getWorld().getName().equals("Tutorial")) {
				//save new time
				if (newRecord != -1) {
					LevelSelect.TIMES.set(LevelSelect.NAMES.indexOf(player.getWorld().getName()), (double) newRecord);
					//write to file
					try {
						FileWriter fw = new FileWriter(new File("times"));
						BufferedWriter out = new BufferedWriter(fw);
						for (int i = 0; i < LevelSelect.TIMES.size(); i++) {
							String name = LevelSelect.NAMES.get(i);
							double time = LevelSelect.TIMES.get(i);
							out.write(name + ": " + time + ((i == LevelSelect.TIMES.size() - 1) ? "" : "\n"));
						}
						out.close();
					} catch (IOException e) {
						Logger.log(e);
					}
				}
				game.enterState(2);
			} else {
				game.enterState(0);
			}
		}
	}

	public int getID() {
		return id;
	}

}
