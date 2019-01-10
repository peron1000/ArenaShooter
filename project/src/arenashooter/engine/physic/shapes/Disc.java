package arenashooter.engine.physic.shapes;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.Shape;

public class Disc extends Shape {
	double radius;

	@Override
	public boolean isColliding(Rectangle other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isColliding(Disc other) {
		double distSqr = Vec2f.subtract(body.position, other.body.position).lengthSquared();
		double radiiSqr = (radius+other.radius)*(radius+other.radius);
		return distSqr <= radiiSqr;
	}

	@Override
	public double getMomentOfInertia(double mass) {
		//From Wikipedia: 1/2 mr^2
		return (mass*radius*radius)/2;
	}
}
