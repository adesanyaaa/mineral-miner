package com.rivergillis.androidgames.framework.impl;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;

/**
 * This class will help to keep track of the GLSurfaceView and the GL10 instance
 * @author jgillis
 *
 */
public class GLGraphics {
	GLSurfaceView glView;
	GL10 gl;
	
	GLGraphics(GLSurfaceView glView) {
		this.glView = glView;
	}
	
	public GL10 getGL() {
		return gl;
	}
	
	public void setGL(GL10 gl) {
		this.gl = gl;
	}
	
	public int getWidth() {
		return glView.getWidth();
	}
	
	public int getHeight() {
		return glView.getHeight();
	}
}
