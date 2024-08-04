package com.threetiles.www.util;

import com.threetiles.www.Game;

public class Camera {

	private Vec2 position;
	private Vec2 target;
	private float screenShakeAmplitude;
	private float screenShakeAnim;

	private float offx, offy;

	public Camera() {
		position = new Vec2(-500, 0);
		target = new Vec2();
	}

	public void transform() {
		float x = (float) (position.getX() - Game.WIDTH / 2f + 8);
		x = (float) (x + Math.cos((float) (screenShakeAnim * Math.PI * 20)) * screenShakeAmplitude);
		float y = (float) (position.getY() - Game.HEIGHT / 2f + 8);
		y = (float) (y + Math.sin((float) (screenShakeAnim * Math.PI * 6)) * screenShakeAmplitude * 0.7f);
		offx = x;
		offy = y;
	}

	public void update() {
		position.setX(fade(position.getX(), target.getX()));
		position.setY(fade(position.getY(), target.getY()));

		screenShakeAnim += 0.005f;
		screenShakeAmplitude -= screenShakeAmplitude * 0.05f;
	}

	public void setTarget(Vec2 target) {
		this.target.set(target.getX(), target.getY());
	}

	private float fade(float current, float target) {
		return current + 0.04f * (target - current);
	}

	public void addShake(float amount) {
		screenShakeAmplitude += amount;
	}

	public float getX() {
		return offx;
	}

	public float getY() {
		return offy;
	}

}
