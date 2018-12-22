package arenashooter.entities;

import arenashooter.engine.math.Vec2f;

public class Spatial extends Entity {
	public Vec2f position;
	public float rotation = 0;
	
	public Spatial(Vec2f position) {
		this.position = position.clone();
	}
}
