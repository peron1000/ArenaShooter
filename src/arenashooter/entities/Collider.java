package arenashooter.entities;

import arenashooter.engine.math.Vec2d;

public class Collider extends Spatial {
	Vec2d extent;
	
	boolean testColl(Collider other) {
		//TODO: faire, merci
		return false;
	}

	public Collider(Vec2d extent) {
		super();
		this.extent = extent;
	}
}
