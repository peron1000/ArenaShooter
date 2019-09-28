package arenashooter.engine.graphics.particles.modules;

import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.graphics.particles.Emitter;
import arenashooter.engine.math.Vec2f;

/**
 * Linearly interpolate over two scales based on particles lifetime
 */
public class SizeOverLife extends ParticleModule {
	private final Vec2f sizeStart, sizeEnd;

	public SizeOverLife(Vec2f sizeStart, Vec2f sizeEnd) {
		this.sizeStart = sizeStart;
		this.sizeEnd = sizeEnd;
	}

	@Override
	public void apply(Emitter emitter, double delta) {
		for(int i=0; i<emitter.lives.size(); i++)
			Vec2f.lerp(sizeStart, sizeEnd, emitter.lives.get(i)/emitter.livesTotal.get(i), emitter.scales.get(i));
	}

	@Override
	public ParticleModule clone() {
		return new SizeOverLife(sizeStart, sizeEnd);
	}
	
	
	/*
	 * JSON
	 */
	
	public static final String jsonType = "size over life";

	@Override
	JsonObject toJsonObject() {
		return new JsonObject().putChain("type", jsonType).putChain("size start", sizeStart).putChain("size end", sizeEnd);
	}
	
	public static SizeOverLife fromJson(JsonObject json) {
		return new SizeOverLife( Vec2f.jsonImport(json.getCollectionOrDefault(Keys.sizeStart)), Vec2f.jsonImport(json.getCollectionOrDefault(Keys.sizeEnd)) );
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
		sizeStart {
			@Override
			public String getKey() {
				return "size start";
			}
			@Override
			public Vec2f getValue() {
				return new Vec2f(1);
			}
		},
		sizeEnd {
			@Override
			public String getKey() {
				return "size end";
			}
			@Override
			public Vec2f getValue() {
				return new Vec2f(0);
			}
		}
	}

}
