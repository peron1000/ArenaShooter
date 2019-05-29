package arenashooter.entities.spatials;

import arenashooter.engine.DamageInfo;
import arenashooter.engine.DamageType;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.entities.Entity;
import arenashooter.game.Main;

public class RigidBodyContainer extends Spatial {

	private RigidBody body;
	
	private boolean needsPhysWorld = true;
	private boolean init = false;

	public RigidBodyContainer(Vec2f position, RigidBody body) {
		super(position);
		this.body = body;
	}
	
	@Override
	public Entity attachToParent(Entity newParent, String name) {
		if(body != null) body.removeFromWorld();
		needsPhysWorld = true;
		
		Entity prev = super.attachToParent(newParent, name);
		
		if(getArena() != null) {
			body.addToWorld(getArena().physic);
			needsPhysWorld = false;
		}
		
		return prev;
	}
	
	@Override
	/**
	 * Detach from current parent and destroy physic body
	 */
	public void detach() {
		if(body != null) body.removeFromWorld();
		needsPhysWorld = true;
		
		super.detach();
	}
	
	/**
	 * Apply an impulse depending on damage received.
	 * <br/>Detach if out of bounds
	 */
	@Override
	public float takeDamage(DamageInfo info) { //TODO: Get impact location
		applyImpulse(Vec2f.multiply(info.direction, info.damage));
		
		//Destroy when out of bounds
		if(info.dmgType == DamageType.OUT_OF_BOUNDS) detach();

		return 0;
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
	
	/**
	 * Apply an impulse at center of mass
	 * @param impulse
	 */
	public void applyImpulse(Vec2f impulse) {
		body.applyImpulse(impulse);
	}
	/**
	 * Apply an impulse at location
	 * @param impulse
	 * @param location world position
	 */
	public void applyImpulse(Vec2f impulse, Vec2f location) {
		body.applyImpulse(impulse, location);
	}
	
	public RigidBody getBody() { return body; }
	
	@Override
	public void step(double d) {
		if(!init) {
			init = true;
			body.setUserData(this);
		}
		
		if(needsPhysWorld) {
			if(getArena() != null) {
				body.addToWorld(getArena().physic);
				needsPhysWorld = false;
			}
		} else {
			localPosition = Vec2f.subtract(body.getPosition(), parentPosition);
			rotation = body.getRotation();
		}
		
		//Destroy when out of bounds
		if (getArena() != null && (getWorldPos().x < getArena().killBound.x || getWorldPos().x > getArena().killBound.z
				|| getWorldPos().y < getArena().killBound.y || getWorldPos().y > getArena().killBound.w))
			takeDamage(new DamageInfo(0, DamageType.OUT_OF_BOUNDS, new Vec2f(), null));
		
		super.step(d);
	}
	
	@Override
	public void draw() {
		if(Main.drawCollisions) body.debugDraw();
		
		super.draw();
	}
	
}
