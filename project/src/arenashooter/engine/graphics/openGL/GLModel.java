package arenashooter.engine.graphics.openGL;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryUtil;

import arenashooter.engine.Profiler;
import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Shader;
import arenashooter.engine.math.Vec2f;

/**
 * Contains geometry data for a single openGL model 
 * Doesn't have any shader or texture information
 */
final class GLModel implements Model {
	//Floats per vertex: x, y, z + u, v + nx, ny, nz
	private static final int floatsPerVertex = 3 + 2 + 3;
	//Float size in bytes
	private static final int floatByteSize = (Float.SIZE / Byte.SIZE);
	
	private int vaoID, vboID, indexVBO;
	private final int indicesCount;
	
	private boolean ready = false;
	private FloatBuffer dataBuffer;
	private IntBuffer indicesBuffer;
	
	/**
	 * Load a model
	 * @param data vertices data (position + uv)
	 * @param indices vertices indices ()
	 */
	GLModel( float[] data, int[] indices ) {
		//Memory allocation
		dataBuffer = MemoryUtil.memAllocFloat(data.length);
		dataBuffer.put(data);
		((Buffer)dataBuffer).flip();

		indicesBuffer = MemoryUtil.memAllocInt(indices.length);
		indicesBuffer.put(indices);
		((Buffer)indicesBuffer).flip();
		indicesCount = indices.length;
	}
	
	/**
	 * Only call this from a thread with an opengl context
	 */
	private void initModel() {
		if(ready) return;
		ready = true;
		
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
		dataBuffer = null;
		indicesBuffer = null;
	}
	
	@Override
	public void bindToShader( Shader shader ) {
		GLShader glShader = (GLShader) shader;
		
		if(!ready) initModel();
		glBindVertexArray(vaoID);

		glBindBuffer( GL_ARRAY_BUFFER, vboID );

		//Vertices
		int posAttLoc = glShader.getAttribLocation("position");
		glEnableVertexAttribArray(posAttLoc);
		glVertexAttribPointer(posAttLoc, 3, GL_FLOAT, false, floatsPerVertex*floatByteSize, 0);

		//UVs
		int uvAttLoc = glShader.getAttribLocation("uv");
		glEnableVertexAttribArray(uvAttLoc);
		glVertexAttribPointer(uvAttLoc, 2, GL_FLOAT, false, floatsPerVertex*floatByteSize, 3*floatByteSize);
		
		//Normals
		int normAttLoc = glShader.getAttribLocation("normal");
		glEnableVertexAttribArray(normAttLoc);
		glVertexAttribPointer(normAttLoc, 3, GL_FLOAT, false, floatsPerVertex*floatByteSize, (3+2)*floatByteSize);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}
	
	@Override
	public void bind() {
		if(!ready) initModel();
		glBindVertexArray(vaoID);
		glEnableVertexAttribArray(0); //Enable position
		glEnableVertexAttribArray(1); //Enable uv
		
		//Bind indices buffer
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexVBO);
	}
	
	@Override
	public void draw() {
		glDrawElements(GL_TRIANGLES, indicesCount, GL_UNSIGNED_INT, 0);
		Profiler.drawCalls++;
	}
	
	@Override
	public void draw(boolean wireframe) {
		glDrawElements(wireframe ? GL_LINE_LOOP : GL_TRIANGLES, indicesCount, GL_UNSIGNED_INT, 0);
		Profiler.drawCalls++;
	}
	
	public static void unbind() {
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
	}
	
	/**
	 * Create a simple quad from (-.5, -.5, 0) to (.5, .5, 0) with corresponding texture coordinates
	 * @return the quad model
	 */
	static GLModel loadQuad() {
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
		
		return new GLModel( data, indices );
	}
	
	/**
	 * Create a simple disk centered at (0,0) with a diameter of 1 with corresponding texture coordinates
	 * @param sides number of vertices, increasing this will improve the shape
	 * @return the disk model
	 */
	static GLModel loadDisk(int sides) {
		if( sides < 3 ) sides = 3;
		
		float[] data = new float[(sides+1)*floatsPerVertex];
		//Center
		data[0] = 0;   //x
		data[1] = 0;   //y
		data[2] = 0;   //z
		data[3] = .5f; //u
		data[4] = .5f; //v
		data[5] = 0;   //nx
		data[6] = 0;   //ny
		data[7] = 1;   //nz
		int[] indices = new int[sides*3];
		for(int i=0; i<sides; i++) {
			Vec2f v = Vec2f.fromAngle((2*Math.PI*i)/(double)sides);
			v.multiply(.5f);
			
			//Vertex ID (i+1 to skip 0 which is used as the center)
			int vID = (i+1)*(3+2+3);
			
			//Vertices positions
			data[vID]   = v.x; //x
			data[vID+1] = v.y; //y
			data[vID+2] = 0;   //z
			//Texture coordinates
			data[vID+3] = (float) (v.x+.5); //u
			data[vID+4] = (float) (v.y+.5); //v
			//Normal
			data[vID+5] = 0;   //nx
			data[vID+6] = 0;   //ny
			data[vID+7] = 1;   //nz
			
			int fID = i*3;
			
			indices[fID]   = 0;
			indices[fID+1] = i+1;
			indices[fID+2] = ((i+1)%sides)+1;
		}
		return new GLModel(data, indices);
	}

	//
	//Memory management
	//
	

}
