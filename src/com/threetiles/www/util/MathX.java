package com.threetiles.www.util;

public class MathX {

	public static int clamp(int n, int min, int max) {
		if (n <= min) return min;
		if (n >= max) return max;
		return n;
	}

	public static float clamp(float n, float min, float max) {
		if (n <= min) return min;
		if (n >= max) return max;
		return n;
	}
}
