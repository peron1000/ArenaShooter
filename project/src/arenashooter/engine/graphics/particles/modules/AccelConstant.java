package arenashooter.engine.graphics.particles.modules;

import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.graphics.particles.Emitter;
import arenashooter.engine.math.Vec2f;

public class AccelConstant extends ParticleModule {

	private final Vec2f accel;
	
	public AccelConstant(Vec2f accel) {
		this.accel = accel;
	}

	@Override
	public void apply(Emitter emitter, double delta) {
		Vec2f force = accel.clone().multiply((float) delta);
		for(Vec2f position : emitter.positions)
			position.add(force);
	}

	@Override
	public AccelConstant clone() {
		return new AccelConstant(accel);
	}
	
	
	/*
	 * JSON
	 */
	
	public static final String jsonType = "accel constant";
	
	@Override
	JsonObject toJsonObject() {
		return new JsonObject().putChain("type", jsonType).putChain("accel", accel);
	}

	public static AccelConstant fromJson(JsonObject json) {
		return new AccelConstant(Vec2f.jsonImport(json.getCollectionOrDefault(Keys.accel)));
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
		accel {
			@Override
			public String getKey() {
				return "accel";
			}
			@Override
			public Vec2f getValue() {
				return new Vec2f(0, 1);
			}
		}
	}
}
