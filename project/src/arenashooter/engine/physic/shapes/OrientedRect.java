package arenashooter.engine.physic.shapes;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.Shape;

public class OrientedRect extends Shape {
	Vec2f extent;
	
	public OrientedRect(Vec2f extent) {
		this.extent = extent.clone();
	}
	
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
	
	@Override
	public double getMomentOfInertia(double mass) {
		//From Wikipedia: 1/12 m * (width^2 + height^2)
		return (mass/12)*((extent.x*2)*(extent.x*2) + (extent.y*2)*(extent.y*2));
	}

}
