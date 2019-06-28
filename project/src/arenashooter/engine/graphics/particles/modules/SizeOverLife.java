package arenashooter.engine.graphics.particles.modules;

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
	public void apply(Emitter emitter, double delta, double worldRotation) {
		for(int i=0; i<emitter.lives.size(); i++)
			Vec2f.lerp(sizeStart, sizeEnd, emitter.lives.get(i)/emitter.livesTotal.get(i), emitter.scales.get(i));
	}

	@Override
	public ParticleModule clone() {
		return new SizeOverLife(sizeStart, sizeEnd);
	}

}
