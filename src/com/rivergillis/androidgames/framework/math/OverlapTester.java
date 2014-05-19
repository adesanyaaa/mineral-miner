package com.rivergillis.androidgames.framework.math;

/**
 * This helper class tests whether or not shapes and points are 
 * overlapping eachother. This is most useful for collision detection.
 * @author jgillis
 *
 */
public class OverlapTester {
	
	// determines whether or not two circles are overlapping
	public static boolean overlapCircles(Circle c1, Circle c2) {
		float distance = c1.center.distSquared(c2.center);
		float radiusSum = c1.radius + c2.radius;
		return distance <= radiusSum * radiusSum;
	}
	
	// determines whether or not two rectangles are overlapping
	public static boolean overlapRectangles(Rectangle r1, Rectangle r2) {
		if(	r1.lowerLeft.x < r2.lowerLeft.x + r2.width &&			// left edge of first rect must be to the left of the right edge of the second rect
			r1.lowerLeft.x + r1.width > r2.lowerLeft.x &&			// right edge of the first rect rect must be to the right of the left edge of the second rect
			r1.lowerLeft.y < r2.lowerLeft.y + r2.height &&			
			r1.lowerLeft.y + r1.height > r2.lowerLeft.y)			
			return true;
		else
			return false;
	}
	
	// determines whether or not a circle and a rectangle are overlapping
	public static boolean overlapCircleRectangle(Circle c, Rectangle r) {
		float closestX = c.center.x;
		float closestY = c.center.y;
		
		if(c.center.x < r.lowerLeft.x) {
			closestX = r.lowerLeft.x;
		} else if(c.center.x > r.lowerLeft.x + r.width) {
			closestX = r.lowerLeft.x + r.width;
		}
		
		if(c.center.y < r.lowerLeft.y) {
			closestY = r.lowerLeft.y;
		} else if(c.center.y > r.lowerLeft.y + r.height) {
			closestY = r.lowerLeft.y + r.height;
		}
		
		return c.center.distSquared(closestX, closestY) < c.radius * c.radius;
	}
	
	// determines whether or not a point is in a circle
	public static boolean pointInCircle(Circle c, Vector2 p) {
		return c.center.distSquared(p) < c.radius * c.radius;
	}
	
	public static boolean pointInCircle(Circle c, float x, float y) {
		return c.center.distSquared(x, y) < c.radius * c.radius;
	}
	
	// determines whether or not a point is in a rectangle
	public static boolean pointInRectangle(Rectangle r, Vector2 p) {
		return r.lowerLeft.x <= p.x && r.lowerLeft.x + r.width >= p.x &&
				r.lowerLeft.y <= p.y && r.lowerLeft.y + r.height >= p.y;
	}
	
	public static boolean pointInRectangle(Rectangle r, float x, float y) {
		return r.lowerLeft.x <= x && r.lowerLeft.x + r.width >= x &&
				r.lowerLeft.y <= y && r.lowerLeft.y + r.height >= y;
	}
}
