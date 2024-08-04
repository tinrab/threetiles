package com.threetiles.www;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;
import com.threetiles.www.states.GameOver;
import com.threetiles.www.states.LevelSelect;
import com.threetiles.www.states.Menu;
import com.threetiles.www.states.Play;
import com.threetiles.www.states.Tutorial;
import com.threetiles.www.states.Win;

public class Game extends StateBasedGame {

	public static final int WIDTH = 900;
	public static final int HEIGHT = WIDTH / 5 * 3;

	public Game(String name) {
		super(name);
		Renderer.setRenderer(Renderer.VERTEX_ARRAY_RENDERER);

		addState(new Menu(0));
		addState(new Tutorial(1));
		addState(new LevelSelect(2));
		addState(new Play(3));
		addState(new Win(4));
		addState(new GameOver(5));
	}

	public void initStatesList(GameContainer gc) throws SlickException {
		enterState(0);
	}

	public static void main(String[] args) {
		Game game = new Game("Thre3 Tiles - LD26");
		try {
			AppGameContainer app = new AppGameContainer(game);
			Log.setLogSystem(new NullLogSystem());
			app.setDisplayMode(WIDTH, HEIGHT, false);
			app.setVSync(true);
			app.setAlwaysRender(true);
			app.setIcons(new String[] { "icon16.png", "icon32.png" });
			app.setShowFPS(false);
			app.start();
		} catch (SlickException e) {
			Logger.log(e);
		}
	}

}
