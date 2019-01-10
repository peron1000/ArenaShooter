package arenashooter.engine.physic.bodies;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.Body;
import arenashooter.engine.physic.Shape;

/**
 * Simple physics object with custom movement
 */
public class KinematicBody extends Body {
	public KinematicBody(Shape shape, Vec2f position, double rotation) {
		super(shape, position, rotation);
	}
}
