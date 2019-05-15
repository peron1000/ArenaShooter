package arenashooter.engine.ui;

import arenashooter.engine.math.Vec2f;

public abstract class UiElement {
	Menu owner;
	
	public UiElement left = null, right = null, up = null, down = null;
	
	public Vec2f pos , scale;
	public double rotation;
	
	public UiElement( Menu owner, Vec2f pos, double rot, Vec2f scale ) {
		this.owner = owner;
		owner.elems.add(this);
		this.pos = pos.clone();
		this.rotation = rot;
		this.scale = scale.clone();
	}
	
	protected abstract void update();
	
	protected abstract void draw();
}
