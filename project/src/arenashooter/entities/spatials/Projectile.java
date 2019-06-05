package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.bodies.RigidBody;

public abstract class Projectile extends RigidBodyContainer {

	public Character shooter;

	Vec2f vel = new Vec2f();
	float damage; //A projectile doesn't necessarily have damage
	
	public abstract void impact(Spatial other);

	public boolean isShooter(Character dude) {
			return dude == shooter;
	}

	public Projectile(RigidBody body) {
		super(body);
	}

}
