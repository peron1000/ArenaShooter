package arenashooter.entities.spatials;

import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.DamageInfo;
import arenashooter.engine.DamageType;
import arenashooter.engine.json.EntityTypes;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.PhysicShape;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.engine.physic.shapes.ShapeDisk;

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
		applyImpulse(Vec2f.multiply(info.direction, info.impulse));

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
			takeDamage(new DamageInfo(0, DamageType.OUT_OF_BOUNDS, new Vec2f(), 0, null));

	}
	
	@Override
	protected EntityTypes getType() {
		PhysicShape shape = getBody().getShape();
		if(shape instanceof ShapeBox) {
			return EntityTypes.RIGID_BOX;
		} else if(shape instanceof ShapeDisk){
			return EntityTypes.RIGID_DISK;
		} else {
			return super.getType();
		}
	}
	
	@Override
	protected JsonObject getJsonObject() {
		JsonObject rigid = super.getJsonObject();
		rigid.putChain("density", getBody().getDensity());
		rigid.putChain("friction", getBody().getFriction());
		rigid.putChain("worldPosition", getWorldPos());
		rigid.putChain("worldRotation", getWorldRot());
		return rigid;
	}

}
