package arenashooter.engine.graphics.particles.modules;

import java.io.IOException;
import java.io.Writer;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;

import arenashooter.engine.graphics.particles.Emitter;

public abstract class ParticleModule implements Jsonable {
	public abstract void apply(Emitter emitter, double delta);
	
	@Override
	public abstract ParticleModule clone();
	
	
	/*
	 * JSON
	 */
	
	abstract JsonObject toJsonObject();
	
	@Override
	public String toJson() {
		return toJsonObject().toJson();
	}

	@Override
	public void toJson(Writer writable) throws IOException {
		toJsonObject().toJson(writable);
	}
}
