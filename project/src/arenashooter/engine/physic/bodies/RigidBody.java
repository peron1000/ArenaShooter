package arenashooter.engine.physic.bodies;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.Body;

/**
 * Fully simulated object
 */
public class RigidBody extends Body {
	public double mass;
	
	public Vec2f velocity;
	public double radialVel;
	
	public void process(double d) {
		position.add(Vec2f.multiply(velocity, d));
		rotation += radialVel;
	}

}
