package arenashooter.engine.graphics;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static org.lwjgl.opengl.GL20.GL_TEXTURE0;
import static org.lwjgl.opengl.GL20.glActiveTexture;

import arenashooter.engine.graphics.Light.LightType;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.xmlReaders.reader.MaterialXmlReader;

public class Material {
	private static Map<String, Material> cache = new HashMap<String, Material>();
	
	public enum ParamType {
		INT, FLOAT, VEC2F, VEC3F, VEC4F, MAT4F, TEXTURE2D;
	}
	
	private Shader shader;
	private final String name, shaderPathV, shaderPathF;
	
	private boolean ready = false;
	
	/** Should this material be used during transparency pass */
	public boolean transparency = false;

	private Map<String, Integer> paramsI = new HashMap<>();
	private Map<String, Float> paramsF = new HashMap<>();
	private Map<String, Vec2f> paramsVec2f = new HashMap<>();
	private Map<String, Vec3f> paramsVec3f = new HashMap<>();
	private Map<String, Vec4f> paramsVec4f = new HashMap<>();
	private Map<String, Mat4f> paramsMat4f = new HashMap<>();
	private Map<String, Texture> paramsTex = new HashMap<>();
	
	private Material(String name, String shaderPathV, String shaderPathF) {
		this.name = name;
		this.shaderPathV = shaderPathV;
		this.shaderPathF = shaderPathF;
	}
	
	public static Material loadMaterial(String path) {
		Material res = cache.get(path);
		if(res != null) return res.clone();
		
		MaterialXmlReader reader = new MaterialXmlReader(path);
		
		res = new Material(path, reader.vertexShader, reader.fragmentShader);
		res.paramsI.putAll(reader.paramsI);
		res.paramsF.putAll(reader.paramsF);
		res.paramsVec2f.putAll(reader.paramsVec2f);
		res.paramsVec3f.putAll(reader.paramsVec3f);
		res.paramsVec4f.putAll(reader.paramsVec4f);
		res.paramsTex.putAll(reader.paramsTex);
		
		//Add matrices
		res.paramsMat4f.put("model", Mat4f.identity());
		res.paramsMat4f.put("view", Mat4f.identity());
		res.paramsMat4f.put("projection", Mat4f.identity());
		
		cache.put(path, res);
		
		return res.clone();
	}

	/**
	 * Attempt to bind this Material for rendering
	 * @param model
	 * @return success
	 */
	public boolean bind(Model model) {
		if(!ready) initMaterial();
		if(shader == null) return false;
		
		shader.bind();
		model.bindToShader(shader);
		
		for(Entry<String, Integer> entry : paramsI.entrySet())
			shader.setUniformI(entry.getKey(), entry.getValue());
		
		for(Entry<String, Float> entry : paramsF.entrySet())
			shader.setUniformF(entry.getKey(), entry.getValue());
		
		for(Entry<String, Vec2f> entry : paramsVec2f.entrySet())
			shader.setUniformV2(entry.getKey(), entry.getValue());
		
		for(Entry<String, Vec3f> entry : paramsVec3f.entrySet())
			shader.setUniformV3(entry.getKey(), entry.getValue());
		
		for(Entry<String, Vec4f> entry : paramsVec4f.entrySet())
			shader.setUniformV4(entry.getKey(), entry.getValue());
		
		for(Entry<String, Mat4f> entry : paramsMat4f.entrySet())
			shader.setUniformM4(entry.getKey(), entry.getValue());
		
		int texSlot = 0;
		for(Entry<String, Texture> entry : paramsTex.entrySet()) {
			glActiveTexture(GL_TEXTURE0+texSlot);
			entry.getValue().bind();
			shader.setUniformI(entry.getKey(), texSlot);
			texSlot++;
		}
		
		return true;
	}
	
	public int getParamI(String name) {
		return paramsI.get(name);
	}

	public void setParamI(String name, int value) {
		paramsI.put(name, value);
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
	
	public Mat4f getParamMat4f(String name) {
		return paramsMat4f.get(name);
	}

	public void setParamMat4f(String name, Mat4f value) {
		paramsMat4f.put(name, value);
	}
	
	public Texture getParamTex(String name) {
		return paramsTex.get(name);
	}

	public void setParamTex(String name, Texture value) {
		paramsTex.put(name, value);
	}
	
	/**
	 * Set lights used on this materials
	 * @param lights
	 */
	public void setLights(Set<Light> lights) {
		if(!ready) initMaterial();
		
		int i = 0;
		for(Light light : lights) {
			//Skip 0-radius lights
			if(light.radius == 0) continue;
			//Skip black lights
			if(light.color.x == 0 && light.color.y == 0 && light.color.z == 0) continue;
			//Skip invalid directional lights
			if(light.getType() == LightType.DIRECTIONAL && light.direction.x == 0 && light.direction.y == 0 && light.direction.z == 0) continue;

			setParamVec3f("lights["+i+"].position", light.position);
			setParamVec3f("lights["+i+"].direction", light.direction);
			setParamF("lights["+i+"].radius", light.radius);
			setParamVec3f("lights["+i+"].color", light.color);
			i++;
			
			if(i >= 16) {
				Window.log.warn("Too many lights (16 max)");
				break;
			}
		}
		
		setParamI("activeLights", Math.min(i, 16));
	}
	
	private void initMaterial() {
		if(ready) return;
		ready = true;
		shader = Shader.loadShader(shaderPathV, shaderPathF);
	}
	
	/**
	 * Creates a copy of this material, mutable parameters are cloned too
	 */
	@Override
	public Material clone() {
		Material res = new Material(name, shaderPathV, shaderPathF);
		
		res.transparency = transparency;
		
		for(Entry<String, Integer> entry : paramsI.entrySet())
			res.paramsI.put(entry.getKey(), entry.getValue().intValue());
		
		for(Entry<String, Float> entry : paramsF.entrySet())
			res.paramsF.put(entry.getKey(), entry.getValue().floatValue());
		
		for(Entry<String, Vec2f> entry : paramsVec2f.entrySet())
			res.paramsVec2f.put(entry.getKey(), entry.getValue().clone());
		
		for(Entry<String, Vec3f> entry : paramsVec3f.entrySet())
			res.paramsVec3f.put(entry.getKey(), entry.getValue().clone());
		
		for(Entry<String, Vec4f> entry : paramsVec4f.entrySet())
			res.paramsVec4f.put(entry.getKey(), entry.getValue().clone());
		
		for(Entry<String, Mat4f> entry : paramsMat4f.entrySet())
			res.paramsMat4f.put(entry.getKey(), entry.getValue().clone());
		
		res.paramsTex = new HashMap<>(paramsTex);
		
		return res;
	}
}
