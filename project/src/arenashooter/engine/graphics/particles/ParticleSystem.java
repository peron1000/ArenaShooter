package arenashooter.engine.graphics.particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import arenashooter.engine.Profiler;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.xmlReaders.ParticlesXmlReader;

public class ParticleSystem {
	private static Map<String, EmitterTemplate[]> cache = new HashMap<>();

	private List<Emitter> emitters;
	/** System's world position */
	public Vec3f position;
	
	public Vec2f gravity = new Vec2f(0, 9.807);

	private ParticleSystem(EmitterTemplate[] data) {
		this.position = new Vec3f();
		emitters = new ArrayList<>();

		for (EmitterTemplate emitterData : data) {
			emitters.add(new Emitter(this, emitterData));
		}
	}

	public static ParticleSystem load(String path) {
		EmitterTemplate[] emitters = cache.get(path);

		if (emitters != null)
			return new ParticleSystem(emitters);

		emitters = ParticlesXmlReader.read(path);

		cache.put(path, emitters);

		return new ParticleSystem(emitters);
	}

	public void update(double delta, Vec2f gravity, double worldRotation) {
		this.gravity.set(gravity);
		
		for (int i = emitters.size() - 1; i >= 0; i--) {
			if (emitters.get(i).update(delta, gravity, worldRotation))
				emitters.remove(i);
		}
	}

	public boolean ended() {
		return emitters.isEmpty();
	}

	public void draw() {
		Profiler.startTimer(Profiler.PARTICLES);
		for (Emitter e : emitters)
			e.draw();
		Profiler.endTimer(Profiler.PARTICLES);
	}
}
