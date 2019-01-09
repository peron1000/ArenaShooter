package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Collider;

public class Plateform extends Spatial {
	
	private Vec2f extend;
	

	public Plateform(Vec2f position, Vec2f extent) {
		super(position);
		Collider coll = new Collider(this.position, extent);
		coll.extent = extent;
		this.extend = extent;
		coll.attachToParent(this, "collider");
		
		//TODO: passer sprite & rgb
		Sprite spr = new Sprite(position);
		spr.size = new Vec2f(extent.x*2, extent.y*2);
		spr.attachToParent(this, "sprite");
	}
	
	public float getExtendX() {
		return extend.x;
	}
	
	public float getExtendY() {
		return extend.y;
	}
	
	@Override
	public void step(double d) {
		((Spatial)children.get("sprite")).position = position;
		((Spatial)children.get("collider")).position = position;
		super.step(d);
	}
}
