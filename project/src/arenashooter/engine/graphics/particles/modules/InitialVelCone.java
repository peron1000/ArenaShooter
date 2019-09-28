package arenashooter.engine.graphics.particles.modules;

import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.graphics.particles.Emitter;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;

/**
 * Spawned particles will be placed on a circle of random radius between radiusMin and radiusMax
 */
public class InitialVelCone extends ParticleModule {
	private final float angleMin, angleMax, velMin, velMax;
	
	public InitialVelCone(float angleMin, float angleMax, float velMin, float velMax) {
		this.angleMin = angleMin;
		this.angleMax = angleMax;
		this.velMin = velMin;
		this.velMax = velMax;
	}

	@Override
	public void apply(Emitter emitter, double delta) {
		for(int i=0; i<emitter.lives.size(); i++) {
			if(emitter.lives.get(i) != 0) continue;
			
			emitter.velocities.get(i).set( Utils.lerpF(velMin, velMax, Math.random()), 0 );
			Vec2f.rotate(emitter.velocities.get(i), Utils.lerpF(angleMin, angleMax, Math.random()) + emitter.owner.rotation, emitter.velocities.get(i));
		}
	}

	@Override
	public InitialVelCone clone() {
		return new InitialVelCone(angleMin, angleMax, velMin, velMax);
	}
	
	
	/*
	 * JSON
	 */
	
	public static final String jsonType = "initial vel cone";

	@Override
	JsonObject toJsonObject() {
		return new JsonObject().putChain("type", jsonType).putChain("angle min", angleMin).putChain("angle max", angleMax).putChain("vel min", velMin).putChain("vel max", velMax);
	}
	
	public static InitialVelCone fromJson(JsonObject json) {
		return new InitialVelCone( json.getFloatOrDefault(Keys.angleMin), json.getFloatOrDefault(Keys.angleMax), json.getFloatOrDefault(Keys.velMin), json.getFloatOrDefault(Keys.velMax) );
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
		angleMin {
			@Override
			public String getKey() {
				return "angle min";
			}
			@Override
			public Float getValue() {
				return -1f;
			}
		},
		angleMax {
			@Override
			public String getKey() {
				return "angle max";
			}
			@Override
			public Float getValue() {
				return 1f;
			}
		},
		velMin {
			@Override
			public String getKey() {
				return "vel min";
			}
			@Override
			public Float getValue() {
				return 1f;
			}
		},
		velMax {
			@Override
			public String getKey() {
				return "vel max";
			}
			@Override
			public Float getValue() {
				return 10f;
			}
		}
	}

}
