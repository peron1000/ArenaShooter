package arenashooter.engine.physic;

import java.util.ArrayList;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.bodies.StaticBody;

public final class Physic {
	private static ArrayList<StaticBody> staticbodies = new ArrayList<StaticBody>();
	private static ArrayList<RigidBody> rigidBodies = new ArrayList<RigidBody>();
	
	public static Vec2f globalForce = new Vec2f();
	
	//This class cannot be instantiated
	private Physic() {}
	
	public static void step(double d) {
		for( RigidBody rigid : rigidBodies ) {
			rigid.process(d);
		}
	}
	
	public static void registerRigidBody(RigidBody body) {
		rigidBodies.add(body);
	}
	
	public static void registerStaticBody(StaticBody body) {
		staticbodies.add(body);
	}
}
