package arenashooter.engine.graphics;

import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;
import java.util.Map;

import arenashooter.engine.FileUtils;
import arenashooter.engine.math.Mat4fi;
import arenashooter.engine.math.Vec2fi;
import arenashooter.engine.math.Vec3fi;
import arenashooter.engine.math.Vec4fi;

/**
 * Container for an openGL shader program
 */
public final class GLShader implements Shader { //TODO remove public modifier
	/** Array used to send Vec2f values to openGL */
	private static float[] floatBuffer2 = new float[2];
	/** Array used to send Vec3f values to openGL */
	private static float[] floatBuffer3 = new float[3];
	/** Array used to send Vec4f values to openGL */
	private static float[] floatBuffer4 = new float[4];
	/** Array used to send Mat4f values to openGL */
	private static float[] floatBuffer16 = new float[16];
	
	/** Cached shaders */
	private static Map<String, GLShader> cache = new HashMap<>();
	/** Cached shader source code */
	private static Map<String, String> cacheSrc = new HashMap<>();

	/** Currently bound shader program */
	private static int boundShader = 0;
	
	private Map<String, Integer> uniforms = new HashMap<>();
	
	private int program;
	
	private GLShader(int program) {
		this.program = program;
	}
	
	/**
	 * Tries to load a shader from cache or load a new one. 
	 * @param path resource path to the files, excluding file extensions
	 * @return 
	 */
	public static GLShader loadShader(String pathVertex, String pathFragment) {
		String key = pathVertex+"/~\\"+pathFragment;
		GLShader cached = cache.get(key);
		if(cached != null) return cached;
		
		GLShader shader = loadShaderFromDisk(pathVertex, pathFragment);
		cache.put(key, shader);
		return shader;
	}
	
	/**
	 * Create a shader from vertex and fragment shader files
	 * @param vertexPath resource path to the vertex shader
	 * @param fragmentPath resource path to the fragment shader
	 * @return 
	 */
	private static GLShader loadShaderFromDisk(String vertexPath, String fragmentPath) {
		//Vertex shader
		String vertexSrc;
		
		if(cacheSrc.containsKey(vertexPath))
			vertexSrc = cacheSrc.get(vertexPath);
		else {
			vertexSrc = FileUtils.resToString(vertexPath);
			cacheSrc.put(vertexPath, vertexSrc);
		}
		
		int vertex = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertex, FileUtils.resToString(vertexPath));
		glCompileShader(vertex);
		if( glGetShaderi(vertex, GL_COMPILE_STATUS) != GL_TRUE ) {
			GLRenderer.log.error("Cannot compile vertex shader: "+vertexPath);
			GLRenderer.log.error(glGetShaderInfoLog(vertex));
		}

		//Fragment shader
		String fragmentSrc;

		if(cacheSrc.containsKey(fragmentPath))
			fragmentSrc = cacheSrc.get(fragmentPath);
		else {
			fragmentSrc = FileUtils.resToString(fragmentPath);
			cacheSrc.put(vertexPath, vertexSrc);
		}

		int fragment = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragment, fragmentSrc);
		glCompileShader(fragment);
		if( glGetShaderi(fragment, GL_COMPILE_STATUS) != GL_TRUE ) {
			GLRenderer.log.error("Cannot compile fragment shader: "+fragmentPath);
			GLRenderer.log.error(glGetShaderInfoLog(fragment));
		}

		//Program
		int program = glCreateProgram();
		glAttachShader(program, vertex);
		glAttachShader(program, fragment);
		
		glLinkProgram(program);
		if( glGetProgrami(program, GL_LINK_STATUS) != GL_TRUE ) {
			GLRenderer.log.error("Cannot link shaders: "+vertexPath+", "+fragmentPath);
			GLRenderer.log.error(glGetProgramInfoLog(program));
		}
		glValidateProgram(program);
		
		boolean success = glGetProgrami(program, GL_VALIDATE_STATUS) == GL_TRUE;
		if( !success ) {
			GLRenderer.log.error("Cannot validate shaders: "+vertexPath+", "+fragmentPath);
			GLRenderer.log.error(glGetProgramInfoLog(program));
		}
		
		//Cleanup vertex and fragment shaders after linking
		glUseProgram(program);
		boundShader = program;
		glDetachShader(program, vertex);
		glDeleteShader(vertex);
		glDetachShader(program, fragment);
		glDeleteShader(fragment);
		
		if(success)
			return new GLShader(program);
		else
			return null;
	}
	
	int getAttribLocation(String name) { return glGetAttribLocation(program, name); }
	
	private int getUniformLocation(String name) {
		if(uniforms.containsKey(name))
			return uniforms.get(name);
		else {
			int res = glGetUniformLocation(program, name);
			uniforms.put(name, res);
			return res;
		}
	}
	
	@Override
	public void setUniformM4(String name, Mat4fi value) {
		glUniformMatrix4fv(getUniformLocation(name), false, value.toArray(floatBuffer16));
	}
	
	@Override
	public void setUniformV2(String name, Vec2fi value) {
		glUniform2fv(getUniformLocation(name), value.toArray(floatBuffer2));
	}
	
	@Override
	public void setUniformV3(String name, Vec3fi value) {
		glUniform3fv(getUniformLocation(name), value.toArray(floatBuffer3));
	}
	
	@Override
	public void setUniformV4(String name, Vec4fi value) {
		glUniform4fv(getUniformLocation(name), value.toArray(floatBuffer4));
	}
	
	@Override
	public void setUniformF(String name, float value) {
		glUniform1f(getUniformLocation(name), value);
	}
	
	@Override
	public void setUniformI(String name, int value) {
		glUniform1i(getUniformLocation(name), value);
	}
	
	@Override
	public void bind() {
		if( program == boundShader ) return;
		glUseProgram(program);
		boundShader = program;
	}
	
	/**
	 * Stop current shader
	 */
	static void unbind() {
		glUseProgram(0);
		boundShader = 0;
	}
	
	//
	//Memory Management
	//
	
	/**
	 * Remove unused shaders from memory
	 */
	public static void cleanShaders() {
		//TODO
	}
}
