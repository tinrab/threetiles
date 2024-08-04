package com.threetiles.www.states;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ResourceLoader;
import com.threetiles.www.Logger;
import com.threetiles.www.Resources;
import com.threetiles.www.util.MathX;
import com.threetiles.www.world.World;

public class LevelSelect extends BasicGameState {

	private int id;

	private int selected;
	//format:off
	private static String[] levels = new String[] { 
		"Invasion.tmx", 
		"Fall.tmx", 
		"LD26.tmx", 
		"Maze.tmx", 
		"RedParty.tmx", 
		"Climb.tmx", 
		"Attack.tmx", 
		"MegaMaze.tmx", 
		"Mixture.tmx",
		"Confusion.tmx",
		"Dice.tmx",
		"Quick.tmx",
		"TwoColor.tmx",
		"Epic.tmx",
		"TheEnd.tmx"};
	//format:on
	private List<World> worlds = new ArrayList<World>();
	public static List<String> NAMES = new ArrayList<String>();
	public static List<Double> TIMES = new ArrayList<Double>();
	public static List<Integer> TRACKS = new ArrayList<Integer>();

	public LevelSelect(int id) {
		this.id = id;
	}

	public void init(GameContainer gc, StateBasedGame game) throws SlickException {
		for (String file : levels) {
			String name = file.substring(0, file.indexOf("."));
			worlds.add(new World("maps/" + file, Character.toUpperCase(name.charAt(0)) + name.substring(1)));
			TRACKS.add(new Random().nextInt(3));
			NAMES.add(name);
		}

		DataInputStream in = null;
		in = new DataInputStream(ResourceLoader.getResourceAsStream("maps/times"));
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		try {
			String line;
			int i = 0;
			while ((line = br.readLine()) != null) {
				TIMES.add(Double.parseDouble(line.substring(line.indexOf(": ") + 2)));
				i++;
			}
			br.close();
		} catch (Exception e) {
			Logger.log(e);
		}
	}

	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
		String s = "./Select world: ";
		String s2 = "Pr3ss 'ENTER' to play.";
		int sw = Resources.BITFONT.getWidth(s);
		int sw2 = Resources.BITFONT.getWidth(s2);
		g.scale(2, 2);
		Resources.BITFONT.drawString((gc.getWidth() / 2 - sw) / 2f, 15, s);
		Resources.BITFONT.drawString((gc.getWidth() / 2 - sw2) / 2f, 240, s2);
		g.resetTransform();

		g.setColor(Color.lightGray);
		g.drawRect(50, 80, 600, 370);
		int tw = gc.getWidth() - 730;
		g.drawRect(680, 80, tw, 100);
		int x = 70;
		int y = 100;
		for (int i = 0; i < worlds.size(); i++) {
			if (selected == i) {
				g.setColor(Color.darkGray);
				g.fillRect(x, y, 160, 40);
			}
			String name = NAMES.get(i);
			Resources.BITFONT.drawString(x + 80 - Resources.BITFONT.getWidth(name) / 2, y + 12, name);
			g.setColor(Color.white);
			g.drawRect(x, y, 160, 40);
			y += 55;
			if ((i + 1) % 6 == 0) {
				x += 180;
				y = 100;
			}
		}

		String best = "Best time:";
		Resources.BITFONT.drawString(680 + tw / 2 - Resources.BITFONT.getWidth(best) / 2, 105, best);
		if (TIMES.get(selected) == -1) Resources.BITFONT.drawString(680 + tw / 2 - Resources.BITFONT.getWidth("Not played") / 2, 135, "Not played");
		else {
			double t = TIMES.get(selected) / 1000.0;
			String time = t + "s";
			Resources.BITFONT.drawString(680 + tw / 2 - Resources.BITFONT.getWidth(time) / 2, 135, time);
		}
	}

	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
		Input in = gc.getInput();
		if (in.isKeyPressed(Input.KEY_UP) || in.isKeyPressed(Input.KEY_W)) {
			if (selected > 0) Resources.play(4);
			selected--;
		}
		if (in.isKeyPressed(Input.KEY_DOWN) || in.isKeyPressed(Input.KEY_S)) {
			if (selected < worlds.size() - 1) Resources.play(4);
			selected++;
		}
		selected = MathX.clamp(selected, 0, worlds.size() - 1);

		if (in.isKeyPressed(Input.KEY_ENTER)) {
			Resources.MENU.stop();
			Resources.playTrack(selected);
			Resources.play(5);
			worlds.get(selected).reset();
			((Play) game.getState(3)).setWorld(worlds.get(selected));
			game.enterState(3);
			selected = 0;
		}

		Resources.updateVolume(gc);
	}

	public int getID() {
		return id;
	}

}
