package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.Impact;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.entities.Collider;
import arenashooter.entities.Entity;

public class RigidBodyContainer extends Spatial {

	private RigidBody body;

	public RigidBodyContainer(Vec2f position, RigidBody body) {
		super(position);
		this.body = body;
	}
	
	@Override
	public void step(double d) {
		position.set(body.position);
		rotation = body.rotation;
		
		super.step(d);
	}
	
}
