package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.bodies.RigidBody;

public abstract class Projectile extends RigidBodyContainer {

	public Character shooter;

//	boolean disp = false;
	Vec2f vel = new Vec2f();
//	Collider collider;
	float damage; //Un projectile n'a pas forcément de damage.
	
	public abstract void impact(Spatial other);

	public boolean isShooter(Character dude) {
			return dude == shooter;
	}

	public Projectile(Vec2f position, RigidBody body) {
		super(position, body);
	}

}
