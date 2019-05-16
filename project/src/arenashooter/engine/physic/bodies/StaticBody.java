package arenashooter.engine.physic.bodies;

import org.jbox2d.dynamics.BodyDef;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.PhysicBody;
import arenashooter.engine.physic.PhysicShape;
import arenashooter.engine.physic.PhysicWorld;

/**
 * Immobile object
 */
public class StaticBody extends PhysicBody {
	public StaticBody(PhysicShape shape, Vec2f position, double rotation) {
		super(shape, position, rotation);
		
		bodyDef = new BodyDef();
		bodyDef.setPosition(position.toB2Vec());
		bodyDef.setAngle((float)rotation);
	}

	@Override
	public void addToWorld(PhysicWorld world) {
		this.world = world;
		body = world.getB2World().createBody(bodyDef);
		body.createFixture(shape.getB2Shape(), 0);
	}

}
