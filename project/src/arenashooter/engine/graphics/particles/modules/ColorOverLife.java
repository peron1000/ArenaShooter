package arenashooter.engine.graphics.particles.modules;

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
	public void apply(Emitter emitter, double delta, double worldRotation) {
		for(int i=0; i<emitter.lives.size(); i++)
			Vec4f.lerp(colorStart, colorEnd, emitter.lives.get(i)/emitter.livesTotal.get(i), emitter.colors.get(i));
	}

	@Override
	public ParticleModule clone() {
		return new ColorOverLife(colorStart, colorEnd);
	}

}
