package arenashooter.engine.physic.bodies;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.FixtureDef;

import arenashooter.engine.math.Vec2f;
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

		bodyDef.position.set(worldPosition.x, worldPosition.y);
		bodyDef.setAngle((float)worldRotation);
		bodyDef.setType(BodyType.KINEMATIC);
	}
	
	public void applyImpulse(Vec2f impulse) {
		body.applyLinearImpulse(impulse.toB2Vec(), body.getPosition(), true);
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
		bodyDef.setLinearVelocity(newVelocity.toB2Vec());
		if(body != null)
			body.setLinearVelocity(newVelocity.toB2Vec());
	}

	/**
	 * Teleport this body
	 * @param position
	 */
	public void setPosition(Vec2f position) {
		if(body != null) {
			body.setTransform(position.toB2Vec(), body.getAngle());
			//Collision filter
			Filter filter = new Filter();
			filter.categoryBits = collFlags.category.bits;
			filter.maskBits = collFlags.maskBits;
		}
	}
	
	/**
	 * Teleport this body
	 * @param position
	 * @param angle
	 */
	public void setTransform(Vec2f position, double angle) {
		if(body != null) {
			body.setTransform(position.toB2Vec(), (float) angle);
			//Collision filter
			Filter filter = new Filter();
			filter.categoryBits = collFlags.category.bits;
			filter.maskBits = collFlags.maskBits;
		}
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

}
