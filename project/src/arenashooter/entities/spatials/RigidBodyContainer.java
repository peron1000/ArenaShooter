package arenashooter.entities.spatials;

import arenashooter.engine.input.Action;
import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.game.Main;

public class RigidBodyContainer extends Spatial {

	private RigidBody body;
	
	private boolean needsPhysWorld = true;

	public RigidBodyContainer(Vec2f position, RigidBody body) {
		super(position);
		this.body = body;
	}
	
	@Override
	public void step(double d) {
		if(Input.actionJustPressed(Device.KEYBOARD, Action.ATTACK))
			body.applyImpulse(new Vec2f(2, -20));
		
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
