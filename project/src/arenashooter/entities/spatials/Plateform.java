package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.bodies.StaticBody;
import arenashooter.engine.physic.shapes.ShapeBox;

public class Plateform extends Spatial {
	
	public Plateform(Vec2f position, Vec2f extent) {
		super(position);
		Collider coll = new Collider(this.parentPosition, extent);
		coll.extent = extent;
		coll.attachToParent(this, "collider");
		
		//TODO: passer sprite & rgb
		Sprite spr = new Sprite(position);
		spr.size = new Vec2f(extent.x*2, extent.y*2);
		spr.attachToParent(this, "sprite");
		
		StaticBody staticBody = new StaticBody(new ShapeBox(extent), position, rotation);
		StaticBodyContainer staticBodyC = new StaticBodyContainer(position, staticBody);
		staticBodyC.attachToParent(this, "static body");
	}
}
