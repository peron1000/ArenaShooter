package arenashooter.entities.spatials;

import arenashooter.engine.Device;
import arenashooter.engine.Input;
import arenashooter.engine.Input.Action;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.Physic;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.entities.Entity;

public class RigidBodyContainer extends Spatial {

	private RigidBody body;

	public RigidBodyContainer(Vec2f position, RigidBody body) {
		super(position);
		this.body = body;
		Physic.registerBody(body);
	}
	
	@Override
	public void step(double d) {
		position.set(body.position);
		rotation = body.rotation;
		
		//TODO: Delet this
		if( Input.actionJustPressed(Device.KEYBOARD, Action.ATTACK) )
			body.applyImpluse(new Vec2f(position.x, position.y+10), new Vec2f(50, 0));
		
		super.step(d);
		
		//TODO: Proper attachement system
		for(Entity e : children.values())
			if(e instanceof Spatial) ((Spatial)e).rotation = rotation;
	}
	
}
