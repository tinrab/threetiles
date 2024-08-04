package com.threetiles.www.world.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import com.threetiles.www.util.Vec2;
import com.threetiles.www.world.World;

public abstract class Entity implements Comparable<Entity> {

	public static enum Type {
		MOB(0), BULLET(1), EFFECT(2);

		public int VALUE;

		Type(int val) {
			this.VALUE = val;
		}
	}

	private static int next;
	private int id;
	private Type type;
	protected Vec2 position;
	protected World world;

	public Entity(Type type, Vec2 position) {
		id = next++;
		this.type = type;
		this.position = position;
	}

	public int getID() {
		return id;
	}

	public Type getType() {
		return type;
	}

	public Vec2 getPosition() {
		return position;
	}

	public void enter(World world) {
		this.world = world;
	}

	public World getWorld() {
		return world;
	}

	public abstract void update(GameContainer gc, StateBasedGame game, int delta);

	public boolean equals(Object obj) {
		if (obj == null) return false;
		return this.id == ((Entity) obj).id;
	}

	public int compareTo(Entity e) {
		if (e.type.VALUE < this.type.VALUE) return -1;
		else if (e.type.VALUE > this.type.VALUE) return 1;
		return 0;
	}

}
