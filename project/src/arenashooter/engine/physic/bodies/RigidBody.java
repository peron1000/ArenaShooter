package arenashooter.engine.physic.bodies;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.FixtureDef;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.shapes.PhysicShape;

/**
 * Fully simulated object
 */
public class RigidBody extends PhysicBody {
	float density = 1.0f, friction = 0.5f, restitution = 0.25f;
	
	private Vec2f linearVelocity = new Vec2f();
	
	public RigidBody(PhysicShape shape, Vec2f worldPosition, double worldRotation, CollisionFlags collFlags, float density, float friction) {
		super(shape, worldPosition, worldRotation, collFlags);
		
		this.density = density;
		this.friction = friction;
		
		bodyDef = new BodyDef();
		bodyDef.setPosition(worldPosition.toB2Vec());
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
	public void applyImpulse(Vec2f impulse) {
		if(body != null)
			body.applyLinearImpulse(impulse.toB2Vec(), body.getPosition(), true);
	}
	/**
	 * Apply an impulse at location
	 * @param impulse
	 * @param location world position
	 */
	public void applyImpulse(Vec2f impulse, Vec2f location) {
		if(body != null)
			body.applyLinearImpulse(impulse.toB2Vec(), location.toB2Vec(), true);
	}
	
	/**
	 * Apply a force at center of mass
	 * @param force
	 */
	public void applyForce(Vec2f force) {
		if(body != null)
			body.applyForceToCenter(force.toB2Vec());
	}
	
	/**
	 * @return linear velocity at center of mass
	 */
	public Vec2f getLinearVelocity() {
		if(body == null) return new Vec2f(bodyDef.linearVelocity);
		return linearVelocity.set(body.getLinearVelocity());
	}
	
	/**
	 * Set linear velocity at center of mass
	 * @param newVelocity
	 */
	public void setLinearVelocity(Vec2f newVelocity) {
		if(body != null)
			body.setLinearVelocity(newVelocity.toB2Vec());
		else
			bodyDef.setLinearVelocity(newVelocity.toB2Vec());
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

}
