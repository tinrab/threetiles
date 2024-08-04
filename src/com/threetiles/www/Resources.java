package com.threetiles.www;

import java.io.IOException;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;
import com.threetiles.www.states.LevelSelect;

public class Resources {

	private static boolean loaded;

	public static AngelCodeFont BITFONT;
	static {
		try {
			BITFONT = new AngelCodeFont("font/font.fnt", new Image("font/font.png", false, Image.FILTER_NEAREST));
		} catch (SlickException e) {
			Logger.log(e);
		}
	}
	public static Image SCORE;
	public static Image[] BUTTON = new Image[3];
	public static Image TITLE;
	public static Image BACKGROUND;

	public static Audio[] SOUNDS = new Audio[12];
	public static Audio MENU;
	public static Audio[] SOUNDTRACK = new Audio[3];

	public static void load() throws SlickException {
		BUTTON[0] = new Image("gfx/button.png", false, Image.FILTER_NEAREST);
		BUTTON[1] = new Image("gfx/pointer.png", false, Image.FILTER_NEAREST);
		BUTTON[2] = new Image("gfx/pointer.png", false, Image.FILTER_NEAREST);
		BUTTON[2].rotate(180);

		TITLE = new Image("gfx/title.png", false, Image.FILTER_NEAREST);
		SCORE = new Image("gfx/score.png", false, Image.FILTER_NEAREST);
		BACKGROUND = new Image("gfx/background.png");

		try {
			for (int i = 0; i < SOUNDS.length; i++)
				SOUNDS[i] = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("sounds/clip" + (i + 1) + ".wav"));
			MENU = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("sounds/menu.wav"));
			for (int i = 0; i < SOUNDTRACK.length; i++) {
				SOUNDTRACK[i] = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("sounds/track" + (i + 1) + ".ogg"));
			}
		} catch (IOException e) {
			Logger.log(e);
		}

		loaded = true;
	}

	public static void play(int id) {
		SOUNDS[id].playAsSoundEffect(1, 1, false);
	}

	public static Image get(String path) {
		try {
			return new Image(path, false, Image.FILTER_NEAREST);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void playTrack(int id) {
		for (int i = 0; i < SOUNDTRACK.length; i++)
			if (SOUNDTRACK[i].isPlaying()) SOUNDTRACK[i].stop();
		Resources.SOUNDTRACK[LevelSelect.TRACKS.get(id)].playAsMusic(1, 1, false);
	}

	public static void updateVolume(GameContainer gc) {
		if (gc.getInput().isKeyPressed(78)) {
			float v = gc.getMusicVolume();
			v += 0.1f;
			if (v > 1) v = 1;
			gc.setMusicVolume(v);
			v = gc.getSoundVolume();
			v += 0.1f;
			if (v > 1) v = 1;
			gc.setSoundVolume(v);
		}
		if (gc.getInput().isKeyPressed(74)) {
			float v = gc.getMusicVolume();
			v -= 0.1f;
			if (v < 0) v = 0;
			gc.setMusicVolume(v);
			v = gc.getSoundVolume();
			v -= 0.1f;
			if (v < 0) v = 0;
			gc.setSoundVolume(v);
		}
	}

	public static boolean loaded() {
		return loaded;
	}

}
