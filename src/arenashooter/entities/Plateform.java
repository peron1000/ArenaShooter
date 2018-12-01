package arenashooter.entities;

import arenashooter.engine.math.Vec2f;

public class Plateform extends Spatial {
	

	public Plateform(Vec2f extent) {
		Collider coll = new Collider(extent);
		coll.extent = extent;
		coll.attachToParent(this, "collider");
		
		Sprite spr = new Sprite();
		spr.size = new Vec2f(extent.x*2, extent.y*2);
		//TODO: passer sprite & rgb
		spr.attachToParent(this, "sprite");
	}
	
	@Override
	public void step(double d) {
		((Spatial)children.get("sprite")).position = position;
		((Spatial)children.get("collider")).position = position;
		super.step(d);
	}
}
