package arenashooter.engine.graphics.particles.modules;

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
	public void apply(Emitter emitter, double delta, double worldRotation) {
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

}
