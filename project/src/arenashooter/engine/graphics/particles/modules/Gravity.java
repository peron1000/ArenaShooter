package arenashooter.engine.graphics.particles.modules;

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
	public void apply(Emitter emitter, double delta, double worldRotation) {
		Vec2f force = emitter.owner.gravity.clone().multiply(scale).multiply((float) delta);
		for(Vec2f vel : emitter.velocities)
			vel.add(force);
	}

	@Override
	public Gravity clone() {
		return new Gravity(scale);
	}

}
