package arenashooter.entities;

import arenashooter.engine.math.Vec2f;

public class Collider extends Spatial {
	Vec2f extent;
	
	boolean testColl(Collider other) {
		//TODO: faire, merci
		return false;
	}

	public Collider(Vec2f extent) {
		super();
		this.extent = extent;
	}
}
