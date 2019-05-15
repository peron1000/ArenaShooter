package arenashooter.engine.graphics;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.util.HashMap;
import java.util.Map.Entry;

import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;

public class Material {
	private final Shader shader;
	
	private HashMap<String, Float> paramsF = new HashMap<>();
	private HashMap<String, Vec2f> paramsVec2f = new HashMap<>();
	private HashMap<String, Vec3f> paramsVec3f = new HashMap<>();
	private HashMap<String, Vec4f> paramsVec4f = new HashMap<>();
	private HashMap<String, Texture> paramsTex = new HashMap<>();
	
	public Mat4f model, view, proj;
	
	public Material(String shaderPath) {
		this.shader = Shader.loadShader(shaderPath);
	}
	
	public void bind(Model model) {
		shader.bind();
		model.bindToShader(shader);
		
		shader.setUniformM4("model", this.model);
		shader.setUniformM4("view", view);
		shader.setUniformM4("projection", proj);
		
		for(Entry<String, Float> entry : paramsF.entrySet())
			shader.setUniformF(entry.getKey(), entry.getValue());
		
		for(Entry<String, Vec2f> entry : paramsVec2f.entrySet())
			shader.setUniformV2(entry.getKey(), entry.getValue());
		
		for(Entry<String, Vec3f> entry : paramsVec3f.entrySet())
			shader.setUniformV3(entry.getKey(), entry.getValue());
		
		for(Entry<String, Vec4f> entry : paramsVec4f.entrySet())
			shader.setUniformV4(entry.getKey(), entry.getValue());
		
		int texSlot = 0;
		for(Entry<String, Texture> entry : paramsTex.entrySet()) {
//			glActiveTexture(GL_TEXTURE0);
			entry.getValue().bind();
			shader.setUniformI(entry.getKey(), texSlot);
			texSlot++;
		}
	}
	
	public float getParamF(String name) {
		return paramsF.get(name);
	}

	public void setParamF(String name, float value) {
		paramsF.put(name, value);
	}
	
	public Vec2f getParamVec2f(String name) {
		return paramsVec2f.get(name);
	}
	
	public void setParamVec2f(String name, Vec2f value) {
		paramsVec2f.put(name, value);
	}
	
	public Vec3f getParamVec3f(String name) {
		return paramsVec3f.get(name);
	}

	public void setParamVec3f(String name, Vec3f value) {
		paramsVec3f.put(name, value);
	}
	
	public Vec4f getParamVec4f(String name) {
		return paramsVec4f.get(name);
	}

	public void setParamVec4f(String name, Vec4f value) {
		paramsVec4f.put(name, value);
	}
	
	public Texture getParamTex(String name) {
		return paramsTex.get(name);
	}

	public void setParamTex(String name, Texture value) {
		paramsTex.put(name, value);
	}
}
