package arenashooter.engine.graphics;

import static org.lwjgl.opengl.GL20.*;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.lwjgl.BufferUtils;

import arenashooter.engine.FileUtils;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;

/**
 * Container for a shader program
 */
public class Shader {
	public enum ParamType {
		INT, FLOAT, VEC2F, VEC3F, VEC4F, MAT4F, TEXTURE2D;
	}
	
	/** Array used to send Vec2f values to openGL */
	private static float[] floatBuffer2 = new float[2];
	/** Array used to send Vec3f values to openGL */
	private static float[] floatBuffer3 = new float[3];
	/** Array used to send Vec4f values to openGL */
	private static float[] floatBuffer4 = new float[4];
	/** Array used to send Mat4f values to openGL */
	private static float[] floatBuffer16 = new float[16];
	
	/** Cached shaders */
	private static Map<String, Shader> cache = new HashMap<>();
	/** Cached shader source code */
	private static Map<String, String> cacheSrc = new HashMap<>();

	/** Currently bound shader program */
	private static int boundShader = 0;
	
	private Map<String, Integer> uniforms;
	private ParamType[] uniformTypes;
	Map<String, Integer> defaultsParamsI = new HashMap<>();
	Map<String, Float> defaultsParamsF = new HashMap<>();
	Map<String, Vec2f> defaultsParamsVec2f = new HashMap<>();
	Map<String, Vec3f> defaultsParamsVec3f = new HashMap<>();
	Map<String, Vec4f> defaultsParamsVec4f = new HashMap<>();
	
	private int program;
	
	private Shader(int program) {
		this.program = program;
		scanUniforms();
	}
	
	/**
	 * Tries to load a shader from cache or load a new one. 
	 * Create a shader from vertex and fragment shader files sharing the same path and name.
	 * Appends ".vert" and ".frag" to get the complete file paths
	 * @param path resource path to the files, excluding file extensions
	 * @return 
	 */
	public static Shader loadShader(String path) {
		Shader cached = cache.get(path);
		if(cached != null) return cached;
		
		Shader shader = loadShader(path+".vert", path+".frag");
		cache.put(path, shader);
		return shader;
	}
	
	/**
	 * Create a shader from vertex and fragment shader files
	 * @param vertexPath resource path to the vertex shader
	 * @param fragmentPath resource path to the fragment shader
	 * @return 
	 */
	private static Shader loadShader(String vertexPath, String fragmentPath) {
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
			Window.log.error("Cannot compile vertex shader: "+vertexPath);
			Window.log.error(glGetShaderInfoLog(vertex));
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
			Window.log.error("Cannot compile fragment shader: "+fragmentPath);
			Window.log.error(glGetShaderInfoLog(fragment));
		}

		//Program
		int program = glCreateProgram();
		glAttachShader(program, vertex);
		glAttachShader(program, fragment);
		
		glLinkProgram(program);
		if( glGetProgrami(program, GL_LINK_STATUS) != GL_TRUE ) {
			Window.log.error("Cannot link shaders: "+vertexPath+", "+fragmentPath);
			Window.log.error(glGetProgramInfoLog(program));
		}
		glValidateProgram(program);
		if( glGetProgrami(program, GL_VALIDATE_STATUS) != GL_TRUE ) {
			Window.log.error("Cannot validate shaders: "+vertexPath+", "+fragmentPath);
			Window.log.error(glGetProgramInfoLog(program));
		}
		
		//Cleanup vertex and fragment shaders after linking
		glUseProgram(program);
		boundShader = program;
		glDetachShader(program, vertex);
		glDeleteShader(vertex);
		glDetachShader(program, fragment);
		glDeleteShader(fragment);
		
		return new Shader(program);
	}
	
	/**
	 * Create a Map of all uniforms available in this shader and load their default values
	 */
	private void scanUniforms() {
		IntBuffer countB = BufferUtils.createIntBuffer(1);
		glGetProgramiv(program, GL_ACTIVE_UNIFORMS, countB);
		int count = countB.get(0);
		
		uniforms = new HashMap<>();
		uniformTypes = new ParamType[count];

		IntBuffer sizeB = BufferUtils.createIntBuffer(1);
		IntBuffer typeB = BufferUtils.createIntBuffer(1);
		for(int i = 0; i < count; i++) {
			String name = glGetActiveUniform(program, i, sizeB, typeB); //TODO This might cause errors on some drivers
		    
		    uniforms.put(name, i);
		    
		    ParamType type;
		    switch(typeB.get(0)) {
		    case GL_INT:
		    	type = ParamType.INT;
		    	defaultsParamsI.put(name, glGetUniformi(program, i));
		    	break;
		    case GL_FLOAT:
		    	type = ParamType.FLOAT;
		    	defaultsParamsF.put(name, glGetUniformf(program, i));
		    	break;
		    case GL_FLOAT_VEC2:
		    	type = ParamType.VEC2F;
		    	float[] vec2 = new float[2];
		    	glGetUniformfv(program, i, vec2);
		    	defaultsParamsVec2f.put(name, new Vec2f(vec2[0], vec2[1]));
		    	break;
		    case GL_FLOAT_VEC3:
		    	type = ParamType.VEC3F;
		    	float[] vec3 = new float[3];
		    	glGetUniformfv(program, i, vec3);
		    	defaultsParamsVec3f.put(name, new Vec3f(vec3[0], vec3[1], vec3[2]));
		    	break;
		    case GL_FLOAT_VEC4:
		    	type = ParamType.VEC4F;
		    	float[] vec4 = new float[4];
		    	glGetUniformfv(program, i, vec4);
		    	defaultsParamsVec4f.put(name, new Vec4f(vec4[0], vec4[1], vec4[2], vec4[3]));
		    	break;
		    case GL_FLOAT_MAT4: //No default values
		    	type = ParamType.MAT4F;
		    	break;
		    case GL_SAMPLER_2D: //No default values
		    	type = ParamType.TEXTURE2D;
		    	break;
		    default:
		    	Window.log.error("Unsuported uniform type for \""+name+"\"");
		    	type = null;
		    	break;
		    }
		    uniformTypes[i] = type;

		    if(type != null) {
		    	Window.log.debug("Shader uniform "+i+": \""+name+"\": "+type);
		    }
		}
	}
	
	public int getAttribLocation(String name) { return glGetAttribLocation(program, name); }
	
	/**
	 * @return a Set of all available uniform names
	 */
	public Set<String> getUniformNames() { return uniforms.keySet(); }
	
	/**
	 * Get the type of a uniform, of null if invalid name
	 * @param name
	 * @return
	 */
	public ParamType getUniformType(String name) {
		if(getUniformLocation(name) < 0) return null;
		return uniformTypes[getUniformLocation(name)];
	}
	
	private int getUniformLocation(String name) {
		if(uniforms.containsKey(name))
			return uniforms.get(name);
		else {
			int res = glGetUniformLocation(program, name);
			uniforms.put(name, res);
			return res;
		}
	}
	
	/**
	 * Set a Matrix4f uniform variable
	 * @param name uniform's name
	 * @param value uniform value
	 */
	public void setUniformM4(String name, Mat4f value) {
		glUniformMatrix4fv(getUniformLocation(name), false, value.toArray(floatBuffer16));
	}
	
	/**
	 * Set a Vec2f uniform variable
	 * @param name uniform's name
	 * @param value uniform value
	 */
	public void setUniformV2(String name, Vec2f value) {
		glUniform2fv(getUniformLocation(name), value.toArray(floatBuffer2));
	}
	
	/**
	 * Set a Vec3f uniform variable
	 * @param name uniform's name
	 * @param value uniform value
	 */
	public void setUniformV3(String name, Vec3f value) {
		glUniform3fv(getUniformLocation(name), value.toArray(floatBuffer3));
	}
	
	/**
	 * Set a Vec4f uniform variable
	 * @param name uniform's name
	 * @param value uniform value
	 */
	public void setUniformV4(String name, Vec4f value) {
		glUniform4fv(getUniformLocation(name), value.toArray(floatBuffer4));
	}
	
	/**
	 * Set a float uniform variable
	 * @param name uniform's name
	 * @param value uniform value
	 */
	public void setUniformF(String name, float value) {
		glUniform1f(getUniformLocation(name), value);
	}
	
	/**
	 * Set an int uniform variable
	 * @param name uniform's name
	 * @param value uniform value
	 */
	public void setUniformI(String name, int value) {
		glUniform1i(getUniformLocation(name), value);
	}
	
	/**
	 * Use this shader
	 */
	public void bind() {
		if( program == boundShader ) return;
		glUseProgram(program);
		boundShader = program;
	}
	
	/**
	 * Stop current shader
	 */
	public static void unbind() {
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
