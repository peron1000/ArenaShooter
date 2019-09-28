package arenashooter.engine.graphics.particles.modules;

import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.graphics.particles.Emitter;
import arenashooter.engine.math.Vec2f;

/**
 * Apply gravity on particles
 */
public class Gravity extends ParticleModule {

	private final float scale;
	
	public Gravity(float gravityScale) {
		this.scale = gravityScale;
	}

	@Override
	public void apply(Emitter emitter, double delta) {
		Vec2f force = emitter.owner.gravity.clone().multiply(scale).multiply((float) delta);
		for(Vec2f vel : emitter.velocities)
			vel.add(force);
	}

	@Override
	public Gravity clone() {
		return new Gravity(scale);
	}
	
	
	/*
	 * JSON
	 */
	
	public static final String jsonType = "gravity";

	@Override
	JsonObject toJsonObject() {
		return new JsonObject().putChain("type", jsonType).putChain("gravity scale", scale);
	}
	
	public static Gravity fromJson(JsonObject json) {
		return new Gravity( json.getFloatOrDefault(Keys.scale) );
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
		scale {
			@Override
			public String getKey() {
				return "gravity scale";
			}
			@Override
			public Float getValue() {
				return 1f;
			}
		}
	}

}
