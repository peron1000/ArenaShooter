package arenashooter.engine.graphics.particles.modules;

import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.graphics.particles.Emitter;
import arenashooter.engine.math.Vec4f;

/**
 * Linearly interpolate over two colors based on particles lifetime
 */
public class ColorOverLife extends ParticleModule {
	private final Vec4f colorStart, colorEnd;

	public ColorOverLife(Vec4f colorStart, Vec4f colorEnd) {
		this.colorStart = colorStart;
		this.colorEnd = colorEnd;
	}

	@Override
	public void apply(Emitter emitter, double delta) {
		for(int i=0; i<emitter.lives.size(); i++)
			Vec4f.lerp(colorStart, colorEnd, emitter.lives.get(i)/emitter.livesTotal.get(i), emitter.colors.get(i));
	}

	@Override
	public ParticleModule clone() {
		return new ColorOverLife(colorStart, colorEnd);
	}
	
	
	/*
	 * JSON
	 */
	
	public static final String jsonType = "color over life";

	@Override
	JsonObject toJsonObject() {
		return new JsonObject().putChain("type", jsonType).putChain("color start", colorStart).putChain("color end", colorEnd);
	}

	public static ColorOverLife fromJson(JsonObject json) {
		return new ColorOverLife( Vec4f.jsonImport(json.getCollectionOrDefault(Keys.colorStart)), Vec4f.jsonImport(json.getCollectionOrDefault(Keys.colorEnd)) );
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
		colorStart {
			@Override
			public String getKey() {
				return "color start";
			}
			@Override
			public Vec4f getValue() {
				return new Vec4f(1, 1, 1, 1);
			}
		},
		colorEnd {
			@Override
			public String getKey() {
				return "color end";
			}
			@Override
			public Vec4f getValue() {
				return new Vec4f(1, 1, 1, 0);
			}
		}
	}

}
