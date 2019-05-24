package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2f;

public class Explosion extends Spatial {
	public Explosion(Vec2f position) {
		super(position);
		Particles particles = new Particles(position, "data/particles/explosion.xml");
		particles.attachToParent(this, "particles");
	}

}
