package com.rivergillis.androidgames.framework;

import com.rivergillis.androidgames.framework.math.Rectangle;
import com.rivergillis.androidgames.framework.math.Vector2;

/**
 * A static object with a rectangular bounding shape
 * @author jgillis
 *
 */
public class GameObject {
	public final Vector2 position;	// positions is the center
	public final Rectangle bounds;
	
	public GameObject(float x, float y, float width, float height) {
		this.position = new Vector2(x, y);
		this.bounds = new Rectangle(x - width / 2, y - height / 2, width, height);
	}
}
