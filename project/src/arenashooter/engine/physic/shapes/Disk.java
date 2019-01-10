package arenashooter.engine.physic.shapes;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.Shape;

public class Disk extends Shape {
	public double radius;
	
	public Disk(double radius) {
		this.radius = radius;
	}
	
	@Override
	public double getMomentOfInertia(double mass) {
		//From Wikipedia: 1/2 mr^2
		return (mass*radius*radius)/2;
	}
	
	@Override
	public Vec2f getAABBextent() {
		return new Vec2f(radius);
	}

	@Override
	public Vec2f project(Vec2f axis) {
		double center = Vec2f.dot(axis, body.position);
		return new Vec2f(center-radius, center+radius);
	}
}
