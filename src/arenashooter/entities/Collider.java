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
}
