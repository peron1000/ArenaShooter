package arenashooter.entities.spatials;

import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;
import arenashooter.engine.input.Action;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.entities.Entity;
import arenashooter.game.Main;

public class RigidBodyContainer extends Spatial {

	private RigidBody body;

	public RigidBodyContainer(Vec2f position, RigidBody body) {
		super(position);
		this.body = body;
	}
	
	@Override
	public Entity attachToParent(Entity newParent, String name) {
		Entity prev = super.attachToParent(newParent, name);
		if(getMap() != null)
			getMap().physic.registerRigidBody(body);
		return prev;
	}

	@Override
	public void detach() {
		if(getMap() != null)
			getMap().physic.unregisterRigidBody(body);
		super.detach();
	}
	
	@Override
	public void step(double d) {
		position.set(body.position);
		rotation = body.rotation;
		
		//TODO: Delet this
		if( Input.actionJustPressed(Device.KEYBOARD, Action.ATTACK) )
			body.applyImpulse(new Vec2f(position.x, position.y+10), new Vec2f(50, 0));
		
		super.step(d);
		
		//TODO: Proper attachement system
		for(Entity e : children.values())
			if(e instanceof Spatial) ((Spatial)e).rotation = rotation;
	}
	
	@Override
	public void draw() {
		if(Main.drawCollisions) body.shape.debugDraw();
		
		super.draw();
	}
	
}
