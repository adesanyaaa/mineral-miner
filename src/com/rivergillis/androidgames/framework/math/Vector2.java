package com.rivergillis.androidgames.framework.math;

import android.util.FloatMath;

/**
 * The Vector2 class serves as the main method of detailing
 * positions, velocities, distances, and directions in 2D space
 * It comes with functions for doing simple math on multiple Vectors as well
 * Most methods return a reference to the vector, allowing
 * them to be easily chained together
 * 
 * This class uses FloatMath to ensure it runs faster on older devices, however, this causes performance drawbacks on newer devices
 * Since newer devices are more powerful anyway, the drawbacks should not matter
 * @author jgillis
 *
 */
public class Vector2 {
	public static float TO_RADIANS = (1 / 180.0f) * (float)Math.PI;
	public static float TO_DEGREES = (1 / (float)Math.PI) * 180;	// multiply a number by these constants to get them in radians or degrees
	public float x, y;	// x and y positions of the vector
	
	public Vector2() {
	}
	
	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	// Create a vector from another vector
	public Vector2(Vector2 other) {
		this.x = other.x;
		this.y = other.y;
	}
	
	// copy a vector
	public Vector2 cpy() {
		return new Vector2(x, y);
	}
	
	// setting the x and y coords of a vector
	public Vector2 set(float x, float y) {				
		this.x = x;	
		this.y = y;
		return this;
	}
	
	public Vector2 set(Vector2 other) {
		this.x = other.x;
		this.y = other.y;
		return this;
	}
	
	// adding vectors
	public Vector2 add(float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	public Vector2 add(Vector2 other) {
		this.x += other.x;
		this.y += other.y;
		return this;
	}
	
	// subtracting vectors
	public Vector2 sub(float x, float y) {
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	public Vector2 sub(Vector2 other) {
		this.x -= other.x;
		this.y -= other.y;
		return this;
	}
	
	// multiplying vectors
	public Vector2 mul(float scalar) {
		this.x *= scalar;
		this.y *= scalar;
		return this;
	}
	
	// getting the length of a vector
	public float len() {
		return FloatMath.sqrt(x * x + y * y);
	}
	
	// normalizes the vector to unit length
	public Vector2 nor() {		
		float len = len();
		if(len != 0) {
			this.x /= len;
			this.y /= len;
		}
		return this;
	}
	
	// calculates the angle between the vector an the x axis
	public float angle() {
		float angle = (float)Math.atan2(y, x) * TO_DEGREES;
		if(angle < 0)
			angle += 360;
		return angle;
	}
	
	// rotates the vector around the origin by a given angle
	public Vector2 rotate(float angle) {
		float rad = angle * TO_RADIANS;		// first have to convert it to radians
		float cos = FloatMath.cos(rad);
		float sin = FloatMath.sin(rad);
		
		float newX = this.x * cos - this.y * sin;
		float newY = this.x * sin - this.y * cos;
		
		this.x = newX;
		this.y = newY;
		return this;
	}
	
	// calculate the distance between vectors
	public float dist(Vector2 other) {
		float distX = this.x - other.x;
		float distY = this.y - other.y;
		return FloatMath.sqrt(distX * distX + distY * distY);
	}
	
	public float dist(float x, float y) {
		float distX = this.x - x;
		float distY = this.y - y;
		return FloatMath.sqrt(distX * distX + distY * distY);
	}
	
	// calculates squared distance between vectors
	public float distSquared(Vector2 other) {
		float distX = this.x - other.x;
		float distY = this.y - other.y;
		return distX * distX + distY * distY;
	}
	
	public float distSquared(float x, float y) {
		float distX = this.x - x;
		float distY = this.y - y;
		return distX * distX + distY * distY;
	}
}
