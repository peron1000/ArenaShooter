package arenashooter.engine.physic.bodies;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.FixtureDef;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec2fi;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.shapes.PhysicShape;

/**
 * Fully simulated object
 */
public class RigidBody extends PhysicBody {
	float density = 1.0f, friction = 0.5f, restitution = 0.25f;
	
	private Vec2f linearVelocity = new Vec2f();
	
	public RigidBody(PhysicShape shape, Vec2fi worldPosition, double worldRotation, CollisionFlags collFlags, float density, float friction) {
		super(shape, worldPosition, worldRotation, collFlags);
		
		this.density = density;
		this.friction = friction;
		
		bodyDef = new BodyDef();
		bodyDef.getPosition().set(worldPosition.x(), worldPosition.y());
		bodyDef.setAngle((float)worldRotation);
		bodyDef.setType(BodyType.DYNAMIC);
		
		debugColor = new Vec4f(1, 0, 0, 1);
	}
	
	/**
	 * A "bullet" body will have more precise movement calculations to avoid tunneling
	 * @param isBullet
	 */
	public void setBullet(boolean isBullet) {
		bodyDef.setBullet(isBullet);
		if(body != null)
			body.setBullet(isBullet);
	}
	
	/**
	 * Apply an impulse at center of mass
	 * @param impulse
	 */
	public void applyImpulse(Vec2fi impulse) {
		if(body != null)
			body.applyLinearImpulse(impulse.toB2Vec(), body.getPosition(), true);
	}
	
	/**
	 * Apply an impulse at location
	 * @param impulse
	 * @param location world position
	 */
	public void applyImpulse(Vec2fi impulse, Vec2fi location) {
		if(body != null)
			body.applyLinearImpulse(impulse.toB2Vec(), location.toB2Vec(), true);
	}
	
	/**
	 * Apply a force at center of mass
	 * @param force
	 */
	public void applyForce(Vec2fi force) {
		if(body != null)
			body.applyForceToCenter(force.toB2Vec());
	}
	
	/**
	 * @return linear velocity at center of mass
	 */
	public Vec2fi getLinearVelocity() {
		if(body == null) return linearVelocity.set(bodyDef.linearVelocity);
		return linearVelocity.set(body.getLinearVelocity());
	}
	
	/**
	 * Set linear velocity at center of mass
	 * @param newVelocity
	 */
	public void setLinearVelocity(Vec2fi newVelocity) {
		newVelocity.toB2Vec(bodyDef.getLinearVelocity());
		if(body != null)
			newVelocity.toB2Vec(body.getLinearVelocity());
	}
	
	public float getAngularVelocity() {
		if(body == null)
			return bodyDef.angularVelocity;
		else
			return body.getAngularVelocity();
	}
	
	public void setAngularVelocity(float angularVelocity) {
		if(body != null)
			body.setAngularVelocity(angularVelocity);
		bodyDef.angularVelocity = angularVelocity;
	}

	@Override
	public Body create() {
		body = world.getB2World().createBody(bodyDef);
		
		if(body == null) return null;
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.setShape(shape.getB2Shape());
		fixtureDef.setDensity(density);
		fixtureDef.setRestitution(restitution);
		fixtureDef.setFriction(friction);
		
		//Collision filter
		Filter filter = new Filter();
		filter.categoryBits = collFlags.category.bits;
		filter.maskBits = collFlags.maskBits;
		fixtureDef.setFilter(filter);
		fixtureDef.setSensor(isSensor());
		fixtureDef.setUserData(userData);
		
		fixture = body.createFixture(fixtureDef);
		
		return body;
	}
	
	public float getDensity() {
		return density;
	}
	
	public float getFriction() {
		return friction;
	}

}
