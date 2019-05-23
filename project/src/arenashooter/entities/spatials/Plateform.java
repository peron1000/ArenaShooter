package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.StaticBody;
import arenashooter.engine.physic.shapes.ShapeBox;

public class Plateform extends Spatial {
	
	public Plateform(Vec2f position, Vec2f extent) {
		super(position);
		
		//TODO: pass a mesh
		Sprite spr = new Sprite(position);
		spr.size = new Vec2f(extent.x*2, extent.y*2);
		spr.attachToParent(this, "sprite");
		
		StaticBody staticBody = new StaticBody(new ShapeBox(extent), position, rotation, CollisionFlags.LANDSCAPE);
		StaticBodyContainer staticBodyC = new StaticBodyContainer(position, staticBody);
		staticBodyC.attachToParent(this, "static body");
	}
}
