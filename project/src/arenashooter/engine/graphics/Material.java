package arenashooter.engine.graphics;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;

import static org.lwjgl.opengl.GL20.GL_TEXTURE0;
import static org.lwjgl.opengl.GL20.glActiveTexture;

import arenashooter.engine.graphics.Light.LightType;
import arenashooter.engine.json.MaterialJsonReader;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;

public class Material implements Jsonable {
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
		
		MaterialJsonReader reader = new MaterialJsonReader(path);
		
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
		
		int lightsDir = 0, lightsPoint = 0, lightsSpot = 0;
		for(Light light : lights) {
			//Skip 0-radius lights
			if(light.radius == 0) continue;
			//Skip black lights
			if(light.color.x == 0 && light.color.y == 0 && light.color.z == 0) continue;
			//Skip invalid directional lights
			if(light.getType() == LightType.DIRECTIONAL && light.direction.x == 0 && light.direction.y == 0 && light.direction.z == 0) continue;
			
			switch(light.getType()) {
			case DIRECTIONAL:
				setParamVec3f("lightsDir["+lightsDir+"].color", light.color);
				setParamVec3f("lightsDir["+lightsDir+"].direction", light.direction);
				lightsDir++;
				break;
			case POINT:
				setParamVec3f("lightsPoint["+lightsPoint+"].color", light.color);
				setParamVec3f("lightsPoint["+lightsPoint+"].position", light.position);
				setParamF("lightsPoint["+lightsPoint+"].radius", light.radius);
				lightsPoint++;
				break;
			case SPOT:
				setParamVec3f("lightsSpot["+lightsSpot+"].color", light.color);
				setParamVec3f("lightsSpot["+lightsSpot+"].position", light.position);
				setParamF("lightsSpot["+lightsSpot+"].radius", light.radius);
				setParamVec3f("lightsSpot["+lightsSpot+"].direction", light.direction);
				setParamF("lightsSpot["+lightsSpot+"].angle", light.angle);
				lightsSpot++;
				break;
			}
			
			if(lightsDir >= 2)
				Window.log.warn("Too many directional lights (2 max)");
			else if(lightsPoint >= 8)
				Window.log.warn("Too many point lights (8 max)");
			else if(lightsSpot >= 8)
				Window.log.warn("Too many spot lights (8 max)");
		}
		
		setParamI("activeLightsDir", Math.min(lightsDir, 2));
		setParamI("activeLightsPoint", Math.min(lightsPoint, 8));
		setParamI("activeLightsSpot", Math.min(lightsSpot, 8));
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

	@Override
	public String toJson() {
		final StringWriter writable = new StringWriter();
		try {
			this.toJson(writable);
		} catch (final IOException e) {
			e.printStackTrace();
		}
        return writable.toString();
	}

	@Override
	public void toJson(Writer writable) throws IOException { //TODO: Test
		final JsonObject json = new JsonObject();
		json.put("type", "material");
		json.put("shaderVertex", shaderPathV);
		json.put("shaderFragment", shaderPathF);
		json.put("transparent", transparency);
		
		JsonObject jsonParams = new JsonObject();
		jsonParams.putAll(paramsI);
		
		for(Entry<String, Float> entry : paramsF.entrySet()) {
			JsonArray jsonArray = new JsonArray();
			jsonArray.add(entry.getValue());
			jsonParams.put(entry.getKey(), jsonArray);
		}

		for(Entry<String, Vec2f> entry : paramsVec2f.entrySet()) {
			JsonArray jsonArray = new JsonArray();
			jsonArray.add(entry.getValue().x);
			jsonArray.add(entry.getValue().y);
			jsonParams.put(entry.getKey(), jsonArray);
		}
		
		for(Entry<String, Vec3f> entry : paramsVec3f.entrySet()) {
			JsonArray jsonArray = new JsonArray();
			jsonArray.add(entry.getValue().x);
			jsonArray.add(entry.getValue().y);
			jsonArray.add(entry.getValue().z);
			jsonParams.put(entry.getKey(), jsonArray);
		}
		
		for(Entry<String, Vec4f> entry : paramsVec4f.entrySet()) {
			JsonArray jsonArray = new JsonArray();
			jsonArray.add(entry.getValue().x);
			jsonArray.add(entry.getValue().y);
			jsonArray.add(entry.getValue().z);
			jsonArray.add(entry.getValue().w);
			jsonParams.put(entry.getKey(), jsonArray);
		}
		
		for(Entry<String, Texture> entry : paramsTex.entrySet()) {
			JsonObject jsonTexture = new JsonObject();
			jsonTexture.put("path", entry.getValue().getPath());
			jsonTexture.put("filtered", entry.getValue().isFiltered());
			jsonParams.put(entry.getKey(), jsonTexture);
		}
		
		json.put("params", jsonParams);
	}
}
