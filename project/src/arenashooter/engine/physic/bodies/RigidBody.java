package arenashooter.engine.physic.bodies;

import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.PhysicBody;
import arenashooter.engine.physic.PhysicShape;
import arenashooter.engine.physic.PhysicWorld;

/**
 * Fully simulated object
 */
public class RigidBody extends PhysicBody {
	float density = 1.0f, friction = 0.3f;
	
	public RigidBody(PhysicShape shape, Vec2f position, double rotation, float density, float friction) {
		super(shape, position, rotation);
		
		this.density = density;
		this.friction = friction;
		
		bodyDef = new BodyDef();
		bodyDef.setPosition(position.toB2Vec());
		bodyDef.setAngle((float)rotation);
		bodyDef.setType(BodyType.DYNAMIC);
	}

	@Override
	public void addToWorld(PhysicWorld world) {
		this.world = world;
		body = world.getB2World().createBody(bodyDef);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.setShape(shape.getB2Shape());
		fixtureDef.setDensity(density);
		fixtureDef.friction = friction;
		
		body.createFixture(fixtureDef);
	}

}
