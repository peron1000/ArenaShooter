package arenashooter.engine.physic.bodies;

import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.PhysicBody;
import arenashooter.engine.physic.PhysicShape;
import arenashooter.engine.physic.PhysicWorld;

/**
 * Simple physics object with custom movement
 */
public class KinematicBody extends PhysicBody {
	float density;
	
	private Vec2f linearVelocity = new Vec2f();

	public KinematicBody(PhysicShape shape, Vec2f position, double rotation, float density) {
		super(shape, position, rotation);

		this.density = density;

		bodyDef = new BodyDef();
		bodyDef.position.set(position.x, position.y);
		bodyDef.setAngle((float)rotation);
		bodyDef.setType(BodyType.KINEMATIC);
	}
	
	public void applyImpulse(Vec2f impulse) {
		body.applyLinearImpulse(impulse.toB2Vec(), body.getPosition(), true);
	}
	
	/**
	 * @return linear velocity at center of mass
	 */
	public Vec2f getLinearVelocity() { return linearVelocity.set(body.getLinearVelocity()); }
	
	/**
	 * Set linear velocity at center of mass
	 * @param newVelocity
	 */
	public void setLinearVelocity(Vec2f newVelocity) {
		body.setLinearVelocity(newVelocity.toB2Vec());
	}

	@Override
	public void addToWorld(PhysicWorld world) {
		this.world = world;
		body = world.getB2World().createBody(bodyDef);
		body.createFixture(shape.getB2Shape(), density);
	}

}
