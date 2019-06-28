package arenashooter.engine.graphics.particles.modules;

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

}
