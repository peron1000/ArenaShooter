package arenashooter.entities;

import arenashooter.engine.math.Vec2f;

public class Collider extends Spatial {
	Vec2f extent;

	public Collider(Vec2f position, Vec2f extent) {
		super();
		this.position = position;
		this.extent = extent;
	}
	
	boolean isColliding(Collider other) {
		return  Math.abs(position.x - other.position.x) < extent.x + other.extent.x &&
				Math.abs(position.y - other.position.y) < extent.y + other.extent.y;
	}
	
	Impact colliding(Vec2f vel , Collider other) {
		return new Impact(this, other, vel);
	}
	
	public Vec2f getTopLeftVec() {
		return new Vec2f(position.x - extent.x, position.y - extent.y);
	}
	
	public Vec2f getTopRightVec() {
		return new Vec2f(position.x + extent.x, position.y - extent.y);
	}
	
	public Vec2f getBottomLeftVec() {
		return new Vec2f(position.x - extent.x, position.y + extent.y);
	}
	
	public Vec2f getBottomRightVec() {
		return new Vec2f(position.x + extent.x, position.y + extent.y);
	}
	
	public float getXLeft() {
		return position.x - extent.x;
	}
	
	public float getXRight() {
		return position.x + extent.x;
	}
	
	public float getYTop() {
		return position.y - extent.y;
	}
	
	public float getYBottom() {
		return position.y + extent.y;
	}
}
