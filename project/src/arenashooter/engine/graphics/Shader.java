package arenashooter.engine.graphics;

import arenashooter.engine.math.Mat4fi;
import arenashooter.engine.math.Vec2fi;
import arenashooter.engine.math.Vec3fi;
import arenashooter.engine.math.Vec4fi;

/**
 * Container for a shader program
 */
public interface Shader {
	
	/**
	 * Set an int uniform variable
	 * @param name uniform's name
	 * @param value uniform value
	 */
	public void setUniformI(String name, int value);
	
	/**
	 * Set a float uniform variable
	 * @param name uniform's name
	 * @param value uniform value
	 */
	public void setUniformF(String name, float value);
	
	/**
	 * Set a Matrix4f uniform variable
	 * @param name uniform's name
	 * @param value uniform value
	 */
	public void setUniformM4(String name, Mat4fi value);
	
	/**
	 * Set a Vec2f uniform variable
	 * @param name uniform's name
	 * @param value uniform value
	 */
	public void setUniformV2(String name, Vec2fi value);
	
	/**
	 * Set a Vec3f uniform variable
	 * @param name uniform's name
	 * @param value uniform value
	 */
	public void setUniformV3(String name, Vec3fi value);
	
	/**
	 * Set a Vec4f uniform variable
	 * @param name uniform's name
	 * @param value uniform value
	 */
	public void setUniformV4(String name, Vec4fi value);
	
	/**
	 * Use this shader
	 */
	public void bind();
}
