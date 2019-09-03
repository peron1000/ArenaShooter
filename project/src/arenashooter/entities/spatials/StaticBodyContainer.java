package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2fi;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.StaticBody;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.engine.physic.shapes.ShapeDisk;

public class StaticBodyContainer extends PhysicBodyContainer<StaticBody> {

	public StaticBodyContainer(StaticBody body) {
		super(body);
	}

	/**
	 * Create a static body with a box shape
	 * @param worldPosition
	 * @param extent box extent
	 * @param worldRotation
	 */
	public StaticBodyContainer(Vec2fi worldPosition, Vec2fi extent, double worldRotation) {
		this(new StaticBody(new ShapeBox(extent), worldPosition, worldRotation, CollisionFlags.LANDSCAPE));
	}
	
	/**
	 * Create a static body with a disk shape
	 * @param worldPosition
	 * @param radius disk radius
	 * @param worldRotation
	 */
	public StaticBodyContainer(Vec2fi worldPosition, double radius, double worldRotation) {
		this(new StaticBody(new ShapeDisk(radius), worldPosition, worldRotation, CollisionFlags.LANDSCAPE));
	}
}
