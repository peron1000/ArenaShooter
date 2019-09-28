package arenashooter.engine.graphics.particles.modules;

import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.graphics.particles.Emitter;
import arenashooter.engine.math.Vec2f;

public class Drag extends ParticleModule {
	private final float strength;

	public Drag(float strength) {
		this.strength = strength;
	}

	@Override
	public void apply(Emitter emitter, double delta) {
		if(strength == 0) return;
		float factor = (float) (1/(delta*strength));
		for(Vec2f vel : emitter.velocities)
			vel.multiply(factor);
	}

	@Override
	public ParticleModule clone() {
		return new Drag(strength);
	}
	
	
	/*
	 * JSON
	 */
	
	public static final String jsonType = "drag";

	@Override
	JsonObject toJsonObject() {
		return new JsonObject().putChain("type", jsonType).putChain("strength", strength);
	}
	
	public static Drag fromJson(JsonObject json) {
		return new Drag(json.getFloatOrDefault(Keys.strength));
	}
	
	private enum Keys implements JsonKey {
		type {
			@Override
			public String getKey() {
				return "type";
			}
			@Override
			public String getValue() {
				return jsonType;
			}
		},
		strength {
			@Override
			public String getKey() {
				return "strength";
			}
			@Override
			public Float getValue() {
				return 1.0f;
			}
		}
	}

}
