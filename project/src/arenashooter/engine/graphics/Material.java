package arenashooter.engine.graphics;

import java.io.IOException;
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
import arenashooter.engine.math.Mat4fi;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec2fi;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec3fi;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.math.Vec4fi;

public class Material implements MaterialI, Jsonable {
	private static Map<String, Material> cache = new HashMap<String, Material>();
	
	private Shader shader;
	private final String name, shaderPathV, shaderPathF;
	
	private boolean ready = false;
	
	/** Should this material be used during transparency pass */
	private boolean transparency = false;

	private Map<String, Integer> paramsI = new HashMap<>();
	private Map<String, Float> paramsF = new HashMap<>();
	private Map<String, Vec2fi> paramsVec2f = new HashMap<>();
	private Map<String, Vec3fi> paramsVec3f = new HashMap<>();
	private Map<String, Vec4fi> paramsVec4f = new HashMap<>();
	private Map<String, Mat4fi> paramsMat4f = new HashMap<>();
	private Map<String, Texture> paramsTex = new HashMap<>();
	
	private Material(String name, String shaderPathV, String shaderPathF) {
		this.name = name;
		this.shaderPathV = shaderPathV;
		this.shaderPathF = shaderPathF;
	}
	
	static Material loadMaterial(String path) {
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
	
	@Override
	public boolean getTransparency() {
		return transparency;
	}
	
	@Override
	public void setTransparency(boolean transparency) {
		this.transparency = transparency;
	}

	@Override
	public boolean bind(Model model) {
		if(!ready) initMaterial();
		if(shader == null) return false;
		
		shader.bind();
		model.bindToShader(shader);
		
		for(Entry<String, Integer> entry : paramsI.entrySet())
			shader.setUniformI(entry.getKey(), entry.getValue());
		
		for(Entry<String, Float> entry : paramsF.entrySet())
			shader.setUniformF(entry.getKey(), entry.getValue());
		
		for(Entry<String, Vec2fi> entry : paramsVec2f.entrySet())
			shader.setUniformV2(entry.getKey(), entry.getValue());
		
		for(Entry<String, Vec3fi> entry : paramsVec3f.entrySet())
			shader.setUniformV3(entry.getKey(), entry.getValue());
		
		for(Entry<String, Vec4fi> entry : paramsVec4f.entrySet())
			shader.setUniformV4(entry.getKey(), entry.getValue());
		
		for(Entry<String, Mat4fi> entry : paramsMat4f.entrySet())
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

	@Override
	public int getParamI(String name) {
		return paramsI.get(name);
	}

	@Override
	public void setParamI(String name, int value) {
		paramsI.put(name, value);
	}

	@Override
	public float getParamF(String name) {
		return paramsF.get(name);
	}

	@Override
	public void setParamF(String name, float value) {
		paramsF.put(name, value);
	}

	@Override
	public Vec2fi getParamVec2f(String name) {
		return paramsVec2f.get(name);
	}

	@Override
	public void setParamVec2f(String name, Vec2fi value) {
		paramsVec2f.put(name, value);
	}

	@Override
	public Vec3fi getParamVec3f(String name) {
		return paramsVec3f.get(name);
	}

	@Override
	public void setParamVec3f(String name, Vec3fi value) {
		paramsVec3f.put(name, value);
	}

	@Override
	public Vec4fi getParamVec4f(String name) {
		return paramsVec4f.get(name);
	}

	@Override
	public void setParamVec4f(String name, Vec4fi value) {
		paramsVec4f.put(name, value);
	}

	@Override
	public Mat4fi getParamMat4f(String name) {
		return paramsMat4f.get(name);
	}

	@Override
	public void setParamMat4f(String name, Mat4fi value) {
		paramsMat4f.put(name, value);
	}

	@Override
	public Texture getParamTex(String name) {
		return paramsTex.get(name);
	}

	@Override
	public void setParamTex(String name, Texture value) {
		paramsTex.put(name, value);
	}
	
	@Override
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
	
	@Override
	public Material clone() {
		Material res = new Material(name, shaderPathV, shaderPathF);
		
		res.transparency = transparency;
		
		for(Entry<String, Integer> entry : paramsI.entrySet())
			res.paramsI.put(entry.getKey(), entry.getValue().intValue());
		
		for(Entry<String, Float> entry : paramsF.entrySet())
			res.paramsF.put(entry.getKey(), entry.getValue().floatValue());
		
		for(Entry<String, Vec2fi> entry : paramsVec2f.entrySet())
			res.paramsVec2f.put(entry.getKey(), new Vec2f(entry.getValue()));
		
		for(Entry<String, Vec3fi> entry : paramsVec3f.entrySet())
			res.paramsVec3f.put(entry.getKey(), new Vec3f(entry.getValue()));
		
		for(Entry<String, Vec4fi> entry : paramsVec4f.entrySet())
			res.paramsVec4f.put(entry.getKey(), new Vec4f(entry.getValue()));
		
		for(Entry<String, Mat4fi> entry : paramsMat4f.entrySet())
			res.paramsMat4f.put(entry.getKey(), new Mat4f(entry.getValue()));
		
		res.paramsTex = new HashMap<>(paramsTex);
		
		return res;
	}
	
	
	/*
	 * JSON
	 */
	
	private JsonObject toJsonObject() {
		final JsonObject json = new JsonObject();
		json.put("type", "material");
		json.put("shaderVertex", shaderPathV);
		json.put("shaderFragment", shaderPathF);
		json.put("transparent", transparency);
		
		JsonObject jsonParams = new JsonObject();
		jsonParams.putAll(paramsI);
		jsonParams.putAll(paramsVec2f);
		jsonParams.putAll(paramsVec3f);
		jsonParams.putAll(paramsVec4f);
		jsonParams.putAll(paramsTex);
		
		//Special case for float parameters which are stored as an array with a single element
		for(Entry<String, Float> entry : paramsF.entrySet())
			jsonParams.put( entry.getKey(), new JsonArray().addChain(entry.getValue()) );
		
		json.put("params", jsonParams);
		
		return json;
	}

	@Override
	public String toJson() {
		return toJsonObject().toJson();
	}

	@Override
	public void toJson(Writer writable) throws IOException {
		toJsonObject().toJson(writable);
	}
}
