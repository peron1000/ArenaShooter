package arenashooter.engine.physic;

import arenashooter.engine.math.Vec2f;

public abstract class Body {
	public Vec2f position;
	public double rotation;
	
	protected Shape shape;
	
	public Body(Shape shape, Vec2f position, double rotation) {
		this.shape = shape;
		this.position = position.clone();
		this.rotation = rotation;
		
		this.shape.body = this;
	}
}
