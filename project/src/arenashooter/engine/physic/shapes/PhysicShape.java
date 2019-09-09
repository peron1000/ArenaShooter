package arenashooter.engine.physic.shapes;

import org.jbox2d.collision.shapes.Shape;

import arenashooter.engine.math.Vec2fi;
import arenashooter.engine.math.Vec4f;

public abstract class PhysicShape {
	protected Shape b2Shape;
	
	public Shape getB2Shape() { return b2Shape; }
	
	public abstract void debugDraw(Vec2fi pos, double rot, Vec4f color);
}
