package com.rivergillis.androidgames.framework.math;

/**
 * A bounding circle.
 * Using circles is a very cheap way to check whether 2 objects collide
 * @author jgillis
 *
 */
public class Circle {
	public final Vector2 center = new Vector2();
	public float radius;
	
	public Circle(float x, float y, float radius) {
		this.center.set(x, y);
		this.radius = radius;
	}
}
