package arenashooter.engine.physic;

import java.util.ArrayList;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.bodies.RigidBody;

public final class Physic {
	private static ArrayList<Body> bodies = new ArrayList<Body>();
	private static ArrayList<RigidBody> rigidBodies = new ArrayList<RigidBody>();
	
	public static Vec2f globalForce = new Vec2f();
	
	//This class cannot be instantiated
	private Physic() {}
	
	public static void step(double d) {
		for( RigidBody rigid : rigidBodies ) {
			rigid.process(d);
		}
	}
	
	public static void registerBody(Body body) {
		bodies.add(body);
		if( body instanceof RigidBody ) rigidBodies.add((RigidBody)body);
	}
}
