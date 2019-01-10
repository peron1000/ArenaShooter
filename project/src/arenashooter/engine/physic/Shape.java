package arenashooter.engine.physic;

import arenashooter.engine.math.Vec2f;

public abstract class Shape {
	public Body body;
	
	public abstract double getMomentOfInertia(double mass);
	
	public abstract Vec2f getAABBextent();
	
	public abstract Vec2f project(Vec2f axis);
}
