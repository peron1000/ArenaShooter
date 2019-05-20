package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.StaticBody;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.game.Main;

public class StaticBodyContainer extends Spatial {
	
	private StaticBody body;
	
	private boolean needsPhysWorld = true;

	public StaticBodyContainer(Vec2f position, StaticBody body) {
		super(position);
		this.body = body;
	}
	
	public StaticBodyContainer(Vec2f position, Vec2f extent, double rotation) {
		this(position, new StaticBody(new ShapeBox(extent), position, rotation, CollisionFlags.LANDSCAPE));
	}
	
	@Override
	public Vec2f getWorldPos() {
		return body.getPosition();
	}
	
	@Override
	public void step(double d) {
		if(needsPhysWorld) {
			if(getMap() != null) {
				body.addToWorld(getMap().physic);
				needsPhysWorld = false;
			}
		} else {
			localPosition = Vec2f.subtract(body.getPosition(), parentPosition);
			rotation = body.getRotation();
		}
		
		super.step(d);
	}
	
	@Override
	public void draw() {
		if(Main.drawCollisions) body.debugDraw();
		
		super.draw();
	}
}
