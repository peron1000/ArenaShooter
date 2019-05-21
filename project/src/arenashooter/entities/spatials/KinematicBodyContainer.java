package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.bodies.KinematicBody;
import arenashooter.entities.Entity;
import arenashooter.game.Main;

public class KinematicBodyContainer extends Spatial {

	private KinematicBody body;
	
	private boolean needsPhysWorld = true;
	private boolean init = false;

	public KinematicBodyContainer(Vec2f position, KinematicBody body) {
		super(position);
		this.body = body;
	}
	
	@Override
	public Entity attachToParent(Entity newParent, String name) {
		if(body != null) body.removeFromWorld();
		needsPhysWorld = true;
		
		Entity prev = super.attachToParent(newParent, name);
		
		if(getMap() != null) {
			body.addToWorld(getMap().physic);
			needsPhysWorld = false;
		}
		
		return prev;
	}
	
	@Override
	public void detach() {
		if(body != null) body.removeFromWorld();
		needsPhysWorld = true;
		
		super.detach();
	}
	
	@Override
	public Vec2f getWorldPos() {
		return body.getPosition();
	}
	
	/**
	 * @return linear velocity at center of mass
	 */
	public Vec2f getLinearVelocity() { return body.getLinearVelocity(); }
	
	/**
	 * Set linear velocity at center of mass
	 * @param newVelocity
	 */
	public void setLinearVelocity(Vec2f newVelocity) {
		body.setLinearVelocity(newVelocity);
	}
	
	public KinematicBody getBody() {
		return body;
	}
	
	@Override
	public void step(double d) {
		if(!init) {
			init = true;
			body.setUserData(this);
		}
		
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
