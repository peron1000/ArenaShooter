package arenashooter.engine.graphics.particles.modules;

import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.graphics.particles.Emitter;
import arenashooter.engine.math.Vec2f;

/**
 * Spawned particles will be placed on a circle of random radius between radiusMin and radiusMax
 */
public class InitialPosCircle extends ParticleModule {
	private final float radiusMin, radiusDelta;
	
	public InitialPosCircle(float radiusMin, float radiusMax) {
		this.radiusMin = radiusMin;
		this.radiusDelta = radiusMax-radiusMin;
	}

	@Override
	public void apply(Emitter emitter, double delta) {
		for(int i=0; i<emitter.lives.size(); i++) {
			if(emitter.lives.get(i) != 0) continue;
			
			emitter.positions.get(i).set(radiusMin+(Math.random()*radiusDelta), 0);
			Vec2f.rotate(emitter.positions.get(i), Math.random()*Math.PI*2, emitter.positions.get(i));
			emitter.positions.get(i).add(emitter.owner.position.x, emitter.owner.position.y);
		}
	}

	@Override
	public InitialPosCircle clone() {
		return new InitialPosCircle(radiusMin, radiusMin+radiusDelta);
	}
	
	
	/*
	 * JSON
	 */
	
	public static final String jsonType = "initial pos circle";
	
	@Override
	JsonObject toJsonObject() {
		return new JsonObject().putChain("type",jsonType).putChain("radius min", radiusMin).putChain("radius max", radiusMin+radiusDelta);
	}
	
	public static InitialPosCircle fromJson(JsonObject json) {
		return new InitialPosCircle( json.getFloatOrDefault(Keys.radiusMin), json.getFloatOrDefault(Keys.radiusMax) );
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
		radiusMin {
			@Override
			public String getKey() {
				return "radius min";
			}
			@Override
			public Float getValue() {
				return 0f;
			}
		},
		radiusMax {
			@Override
			public String getKey() {
				return "radius max";
			}
			@Override
			public Float getValue() {
				return 1f;
			}
		}
	}

}
