package arenashooter.engine.physic;

import java.util.ArrayList;

import arenashooter.engine.physic.bodies.RigidBody;

public final class Physic {
	private ArrayList<Body> bodies = new ArrayList<Body>();
	private ArrayList<RigidBody> rigidBodies = new ArrayList<RigidBody>();
	
	//This class cannot be instantiated
	private Physic() {}
	
	public void step(double d) {
		for( RigidBody rigid : rigidBodies ) {
			rigid.process(d);
		}
	}
}
