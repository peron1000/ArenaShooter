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

	private boolean physicsDirty = false; //TODO: Remove this temp variable

	public RigidBodyContainer(Vec2f position, RigidBody body) {
		super(position);
		this.body = body;
	}
	
	@Override
	public Entity attachToParent(Entity newParent, String name) {
		Entity prev = super.attachToParent(newParent, name);
		physicsDirty = true;
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
		if(physicsDirty && getMap() != null) {
			getMap().physic.registerRigidBody(body);
			physicsDirty = false;
		}
		
		parentPosition.set(body.position); //TODO: Change this to work with attachment
		rotation = body.rotation;
		
		//TODO: Temp impulse added on attack input from keyboard
		if( Input.actionJustPressed(Device.KEYBOARD, Action.ATTACK) )
			body.applyImpulse(new Vec2f(parentPosition.x, parentPosition.y+10), new Vec2f(50, -70));
		
		super.step(d);
		
		//TODO: Proper attachement system
		for(Entity e : getChildren().values())
			if(e instanceof Spatial) ((Spatial)e).rotation = rotation;
	}
	
	@Override
	public void draw() {
		if(Main.drawCollisions) body.shape.debugDraw();
		
		super.draw();
	}
	
}
