package com.threetiles.www.util;

public class Vec2 {

	private float x, y;

	public Vec2() {
		this.x = 0;
		this.y = 0;
	}

	public Vec2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void translate(float dx, float dy) {
		x += dx;
		y += dy;
	}

	public float dot(Vec2 v) {
		return x * v.x + y * v.y;
	}

	public void sub(Vec2 v) {
		x -= v.getX();
		y -= v.getY();
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void scale(float scale) {
		x *= scale;
		y *= scale;
	}

	public boolean equals(Vec2 v) {
		return this.x == v.x && this.y == v.y;
	}

	public void set(Vec2 v) {
		x = v.x;
		y = v.y;
	}

	public float distance(Vec2 v) {
		float dx = x - v.x;
		float dy = y - v.y;
		return dx * dx + dy * dy;
	}

}
