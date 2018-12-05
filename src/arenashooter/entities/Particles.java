package arenashooter.entities;

import arenashooter.engine.graphics.particles.ParticleSystem;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;

public class Particles extends Spatial {
	private ParticleSystem system;
	
	public Particles(Vec2f position) {
		this.position.set(position);
		system = new ParticleSystem( new Vec3f(position.x, position.y, 0) );
	}
	
	@Override
	public void step(double d) {
		system.position.x = position.x;
		system.position.y = position.y;
		system.update(d);
		super.step(d);
	}

	@Override
	public void draw() {
		system.draw();
		super.draw();
	}
}
