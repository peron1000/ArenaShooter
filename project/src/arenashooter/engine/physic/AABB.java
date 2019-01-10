package arenashooter.engine.physic;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.shapes.Disc;
import arenashooter.engine.physic.shapes.Rectangle;

public class AABB {
	Vec2f center;
	Vec2f extent;
	
	public double getTop() { return center.y+extent.y; }
	public double getBottom() { return center.y-extent.y; }
	public double getLeft() { return center.x-extent.x; }
	public double getRight() { return center.x+extent.x; }

	public boolean isColliding(AABB other) {
		return Math.abs(center.x - other.center.x) < extent.x + other.extent.x
			&& Math.abs(center.y - other.center.y) < extent.y + other.extent.y;
	}

	public boolean isColliding(Rectangle other) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isColliding(Disc other) {
		// TODO Auto-generated method stub
		return false;
	}
}
