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

	public KinematicBody(PhysicShape shape, Vec2f position, double rotation, float density) {
		super(shape, position, rotation);

		this.density = density;

		bodyDef = new BodyDef();
		bodyDef.position.set(position.x, position.y);
		bodyDef.setAngle((float)rotation);
		bodyDef.setType(BodyType.KINEMATIC);
	}

	@Override
	public void addToWorld(PhysicWorld world) {
		this.world = world;
		body = world.getB2World().createBody(bodyDef);
		body.createFixture(shape.getB2Shape(), density);
	}

}
