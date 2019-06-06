package arenashooter.entities.spatials;

import arenashooter.engine.DamageInfo;
import arenashooter.engine.DamageType;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.bodies.RigidBody;

public class RigidBodyContainer extends PhysicBodyContainer<RigidBody> {

	public RigidBodyContainer(RigidBody body) {
		super(body);
	}

	/**
	 * @return linear velocity at center of mass
	 */
	public Vec2f getLinearVelocity() {
		return body.getLinearVelocity();
	}

	/**
	 * Set linear velocity at center of mass
	 * 
	 * @param newVelocity
	 */
	public void setLinearVelocity(Vec2f newVelocity) {
		body.setLinearVelocity(newVelocity);
	}
	
	public float getAngularVelocity() { return body.getAngularVelocity(); }
	
	public void setAngularVelocity(float angularVelocity) { body.setAngularVelocity(angularVelocity); }

	/**
	 * Apply an impulse at center of mass
	 * 
	 * @param impulse
	 */
	public void applyImpulse(Vec2f impulse) {
		body.applyImpulse(impulse);
	}

	/**
	 * Apply an impulse at location
	 * 
	 * @param impulse
	 * @param location world position
	 */
	public void applyImpulse(Vec2f impulse, Vec2f location) {
		body.applyImpulse(impulse, location);
	}
	
	/**
	 * Apply an impulse depending on damage received. <br/>
	 * Detach if out of bounds
	 */
	@Override
	public float takeDamage(DamageInfo info) { // TODO: Get impact location
		applyImpulse(Vec2f.multiply(info.direction, info.damage));

		// Destroy when out of bounds
		if(info.dmgType == DamageType.OUT_OF_BOUNDS) {
			if(ignoreKillBounds)
				return 0;
			else
				detach();
		}

		return 0;
	}

	@Override
	public void step(double d) {
		super.step(d);

		// Destroy when out of bounds
		if (getArena() != null && (getWorldPos().x < getArena().killBound.x || getWorldPos().x > getArena().killBound.z
				|| getWorldPos().y < getArena().killBound.y || getWorldPos().y > getArena().killBound.w))
			takeDamage(new DamageInfo(0, DamageType.OUT_OF_BOUNDS, new Vec2f(), null));

	}

}
