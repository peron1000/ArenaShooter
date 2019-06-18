package arenashooter.engine.graphics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import arenashooter.engine.graphics.Shader.ParamType;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;

public class Material {
	private final Shader shader;
	private final String shaderPath;
	
	private static final Set<String> ignoredNames = new HashSet<>();
	static {
		ignoredNames.add("time");
		ignoredNames.add("ambient");
		ignoredNames.add("activeLights");
		ignoredNames.add("fogColor");
		ignoredNames.add("fogDistance");
	}

	private Map<String, Integer> paramsI = new HashMap<>();
	private Map<String, Float> paramsF = new HashMap<>();
	private Map<String, Vec2f> paramsVec2f = new HashMap<>();
	private Map<String, Vec3f> paramsVec3f = new HashMap<>();
	private Map<String, Vec4f> paramsVec4f = new HashMap<>();
	private Map<String, Texture> paramsTex = new HashMap<>();
	
	public Mat4f model = null, view = null, proj = null;
	
	public Material(String shaderPath) {
		this.shaderPath = shaderPath;
		this.shader = Shader.loadShader(shaderPath);
		loadDefaults();
	}
	
	/**
	 * Copy default values from shader
	 */
	private void loadDefaults() {
		Set<String> uniforms = shader.getUniformNames();
		
		for(String name : uniforms) {
			ParamType type = shader.getUniformType(name);
			
			if(type == null) {
				if(!ignoredNames.contains(name))
					Window.log.error("No uniform named \""+name+"\" in "+shaderPath);
				continue;
			}
			
			switch(type) {
			case INT:
				setParamI(name, shader.defaultsParamsI.get(name).intValue());
				break;
			case FLOAT:
				setParamF(name, shader.defaultsParamsF.get(name).floatValue());
				break;
			case VEC2F:
				setParamVec2f(name, shader.defaultsParamsVec2f.get(name).clone());
				break;
			case VEC3F:
				setParamVec3f(name, shader.defaultsParamsVec3f.get(name).clone());
				break;
			case VEC4F:
				setParamVec4f(name, shader.defaultsParamsVec4f.get(name).clone());
				break;
			default:
				Window.log.debug("No default value for shader parameter "+name+" (type "+type+")");
				break;
			}
		}
	}
	
	public void bind(Model model) {
		shader.bind();
		model.bindToShader(shader);
		
		if(this.model != null) shader.setUniformM4("model", this.model);
		if(this.view!= null) shader.setUniformM4("view", view);
		if(this.proj != null) shader.setUniformM4("projection", proj);

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
		
		int texSlot = 0;
		for(Entry<String, Texture> entry : paramsTex.entrySet()) {
//			glActiveTexture(GL_TEXTURE0);
			entry.getValue().bind();
			shader.setUniformI(entry.getKey(), texSlot);
			texSlot++;
		}
	}
	
	/**
	 * Check if the type of a parameter corresponds an expected type. If not, log a type check error
	 * @param name parameter name
	 * @param expected expected type
	 * @return if the parameter type matches the expected type
	 */
	private boolean checkType(String name, ParamType expected) {
		ParamType type = shader.getUniformType(name);
		if( type == expected ) return true;
		
		if(expected != ParamType.INT && !ignoredNames.contains(name)) //Don't print an error for the time value
			Window.log.warn("Type check error: uniform \""+name+"\" is "+type+", expected "+expected+" (in "+shaderPath+")");
		return false;
	}
	
	public int getParamI(String name) {
		checkType(name, ParamType.INT);
		return paramsI.get(name);
	}

	public void setParamI(String name, int value) {
		if(checkType(name, ParamType.INT))
			paramsI.put(name, value);
	}
	
	public float getParamF(String name) {
		checkType(name, ParamType.FLOAT);
		return paramsF.get(name);
	}

	public void setParamF(String name, float value) {
		if(checkType(name, ParamType.FLOAT))
			paramsF.put(name, value);
	}
	
	public Vec2f getParamVec2f(String name) {
		checkType(name, ParamType.VEC2F);
		return paramsVec2f.get(name);
	}
	
	public void setParamVec2f(String name, Vec2f value) {
		if(checkType(name, ParamType.VEC2F))
			paramsVec2f.put(name, value);
	}
	
	public Vec3f getParamVec3f(String name) {
		checkType(name, ParamType.VEC3F);
		return paramsVec3f.get(name);
	}

	public void setParamVec3f(String name, Vec3f value) {
		if(checkType(name, ParamType.VEC3F))
			paramsVec3f.put(name, value);
	}
	
	public Vec4f getParamVec4f(String name) {
		checkType(name, ParamType.VEC4F);
		return paramsVec4f.get(name);
	}

	public void setParamVec4f(String name, Vec4f value) {
		if(checkType(name, ParamType.VEC4F))
			paramsVec4f.put(name, value);
	}
	
	public Texture getParamTex(String name) {
		checkType(name, ParamType.TEXTURE2D);
		return paramsTex.get(name);
	}

	public void setParamTex(String name, Texture value) {
		if(checkType(name, ParamType.TEXTURE2D))
			paramsTex.put(name, value);
	}
	
	/**
	 * Set lights used on this materials
	 * @param lights
	 */
	public void setLights(Set<Light> lights) {
		int i = 0;
		for(Light light : lights) {
			//Skip 0-radius lights
			if(light.radius == 0) continue;
			//Skip black lights
			if(light.color.x == 0 && light.color.y == 0 && light.color.z == 0) continue;
			//Skip invalid directional lights
			if(light.radius < 0 && light.position.x == 0 && light.position.y == 0 && light.position.z == 0) continue;

			setParamVec3f("lights["+i+"].position", light.position);
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
	
	/**
	 * Creates a copy of this material, mutable parameters are cloned too
	 */
	@Override
	public Material clone() {
		Material res = new Material(shaderPath);
		if(model != null)
			res.model = model.clone();
		if(view != null)
			res.view = view.clone();
		if(proj != null)
			res.proj = proj.clone();
		
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
		
		res.paramsTex = new HashMap<>(paramsTex);
		
		return res;
	}
}
