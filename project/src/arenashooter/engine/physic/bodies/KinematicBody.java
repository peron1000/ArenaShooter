package arenashooter.engine.physic.bodies;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.FixtureDef;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.shapes.PhysicShape;

/**
 * Simple physics object with custom movement
 */
public class KinematicBody extends PhysicBody {
	float density;
	
	private Vec2f linearVelocity = new Vec2f();

	public KinematicBody(PhysicShape shape, Vec2f worldPosition, double worldRotation, CollisionFlags collFlags, float density) {
		super(shape, worldPosition, worldRotation, collFlags);

		this.density = density;

		bodyDef.setType(BodyType.KINEMATIC);
		
		debugColor = new Vec4f(.2, 1, .2, 1);
	}

	/**
	 * Apply an impulse at center of mass
	 * @param impulse
	 */
	public void applyImpulse(Vec2f impulse) {
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
	 * @return linear velocity at center of mass
	 */
	public Vec2f getLinearVelocity() {
		if(body == null)
			return linearVelocity.set(bodyDef.getLinearVelocity());
		else
			return linearVelocity.set(body.getLinearVelocity());
	}
	
	/**
	 * Set linear velocity at center of mass
	 * @param newVelocity
	 */
	public void setLinearVelocity(Vec2f newVelocity) {
		newVelocity.toB2Vec(bodyDef.getLinearVelocity());
		if(body != null)
			newVelocity.toB2Vec(body.getLinearVelocity());
	}
	
	@Override
	public Body create() {
		body = world.getB2World().createBody(bodyDef);
		
		if(body == null) return null;
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.setShape(shape.getB2Shape());
		fixtureDef.setDensity(density);
		fixtureDef.setRestitution(.3f); //TODO
		fixtureDef.setFriction(.3f); //TODO
		
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

}
