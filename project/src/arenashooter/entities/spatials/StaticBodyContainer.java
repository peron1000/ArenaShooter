package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.bodies.StaticBody;
import arenashooter.entities.Entity;
import arenashooter.game.Main;

public class StaticBodyContainer extends Spatial {
	
	private StaticBody body;
	
	private boolean physicsDirty = false; //TODO: Remove this temp variable

	public StaticBodyContainer(Vec2f position, StaticBody body) {
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
			getMap().physic.unregisterStaticBody(body);
		super.detach();
	}
	
	@Override
	public void step(double d) {
		if(physicsDirty && getMap() != null) {
			getMap().physic.registerStaticBody(body);
			physicsDirty = false;
		}
		super.step(d);
	}
	
	@Override
	public void draw() {
		if(Main.drawCollisions) body.shape.debugDraw();
		
		super.draw();
	}
}
