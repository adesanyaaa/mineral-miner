package com.rivergillis.androidgames.framework.gl;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;

import com.rivergillis.androidgames.framework.impl.GLGraphics;
import com.rivergillis.androidgames.framework.math.Vector2;

/**
 * A simple spritebatcher object, it will collapse multiple draw calls into a single call
 * @author jgillis
 *
 */
public class SpriteBatcher {
	final float[] verticesBuffer;		// temporary float array where we store the vertices of the sprites of the current batch
	int bufferIndex;					// indicates where in the float array we should start to write the next vertices
	final Vertices vertices;			// the Vertices instance used to render the batch
	int numSprites;						// number drawn so far in the current batch
	
	public SpriteBatcher(GLGraphics glGraphics, int maxSprites) {
		this.verticesBuffer = new float[maxSprites * 4 * 4];	// create the float array with maxSprites, with 4 vertices per sprite, each vertex takes up 4 floats
		this.vertices = new Vertices(glGraphics, maxSprites * 4, maxSprites * 6, false, true);	// create the vertices instance
		this.bufferIndex = 0;
		this.numSprites = 0;
		
		short[] indices = new short[maxSprites * 6];			// create the indices instance
		int len = indices.length;
		short j = 0;
		for(int i = 0; i < len; i += 6, j += 4) {	// precompute the indices (0 1, 2, 2, 3, 0 ... 4, 5, 6, 6, 7, 4 etc)
			indices[i + 0] = (short)(j + 0);
			indices[i + 1] = (short)(j + 1);
			indices[i + 2] = (short)(j + 2);
			indices[i + 3] = (short)(j + 2);
			indices[i + 4] = (short)(j + 3);
			indices[i + 5] = (short)(j + 0);
		}
		vertices.setIndices(indices, 0, indices.length);
	}
	
	public void beginBatch(Texture texture) {
		texture.bind();		// binds the texture
		numSprites = 0;
		bufferIndex = 0;	// resets numsprites and bufferindex so that the first sprites vertices will get inserted at the front of the verticesbuffer array
	}
	
	// called to finalize and draw the current batch
	public void endBatch() {
		vertices.setVertices(verticesBuffer, 0, bufferIndex);	// transfers vertices defined for this batch from the float array to the vertices instance
		vertices.bind();										// binds the vertices instance
		vertices.draw(GL10.GL_TRIANGLES, 0, numSprites * 6);	// draws numsprites * 2 triangles
		vertices.unbind();										// unbinds the vertices instance
	}
	
	//draws a nonrotated sprite
	public void drawSprite(float x, float y, float width, float height, TextureRegion region) {
		float halfWidth = width / 2;
		float halfHeight = height / 2;
		float x1 = x - halfWidth;
		float y1 = y - halfHeight;		// calculate position of bottom left corner
		float x2 = x + halfWidth;
		float y2 = y + halfHeight;		// calculate position of rop right corner
		
		/*
		 * Uses the corners to construct the vertices together with the texture coordinates from the texture region
		 * The vertices are added in counter-clockwise order, starting at the bottom-left vertex
		 */
		verticesBuffer[bufferIndex++] = x1;
		verticesBuffer[bufferIndex++] = y1;
		verticesBuffer[bufferIndex++] = region.u1;
		verticesBuffer[bufferIndex++] = region.v2;
		
		verticesBuffer[bufferIndex++] = x2;
		verticesBuffer[bufferIndex++] = y1;
		verticesBuffer[bufferIndex++] = region.u2;
		verticesBuffer[bufferIndex++] = region.v2;
		
		verticesBuffer[bufferIndex++] = x2;
		verticesBuffer[bufferIndex++] = y2;
		verticesBuffer[bufferIndex++] = region.u2;
		verticesBuffer[bufferIndex++] = region.v1;
		
		verticesBuffer[bufferIndex++] = x1;
		verticesBuffer[bufferIndex++] = y2;
		verticesBuffer[bufferIndex++] = region.u1;
		verticesBuffer[bufferIndex++] = region.v1;
		
		numSprites++;	// increment numSprites
	}
	
	//draws a rotated sprite
	public void drawSprite(float x, float y, float width, float height, float angle, TextureRegion region) {
		/*
		 * This is essentially the same as the nonrotated version, except
		 * that we construct all four corner points instead of just the
		 * two opposite points. This is needed for the rotation.
		 */
		float halfWidth = width /2 ;
		float halfHeight = height / 2;
		
		float rad = angle * Vector2.TO_RADIANS;
		float cos = FloatMath.cos(rad);
		float sin = FloatMath.sin(rad);
		
		float x1 = -halfWidth * cos - (-halfHeight) * sin;
		float y1 = -halfWidth * sin + (-halfHeight) * cos;
		float x2 = halfWidth * cos - (-halfHeight) * sin;
		float y2 = halfWidth * sin + (-halfHeight) * cos;
		float x3 = halfWidth * cos - halfHeight * sin;
		float y3 = halfWidth * sin + halfHeight * cos;
		float x4 = -halfWidth * cos - halfHeight * sin;
		float y4 = -halfWidth * sin + halfHeight * cos;
		
		x1 += x;
		y1 += y;
		x2 += x;
		y2 += y;
		x3 += x;
		y3 += y;
		x4 += x;
		y4 += y;
		
		verticesBuffer[bufferIndex++] = x1;
		verticesBuffer[bufferIndex++] = y1;
		verticesBuffer[bufferIndex++] = region.u1;
		verticesBuffer[bufferIndex++] = region.v2;
		
		verticesBuffer[bufferIndex++] = x2;
		verticesBuffer[bufferIndex++] = y2;
		verticesBuffer[bufferIndex++] = region.u2;
		verticesBuffer[bufferIndex++] = region.v2;
		
		verticesBuffer[bufferIndex++] = x3;
		verticesBuffer[bufferIndex++] = y3;
		verticesBuffer[bufferIndex++] = region.u2;
		verticesBuffer[bufferIndex++] = region.v1;
		
		verticesBuffer[bufferIndex++] = x4;
		verticesBuffer[bufferIndex++] = y4;
		verticesBuffer[bufferIndex++] = region.u1;
		verticesBuffer[bufferIndex++] = region.v1;
		
		numSprites++;
	}
}
