package arenashooter.engine.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.lang.ref.WeakReference;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.system.MemoryUtil;

public class Model {
	private static Map<String, ModelEntry> models = new HashMap<String, ModelEntry>();
	
	//Floats per vertex: x, y, z + u, v + nx, ny, nz
	private static final int floatsPerVertex = 3 + 2 + 3;
	//Float size in bytes
	private static final int floatByteSize = (Float.SIZE / Byte.SIZE);
	
	private int vaoID, vboID, indexVBO, indicesCount;
	
	public Model( float[] data, int[] indices ) {
		loadModel( data, indices );
	}
	
	public static Model[] loadModel(String path) {
		return ModelObjLoader.loadObj(path);
	}
	
	/**
	 * Load a model
	 * @param data vertices data (position + uv)
	 * @param indices vertices indices ()
	 */
	private void loadModel( float[] data, int[] indices ) {
		//Memory allocation
		FloatBuffer dataBuffer = MemoryUtil.memAllocFloat(data.length);
		dataBuffer.put(data);
		dataBuffer.flip();

		IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
		indicesBuffer.put(indices);
		indicesBuffer.flip();
		indicesCount = indices.length;

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
		MemoryUtil.memFree(dataBuffer);
		MemoryUtil.memFree(indicesBuffer);
	}
	
	/**
	 * Create a simple quad from (-.5, -.5, 0) to (.5, .5, 0) with corresponding texture coordinates
	 * @return the quad model
	 */
	public static Model loadQuad() {
		//Vertices positions, texture coordinates and normals
		//		x,    y,    z,    u,  v,    nx, ny, nz
		float[] data = {
				-.5f, -.5f, 0f,   0f, 0f,   0f, 0f, 1f, //0
				 .5f, -.5f, 0f,   1f, 0f,   0f, 0f, 1f, //1
				 .5f,  .5f, 0f,   1f, 1f,   0f, 0f, 1f, //2
				-.5f,  .5f, 0f,   0f, 1f,   0f, 0f, 1f  //3
		};
		
		int[] indices = {
				0, 1, 2, //Top triangle
				2, 3, 0  //Bot triangle
		};
		
		return new Model( data, indices );
	}
	
	/**
	 * Send this model's data to a shader
	 * @param shader
	 */
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
		
		//Normals
		int normAttLoc = shader.getAttribLocation("normal");
		glEnableVertexAttribArray(normAttLoc);
		glVertexAttribPointer(normAttLoc, 3, GL_FLOAT, false, floatsPerVertex*floatByteSize, (3+2)*floatByteSize);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}
	
	/**
	 * Bind this model for drawing. Only one bind() is required if you want to draw this model multiple times.
	 */
	public void bind() {
		glBindVertexArray(vaoID);
		glEnableVertexAttribArray(0); //Enable position
		glEnableVertexAttribArray(1); //Enable uv
		
		//Bind indices buffer
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexVBO);
	}
	
	/**
	 * Draw this model. Make sure bind() was called before.
	 */
	public void draw() {
		glDrawElements(GL_TRIANGLES, indicesCount, GL_UNSIGNED_INT, 0);
	}
	
	public void draw(boolean wireframe) {
		glDrawElements(wireframe ? GL_LINE_LOOP : GL_TRIANGLES, indicesCount, GL_UNSIGNED_INT, 0);
	}
	
	public static void unbind() {
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
	}
	

	//
	//Memory management
	//
	
	/**
	 * Remove unused models from memory
	 */
//	public static void cleanModels() { //TODO: Support multiple models per file
//		System.out.println("Render - Cleaning memory...");
//		
//		ArrayList<String> toRemove = new ArrayList<String>(0);
//		
//		Model.unbind();
//		
//		for ( ModelEntry entry : ModelEntry.values() ) {
//		    if( entry.model.get() == null ) { //Model has been garbage collected
//		    	toRemove.add(entry.file);
//
//		    	//destroy vao
//				glDeleteVertexArrays(entry.vao);
//				
//				//destroy vbos
//				glDeleteBuffers(entry.vbo);
//				glDeleteBuffers(entry.indexVBO);
//		    }
//		}
//		
//		for( String s : toRemove )
//			models.remove(s);
//		
//		System.out.println("Render - Cleaned up "+toRemove.size()+" models.");
//	}
	
	private static class ModelEntry {
		int vao, vbo, indexVBO;
		String file;
		WeakReference<Model> model;
		
		ModelEntry(Model model) {
			this.vao = model.vaoID;
			this.vbo = model.vboID;
			this.indexVBO = model.indexVBO;
//			this.file = model.
			this.model = new WeakReference<Model>(model);
		}
	}
}
