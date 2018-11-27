package arenashooter.entities;

import arenashooter.engine.math.Vec2d;
import arenashooter.engine.math.Vec3f;

public class Plateform extends Spatial {
	

	public Plateform(Vec2d extent, Vec3f rgb) {
		Collider coll = new Collider(extent);
		coll.extent = extent;
		attachToParent(coll, "collider");
		
		Sprite spr = new Sprite();
		//TODO: passer sprite & rgb
		attachToParent(spr, "sprite");
	}
}
