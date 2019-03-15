package arenashooter.engine.ui;

import arenashooter.engine.math.Vec2f;

public abstract class UiElement {
	protected Vec2f pos, scale;
	protected double rotation;
	
	public UiElement( Vec2f pos, double rot, Vec2f scale ) {
		this.pos = pos.clone();
		this.rotation = rot;
		this.scale = scale.clone();
	}
	
	protected abstract void update();
	
	protected abstract void draw();
}
