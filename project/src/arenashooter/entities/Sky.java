package arenashooter.entities;

import java.util.Set;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.Profiler;
import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.Model;
import arenashooter.engine.json.StrongJsonKey;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec3fi;
import arenashooter.game.Main;

/**
 * World sky<br/>
 * This entity will fill the background with a vertical linear gradient when drawn
 */
public class Sky extends Entity {
	
	private static Model quad = null;
	public Material material;

	private Sky() {
		if(quad == null) quad = Main.getRenderer().loadQuad();
		material = Main.getRenderer().loadMaterial("data/materials/sky.material");
		zIndex = -9999;
	}
	
	public Sky(Vec3f colorBot, Vec3f colorTop) {
		this();
		setColors(colorBot, colorTop);
	}
	
	public void setColors(Vec3f newColorBot, Vec3f newColorTop) {
		setColorBot(newColorBot);
		setColorTop(newColorTop);
	}
	
	public void setColorBot(Vec3f newColorBot) {
		material.setParamVec3f("colorBot", newColorBot);
	}
	
	public Vec3fi getColorBot() {
		return material.getParamVec3f("colorBot");
	}
	
	public void setColorTop(Vec3f newColorTop) {
		material.setParamVec3f("colorTop", newColorTop);
	}
	
	public Vec3fi getColorTop() {
		return material.getParamVec3f("colorTop");
	}

	@Override
	public void draw(boolean transparency) {
		Profiler.startTimer(Profiler.SPRITES);
		
		if(material.bind(quad)) {
			quad.bind();
			quad.draw();
		}
		
		Profiler.endTimer(Profiler.SPRITES);
	}
	
	/**
	 * Creates a clone of this Sky (cloned material)
	 */
	@Override
	public Sky clone() {
		Sky clone = new Sky();
		clone.material = material.clone();
		return clone;
	}
	
	@Override
	public Set<StrongJsonKey> getJsonKey() {
		Set<StrongJsonKey> set = super.getJsonKey();
		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				return getColorTop();
			}
			@Override
			public String getKey() {
				return "colorTop";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				JsonArray a = json.getCollection(this);
				if (a != null)
					setColorTop(Vec3f.jsonImport(a));
			}
		});
		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				return getColorBot();
			}
			@Override
			public String getKey() {
				return "colorBottom";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				JsonArray a = json.getCollection(this);
				if (a != null)
					setColorBot(Vec3f.jsonImport(a));
			}
		});
		
		return set;
	}
	
	@Override
	protected JsonObject getJson() {
		JsonObject sky = super.getJson();
		sky.putChain("colorBottom", getColorBot());
		sky.putChain("colorTop", getColorTop());
		return sky;
	}

	public static Sky fromJson(JsonObject json) throws Exception {
		Sky sky = new Sky();
		useKeys(sky, json);
		return sky;
	}
}
