package arenashooter.entities.spatials;

import arenashooter.engine.graphics.particles.ParticleSystem;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;

public class Particles extends Spatial {
	private ParticleSystem system;
	
	private boolean locked = false;
	public double lockedAngle = 0;
	public boolean selfDestruct = false;
	
	public Particles(Vec2f position, String path) {
		super(position);
		system = ParticleSystem.load(path);
		system.position = new Vec3f(getWorldPos().x, getWorldPos().y, 0);
	}
	
	public Particles(Vec2f position, String path, double lockAngle) {
		super(position);
		system = ParticleSystem.load(path);
		system.position = new Vec3f(getWorldPos().x, getWorldPos().y, 0);
		locked = true;
		this.lockedAngle = lockAngle;
	}
	
	/*
	 * Locks rotation to a certain angle
	 */
	public void lockRotation(double lockAngle) {
		locked = true;
		lockedAngle = lockAngle;
	}
	
	/*
	 * Unlock rotation to let it follow parent's World Rotation
	 */
	public void unlockRotation() {
		locked = false;
	}
	
	@Override
	public void step(double d) {
		system.position.x = getWorldPos().x;
		system.position.y = getWorldPos().y;
		if(getArena() == null)
			system.update(d, new Vec2f(), (locked ? lockedAngle : getWorldRot()) );
		else
			system.update(d, getArena().gravity, (locked ? lockedAngle : getWorldRot()) );
		
		if(selfDestruct && system.ended()) detach();
		
		super.step(d);
	}

	@Override
	public boolean drawAsTransparent() { return true; } //TODO: Remove this
	
	@Override
	public void draw(boolean transparency) {
		system.draw();
	}
}
