package arenashooter.engine.physic.bodies;

import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.FixtureDef;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.PhysicBody;
import arenashooter.engine.physic.PhysicShape;
import arenashooter.engine.physic.PhysicWorld;

/**
 * Immobile object
 */
public class StaticBody extends PhysicBody {
	float friction = 0.3f, restitution = 0.25f;
	
	public StaticBody(PhysicShape shape, Vec2f position, double rotation, CollisionFlags collFlags) {
		super(shape, position, rotation, collFlags);
		
		bodyDef = new BodyDef();
		bodyDef.setPosition(position.toB2Vec());
		bodyDef.setAngle((float)rotation);
	}

	@Override
	public void addToWorld(PhysicWorld world) {
		this.world = world;
		body = world.getB2World().createBody(bodyDef);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.setShape(shape.getB2Shape());
		fixtureDef.setDensity(0);
		fixtureDef.setRestitution(restitution);
		fixtureDef.setFriction(friction);
		
		//Collision filter
		Filter filter = new Filter();
		filter.categoryBits = collFlags.category.bits;
		filter.maskBits = collFlags.maskBits;
		fixtureDef.setFilter(filter);
		
		body.createFixture(fixtureDef);
	}

}
