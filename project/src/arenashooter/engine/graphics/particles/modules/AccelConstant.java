package arenashooter.engine.graphics.particles.modules;

import arenashooter.engine.graphics.particles.Emitter;
import arenashooter.engine.math.Vec2f;

public class AccelConstant extends ParticleModule {

	private final Vec2f accel;
	
	public AccelConstant(Vec2f accel) {
		this.accel = accel;
	}

	@Override
	public void apply(Emitter emitter, double delta, double worldRotation) {
		Vec2f force = accel.clone().multiply((float) delta);
		for(Vec2f position : emitter.positions)
			position.add(force);
	}

	@Override
	public AccelConstant clone() {
		return new AccelConstant(accel);
	}

}
