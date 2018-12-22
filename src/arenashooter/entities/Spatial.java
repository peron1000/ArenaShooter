package arenashooter.entities;

import arenashooter.engine.math.Vec2f;

public class Spatial extends Entity {
	public Vec2f position = new Vec2f();
	public float rotation = 0;
	
	public Spatial() {
		
	}
	
	public Spatial(Vec2f position) {
		this.position.set(position);
	}
}
