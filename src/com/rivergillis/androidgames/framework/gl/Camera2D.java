package com.rivergillis.androidgames.framework.gl;

import javax.microedition.khronos.opengles.GL10;

import com.rivergillis.androidgames.framework.impl.GLGraphics;
import com.rivergillis.androidgames.framework.math.Vector2;

/**
 * This class will store the camera's position, the standard frustum
 * width and height, and the zoom factor in 2D space.
 * @author jgillis
 *
 */
public class Camera2D {
	public final Vector2 position;
	public float zoom;
	public final float frustrumWidth;
	public final float frustrumHeight;
	final GLGraphics glGraphics;		// needed to get up-to-date width and height of the screen
	
	public Camera2D(GLGraphics glGraphics, float frustrumWidth, float frustrumHeight) {
		this.glGraphics = glGraphics;
		this.frustrumWidth = frustrumWidth;
		this.frustrumHeight = frustrumHeight;
		this.position = new Vector2(frustrumWidth / 2, frustrumHeight / 2);
		this.zoom = 1.0f;	// zoom factor initially 1
	}
	
	// sets the viewport to span the whole screen and sets the projection matrix in accordance with the camera's parameters
	public void setViewportAndMatrices() {
		GL10 gl = glGraphics.getGL();
		gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(position.x - frustrumWidth * zoom / 2,
					position.x + frustrumWidth * zoom / 2,
					position.y - frustrumHeight * zoom / 2,
					position.y + frustrumHeight * zoom / 2,
					1, -1);
		gl.glMatrixMode(GL10.GL_MODELVIEW);	// all further matrix operation are targeting the model-view matrix
		gl.glLoadIdentity();				// load an identity matrix
	}
	
	// transforms a vector to world space
	public void touchToWorld(Vector2 touch) {
		touch.x = (touch.x / (float)glGraphics.getWidth()) * frustrumWidth * zoom;
		touch.y = (1 - touch.y / (float)glGraphics.getHeight()) * frustrumHeight * zoom;
		touch.add(position).sub(frustrumWidth * zoom / 2, frustrumHeight * zoom / 2);
	}
}
