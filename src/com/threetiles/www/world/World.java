package com.threetiles.www.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.shader.ShaderProgram;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;
import com.threetiles.www.Game;
import com.threetiles.www.Logger;
import com.threetiles.www.Resources;
import com.threetiles.www.states.LevelSelect;
import com.threetiles.www.util.MathX;
import com.threetiles.www.util.TiledMap;
import com.threetiles.www.world.entities.Bomber;
import com.threetiles.www.world.entities.Bullet;
import com.threetiles.www.world.entities.Drone;
import com.threetiles.www.world.entities.Effect;
import com.threetiles.www.world.entities.Entity;
import com.threetiles.www.world.entities.Mob;
import com.threetiles.www.world.entities.Player;

public class World implements TileBasedMap {

	private String name;
	private TiledMap map;

	private List<Entity> entities = new ArrayList<Entity>();
	private Player player;

	private Starfield stars;
	private Image frame;
	private Graphics gf;
	private static ShaderProgram program;
	static {
		try {
			program = ShaderProgram.loadProgram("glow.vs", "glow.fs");
		} catch (SlickException e) {
			Logger.log(e);
		}
	}

	private long start, took;

	public World(String location, String name) throws SlickException {
		map = new TiledMap(ResourceLoader.getResourceAsStream(location));
		this.name = name;
		frame = Image.createOffscreenImage(Game.WIDTH, Game.HEIGHT);
		gf = frame.getGraphics();
	}

	public void finish() {
		took = System.currentTimeMillis() - start;
		start = 0;
	}

	public void reset() {
		player = null;
		entities.clear();
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				if (getTileId(x, y, 1) == 1) {
					set(new Player(x * 48, y * 48));
				}
				int mt = getTileId(x, y, 2);
				if (mt == 1) add(new Drone(x * 48, y * 48));
				else if (mt == 2) add(new Bomber(x * 48, y * 48));
			}
		}
		stars = new Starfield(map.getWidth() * 48, map.getHeight() * 48);
	}

	public void add(Entity entity) {
		if (entity != null) {
			entity.enter(this);
			entities.add(entity);
		}
		Collections.sort(entities);
	}

	public void set(Player player) {
		player.enter(this);
		this.player = player;
	}

	public void remove(Entity entity) {
		entities.remove(entity);
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public void render(GameContainer gc, StateBasedGame game, Graphics g) {
		if (start == 0) start = System.currentTimeMillis();
		gf.setColor(Color.black);
		gf.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		stars.render(gc, game, gf, player.getCamera());
		if (player != null) gf.translate(-player.getCamera().getX(), -player.getCamera().getY());

		//render tiled map
		gf.pushTransform();
		gf.scale(3, 3);
		map.render(0, 0, 0);
		gf.popTransform();

		//render player
		if (player != null) player.render(gc, game, gf);

		//render entities
		for (int i = entities.size() - 1; i >= 0; i--) {
			Entity e = entities.get(i);
			if (e.getType() == Entity.Type.MOB) {
				((Mob) e).render(gc, game, gf);
			} else if (e.getType() == Entity.Type.EFFECT) {
				((Effect) e).render(gc, game, gf);
			} else if (e.getType() == Entity.Type.BULLET) {
				((Bullet) e).render(gc, game, gf);
			}
		}

		gf.flush();
		program.bind();
		g.drawImage(frame, 0, 0);
		program.unbind();
		g.resetTransform();

		g.scale(2, 2);
		g.drawImage(Resources.BACKGROUND, 0, 0);
		g.resetTransform();

		if (!name.equals("Tutorial")) {
			g.scale(4, 4);
			float x = (gc.getWidth() / 2 - 64) / 4f;
			g.drawImage(Resources.SCORE, x, 4);
			double record = LevelSelect.TIMES.get(LevelSelect.NAMES.indexOf(name));
			if (record != -1) {
				g.setLineWidth(4);
				float max = 31.5f;
				float d = (float) ((System.currentTimeMillis() - start) / record);
				g.drawLine(x + 0.5f, 12, x + MathX.clamp(d * max, 0.0f, max), 12);
			}
			g.resetTransform();

			double t = (System.currentTimeMillis() - start) / 1000.0;
			t = ((int) (t * 100)) / 100.0;
			String time = t + "s";
			Resources.BITFONT.drawString(gc.getWidth() / 2 - Resources.BITFONT.getWidth(time) / 2, 25, time);
		}
	}

	public void update(GameContainer gc, StateBasedGame game, int delta) {
		stars.update(gc, game, delta);
		if (player != null) player.update(gc, game, delta);
		for (int i = entities.size() - 1; i >= 0; i--) {
			entities.get(i).update(gc, game, delta);
		}
		Resources.updateVolume(gc);
		SoundStore.get().poll(0);
	}

	public int getTileId(int x, int y) {
		return getTileId(x, y, 0);
	}

	public boolean isSolid(int x, int y) {
		return getTileId(x, y, 0) > 3;
	}

	public int getTileId(int x, int y, int id) {
		if (x >= 0 && x < map.getWidth() && y >= 0 && y < map.getHeight()) return map.getTileId(x, y, id);
		return -1;
	}

	public void setTileId(int x, int y, int id) {
		if (x >= 0 && x < map.getWidth() && y >= 0 && y < map.getHeight()) {
			map.setTileId(x, y, 0, id);
		}
	}

	public Path getPath(int startX, int startY, int endX, int endY, int distance) {
		AStarPathFinder finder = new AStarPathFinder(this, distance, false);
		Path path = finder.findPath(null, startX, startY, endX, endY);
		return path;
	}

	public Player getPlayer() {
		return player;
	}

	public long getTimeTook() {
		return took;
	}

	public String getName() {
		return name;
	}

	public boolean blocked(PathFindingContext p, int x, int y) {
		return isSolid(x, y);
	}

	public float getCost(PathFindingContext p, int x, int y) {
		float dx = p.getSourceX() - x;
		float dy = p.getSourceY() - y;
		return (float) Math.sqrt(dx * dx + dy * dy);
	}

	public int getHeightInTiles() {
		return map.getHeight();
	}

	public int getWidthInTiles() {
		return map.getWidth();
	}

	public void pathFinderVisited(int x, int y) {}

	public int getWidth() {
		return map.getWidth();
	}

	public int getHeight() {
		return map.getHeight();
	}

	public Image getTileImage(int x, int y, int id) {
		return map.getTileImage(x, y, id);
	}

}
