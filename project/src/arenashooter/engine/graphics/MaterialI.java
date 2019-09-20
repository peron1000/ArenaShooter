package arenashooter.engine.graphics;

import java.util.Set;

import arenashooter.engine.math.Mat4fi;
import arenashooter.engine.math.Vec2fi;
import arenashooter.engine.math.Vec3fi;
import arenashooter.engine.math.Vec4fi;

public interface MaterialI {
	public boolean getTransparency();
	
	public void setTransparency(boolean transparency);
	
	/**
	 * Attempt to bind this Material for rendering
	 * @param model
	 * @return success
	 */
	public boolean bind(Model model);
	
	public int getParamI(String name);

	public void setParamI(String name, int value);
	
	public float getParamF(String name);

	public void setParamF(String name, float value);
	
	public Vec2fi getParamVec2f(String name);
	
	public void setParamVec2f(String name, Vec2fi value);
	
	public Vec3fi getParamVec3f(String name);

	public void setParamVec3f(String name, Vec3fi value);
	
	public Vec4fi getParamVec4f(String name);

	public void setParamVec4f(String name, Vec4fi value);
	
	public Mat4fi getParamMat4f(String name);

	public void setParamMat4f(String name, Mat4fi value);
	
	public Texture getParamTex(String name);

	public void setParamTex(String name, Texture value);
	
	/**
	 * Set lights used on this materials
	 * @param lights
	 */
	public void setLights(Set<Light> lights);
	
	/**
	 * Creates a copy of this material, mutable parameters are cloned too
	 */
	public MaterialI clone();
}
