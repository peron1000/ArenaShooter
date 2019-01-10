package arenashooter.engine.physic.shapes;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.Shape;

public class AxisAlignedRect extends Shape {
	Vec2f extent;
	
	public double getTop() { return body.position.y+extent.y; }
	public double getBottom() { return body.position.y-extent.y; }
	public double getLeft() { return body.position.x-extent.x; }
	public double getRight() { return body.position.x+extent.x; }

	@Override
	public boolean isColliding(AxisAlignedRect other) {
		return Math.abs(body.position.x - other.body.position.x) < extent.x + other.extent.x
			&& Math.abs(body.position.y - other.body.position.y) < extent.y + other.extent.y;
	}

	@Override
	public boolean isColliding(OrientedRect other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isColliding(Disc other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getMomentOfInertia(double mass) {
		//From Wikipedia: 1/12 m * (width^2 + height^2)
		return (mass/12)*((extent.x*2)*(extent.x*2) + (extent.y*2)*(extent.y*2));
	}
}
