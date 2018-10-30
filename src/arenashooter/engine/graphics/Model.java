package arenashooter.engine.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryStack;

public class Model {
	//Floats per vertex: x, y, z + u, v
	private static final int floatsPerVertex = 3 + 2;
	//Float size in bytes
	private static final int floatByteSize = (Float.SIZE / Byte.SIZE);
	
	private int vaoID, vboID, indexVBO, indicesCount;
	
	public Model( float[] data, byte[] indices ) {
		loadModel( data, indices );
	}
	
	private void loadModel( float[] data, byte[] indices ) {
		MemoryStack stack = MemoryStack.stackPush();
		FloatBuffer dataBuffer = stack.mallocFloat(data.length);
		dataBuffer.put(data);
		dataBuffer.flip();

		indicesCount = indices.length;
		ByteBuffer indicesBuffer = stack.malloc(indicesCount);
		indicesBuffer.put(indices);
		indicesBuffer.flip();

		//Create vao
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);

		//Store vertices data in vbo
		vboID = glGenBuffers();
		glBindBuffer( GL_ARRAY_BUFFER, vboID );
		glBufferData( GL_ARRAY_BUFFER, dataBuffer, GL_STATIC_DRAW );
		glBindBuffer( GL_ARRAY_BUFFER, 0 );

		glBindVertexArray(0);

		//Indices VBO
		indexVBO = glGenBuffers();
		glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, indexVBO );
		glBufferData( GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW );
		glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, 0 );
		
		//Free memory
		stack.pop();
	}
	
	public static Model loadSquare() {
		//Vertices positions and texture coordinates
		//		x,  y,  z,    u,  v
		float[] data = {
				1f, 0f, 0f,   1f, 0f, //0 Top right
				0f, 0f, 0f,   0f, 0f, //1 Top left
				0f, 1f, 0f,   0f, 1f, //2 Bot left
				1f, 1f, 0f,   1f, 1f  //3 Bot right
		};
		
		byte[] indices = {
				0, 1, 2, //Top triangle
				2, 3, 0 //Bot triangle
		};
		
		return new Model( data, indices );
	}
	
	public void bindToShader( Shader shader ) {
		glBindVertexArray(vaoID);

		glBindBuffer( GL_ARRAY_BUFFER, vboID );

		//Vertices
		int posAttLoc = shader.getAttribLocation("position");
		glEnableVertexAttribArray(posAttLoc);
		glVertexAttribPointer(posAttLoc, 3, GL_FLOAT, false, floatsPerVertex*floatByteSize, 0);

		//UVs
		int uvAttLoc = shader.getAttribLocation("uv");
		glEnableVertexAttribArray(uvAttLoc);
		glVertexAttribPointer(uvAttLoc, 2, GL_FLOAT, false, floatsPerVertex*floatByteSize, 3*floatByteSize);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}
	
	public void bind() {
		glBindVertexArray(vaoID);
		glEnableVertexAttribArray(0); //Enable position
		glEnableVertexAttribArray(1); //Enable uv
		
		//Bind indices buffer
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexVBO);
	}
	
	public void draw() {
		glDrawElements(GL_TRIANGLES, indicesCount, GL_UNSIGNED_BYTE, 0);
	}
	
	public static void unbind() {
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
	}
	
	public void destroy() { //TODO: Test this
		//destroy vao
		glBindVertexArray(0);
		glDeleteVertexArrays(vaoID);
		
		//destroy vbo
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glDeleteBuffers(vboID);
	}
}
