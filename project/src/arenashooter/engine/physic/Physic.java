package arenashooter.engine.physic;

import java.util.ArrayList;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.OrientedRect;

public final class Physic {
	private static ArrayList<Body> bodies = new ArrayList<Body>();
	private static ArrayList<RigidBody> rigidBodies = new ArrayList<RigidBody>();
	
	//This class cannot be instantiated
	private Physic() {}
	
	public static void step(double d) {
		for( RigidBody rigid : rigidBodies ) {
			rigid.process(d);
		}
	}
	
	public static void main(String[] args) {
		System.out.println("Running physics tests");
		RigidBody testBody = new RigidBody(new OrientedRect(new Vec2f(10, 5)), new Vec2f(), 0, 1000);
		rigidBodies.add(testBody);
		testBody.applyForce(new Vec2f(5, 5), new Vec2f(10, 0));
		while(true) {
			step(.1);
			System.out.println(testBody.position);
			System.out.println(testBody.rotation);
		}
	}
}
