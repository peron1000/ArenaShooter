package arenashooter.engine.graphics.particles.modules;

import arenashooter.engine.graphics.particles.Emitter;

public abstract class ParticleModule {
	public abstract void apply(Emitter emitter, double delta);
	
	@Override
	public abstract ParticleModule clone();
}
