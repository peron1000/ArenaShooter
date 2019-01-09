package arenashooter.engine.physic;

import arenashooter.engine.physic.shapes.AxisAlignedRect;
import arenashooter.engine.physic.shapes.Disc;
import arenashooter.engine.physic.shapes.OrientedRect;

public abstract class Shape {
	protected Body body;
	
	public abstract boolean isColliding(AxisAlignedRect other);
	public abstract boolean isColliding(OrientedRect other);
	public abstract boolean isColliding(Disc other);
}
