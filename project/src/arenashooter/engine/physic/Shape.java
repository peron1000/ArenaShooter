package arenashooter.engine.physic;

import arenashooter.engine.physic.shapes.Disc;
import arenashooter.engine.physic.shapes.Rectangle;

public abstract class Shape {
	protected Body body;
	
	public abstract boolean isColliding(Rectangle other);
	public abstract boolean isColliding(Disc other);
	
	public abstract double getMomentOfInertia(double mass);
}
