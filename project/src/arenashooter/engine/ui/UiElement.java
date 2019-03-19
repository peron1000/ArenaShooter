package arenashooter.engine.ui;

import arenashooter.engine.math.Vec2f;

public abstract class UiElement {
	Menu owner;
	
	protected Vec2f pos, scale;
	protected double rotation;
	
	public UiElement( Menu owner, Vec2f pos, double rot, Vec2f scale ) {
		this.owner = owner;
		this.pos = pos.clone();
		this.rotation = rot;
		this.scale = scale.clone();
	}
	
	protected abstract void update();
	
	protected abstract void draw();
}
