package arenashooter.engine.physic.bodies;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.FixtureDef;

import arenashooter.engine.math.Vec2fi;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.shapes.PhysicShape;

/**
 * Immobile object
 */
public class StaticBody extends PhysicBody {
	float friction = 0.3f, restitution = 0.25f;
	
	public StaticBody(PhysicShape shape, Vec2fi worldPosition, double worldRotation, CollisionFlags collFlags) {
		super(shape, worldPosition, worldRotation, collFlags);
		
		debugColor = new Vec4f(.2, .2, 1, 1);
	}

	@Override
	public Body create() {
		body = world.getB2World().createBody(bodyDef);
		
		if(body == null) return null;
		
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
		fixtureDef.setSensor(isSensor());
		fixtureDef.setUserData(userData);
		
		fixture = body.createFixture(fixtureDef);
		
		return body;
	}

}
