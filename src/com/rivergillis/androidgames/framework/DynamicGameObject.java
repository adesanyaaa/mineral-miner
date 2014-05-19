package com.rivergillis.androidgames.framework;

import com.rivergillis.androidgames.framework.math.Vector2;

/**
 * A game object that moves.
 * @author jgillis
 *
 */
public class DynamicGameObject extends GameObject {
	public final Vector2 velocity;
	public final Vector2 accel;
	
	public DynamicGameObject(float x, float y, float width, float height) {
		super(x, y, width, height);
		velocity = new Vector2();
		accel = new Vector2();
	}
}
