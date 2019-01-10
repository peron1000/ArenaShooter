package arenashooter.engine.physic.bodies;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.Body;
import arenashooter.engine.physic.Shape;

/**
 * Immobile object
 */
public class StaticBody extends Body {
	public StaticBody(Shape shape, Vec2f position, double rotation) {
		super(shape, position, rotation);
	}
}
