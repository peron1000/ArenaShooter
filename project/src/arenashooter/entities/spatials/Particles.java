package arenashooter.entities.spatials;

import arenashooter.engine.graphics.particles.ParticleSystem;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;

public class Particles extends Spatial {
	private ParticleSystem system;
	
	public boolean selfDestruct = false;
	
	public Particles(Vec2f position, String path) {
		super(position);
		system = ParticleSystem.load(path);
		system.position = new Vec3f(position.x, position.y, 0);
	}
	
	@Override
	public void step(double d) {
		system.position.x = getWorldPos().x;
		system.position.y = getWorldPos().y;
		system.update(d);
		
		if(selfDestruct && system.ended()) detach();
		
		super.step(d);
	}

	@Override
	public boolean drawAsTransparent() { return true; } //TODO: Remove this
	
	@Override
	public void draw() {
		system.draw();
		super.draw();
	}
}
