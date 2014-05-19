package com.rivergillis.androidgames.framework.gl;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import com.rivergillis.androidgames.framework.FileIO;
import com.rivergillis.androidgames.framework.impl.GLGame;
import com.rivergillis.androidgames.framework.impl.GLGraphics;

/**
 * This helper class will load a bitmap from an asset
 * It will then create a texture object from it.
 * @author jgillis
 *
 */
public class Texture {
	GLGraphics glGraphics;
	FileIO fileIO;
	String fileName;
	int textureId;
	int minFilter;
	int magFilter;
	int width;
	int height;
	
	// set up members
	public Texture(GLGame glGame, String fileName) {
		this.glGraphics = glGame.getGLGraphics();
		this.fileIO = glGame.getFileIO();
		this.fileName = fileName;
		load();
	}
	
	private void load() {
		GL10 gl = glGraphics.getGL();		// get a GL instance
		int[] textureIds = new int[1];
		gl.glGenTextures(1, textureIds, 0);	// generate  a texture id
		textureId = textureIds[0];
		
		InputStream in = null;
		try {
			in = fileIO.readAsset(fileName);					// gets a filestream from a filename
			Bitmap bitmap = BitmapFactory.decodeStream(in);		// gets a bitmap from the filestream
			width = bitmap.getWidth();
			height = bitmap.getHeight();
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			setFilters(GL10.GL_NEAREST, GL10.GL_NEAREST);		// sets min/mag filters
			gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);			// binds the texture
		} catch(IOException e) {
			throw new RuntimeException("Couldn't load texture '" + fileName + "'", e);
		} finally {
			if(in != null)
				try { in.close(); } catch (IOException e) {} //tries to close the filestream
		}
	}
	
	// calls the load and bind functions again, then sets the filters
	public void reload() {
		load();
		bind();
		setFilters(minFilter, magFilter);
		glGraphics.getGL().glBindTexture(GL10.GL_TEXTURE_2D, 0);
	}
	
	// sets the min/mag filters
	public void setFilters(int minFilter, int magFilter) {
		this.minFilter = minFilter;
		this.magFilter = magFilter;
		GL10 gl = glGraphics.getGL();
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, minFilter);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, magFilter);
	}
	
	// binds the texture
	public void bind() {
		GL10 gl = glGraphics.getGL();
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
	}
	
	// destroys the texture
	public void dispose() {
		GL10 gl = glGraphics.getGL();
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		int[] textureIds = { textureId };
		gl.glDeleteTextures(1, textureIds, 0);
	}
}
