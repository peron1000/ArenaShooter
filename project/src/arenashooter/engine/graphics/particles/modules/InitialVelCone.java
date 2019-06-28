package arenashooter.engine.graphics.particles.modules;

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

}
