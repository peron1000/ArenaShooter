package arenashooter.entities;

import arenashooter.engine.math.Vec2f;

public class Plateform extends Spatial {
	

	public Plateform(Vec2f position, Vec2f extent) {
		super(position);
		Collider coll = new Collider(this.position, extent);
		coll.extent = extent;
		coll.attachToParent(this, "collider");
		
		//TODO: passer sprite & rgb
		Sprite spr = new Sprite(position);
		spr.size = new Vec2f(extent.x*2, extent.y*2);
		spr.attachToParent(this, "sprite");
	}
	
	@Override
	public void step(double d) {
		((Spatial)children.get("sprite")).position = position;
		((Spatial)children.get("collider")).position = position;
		super.step(d);
	}
}
