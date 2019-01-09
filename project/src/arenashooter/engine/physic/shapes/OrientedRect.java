package arenashooter.engine.physic.shapes;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.Shape;

public class OrientedRect extends Shape {
	Vec2f extent;
	
	@Override
	public boolean isColliding(AxisAlignedRect other) {
		// TODO Auto-generated method stub
		return false;
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

}
