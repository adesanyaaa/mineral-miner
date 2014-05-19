package com.rivergillis.androidgames.framework.gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.rivergillis.androidgames.framework.impl.GLGraphics;

/**
 * Holds a maximum number of vertices and, optionally, indices to be used for rendering.
 * It also takes care of enabling all states needed for render, as well
 * as cleaning up the states after rendering has finished
 * @author jgillis
 *
 */
public class Vertices {
	final GLGraphics glGraphics;	// used to get a GL10 instance
	final boolean hasColor;
	final boolean hasTexCoords;		// whether the vertices have colors and texture coordinates
	final int vertexSize;
	final IntBuffer vertices;		// buffer to hold vertices
	final int[] tmpBuffer;
	final ShortBuffer indices;		// buffer to hold optional indices
	
	public Vertices(GLGraphics glGraphics, int maxVertices, int maxIndices, boolean hasColor, boolean hasTexCoords) {
		this.glGraphics = glGraphics;
		this.hasColor = hasColor;
		this.hasTexCoords = hasTexCoords;
		this.vertexSize = (2 + (hasColor?4:0) + (hasTexCoords?2:0)) * 4; // instantiate the buffer depending on the vertices' attributes
		this.tmpBuffer = new int[maxVertices * vertexSize / 4];
		
		ByteBuffer buffer = ByteBuffer.allocateDirect(maxVertices * vertexSize);
		buffer.order(ByteOrder.nativeOrder());
		vertices = buffer.asIntBuffer();			// sets up the vertices array as a direct-allocated int buffer
		
		if(maxIndices > 0) {
			buffer = ByteBuffer.allocateDirect(maxIndices * Short.SIZE / 8);
			buffer.order(ByteOrder.nativeOrder());
			indices = buffer.asShortBuffer();		// sets up the indices array as a direct-allocated short buffer if it is being used
		} else {
			indices = null;
		}
	}
	
	public void setVertices(float[] vertices, int offset, int length) {
		this.vertices.clear();							// clear the buffer
		int len = offset + length;						// set the offset and length
		for(int i = offset, j = 0; i < len; i++, j++)
			tmpBuffer[j] = Float.floatToRawIntBits(vertices[i]);	// converts the float values to raw int bits, and puts them in tmpbuffer
		this.vertices.put(tmpBuffer, 0, length);		// puts the contents of tmpbuffer into vertices
		this.vertices.flip();							// flip the array, as OpenGL expects it to be flipped
	}
	
	public void setIndices(short[] indices, int offset, int length) {
		this.indices.clear();							// clear the buffer
		this.indices.put(indices, offset, length);		// put the indices contents into the buffer
		this.indices.flip();							// flip the array
	}
	
	public void bind() {
		GL10 gl = glGraphics.getGL();
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		vertices.position(0);
		gl.glVertexPointer(2, GL10.GL_FLOAT, vertexSize, vertices);		// sets the positions of the client state and pointers
		
		if(hasColor) {
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			vertices.position(2);
			gl.glColorPointer(4, GL10.GL_FLOAT, vertexSize, vertices);	// change the positions if it has color
		}
		
		if(hasTexCoords) {
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);		// change the positions if it has tex coords
			vertices.position(hasColor?6:2);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, vertexSize, vertices);
		}
	}
	
	// draw works with the bound vertices, so they must be bound first
	public void draw(int primitiveType, int offset, int numVertices) {
		GL10 gl = glGraphics.getGL();

		if(indices != null) {
			indices.position(offset);
			gl.glDrawElements(primitiveType, numVertices, GL10.GL_UNSIGNED_SHORT, indices);	// draw the vertices
		} else {
			gl.glDrawArrays(primitiveType, offset, numVertices);
		}
	}
	
	// disables any vertex attributes
	public void unbind() {
		GL10 gl = glGraphics.getGL();
		if(hasTexCoords)
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		if(hasColor)
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	}
}
