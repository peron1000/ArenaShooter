package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.bodies.KinematicBody;
import arenashooter.game.Main;

public class KinematicBodyContainer extends Spatial {

	private KinematicBody body;
	
	private boolean needsPhysWorld = true;

	public KinematicBodyContainer(Vec2f position, KinematicBody body) {
		super(position);
		this.body = body;
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
