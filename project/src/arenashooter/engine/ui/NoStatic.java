package arenashooter.engine.ui;

import arenashooter.engine.math.Vec2f;

public interface NoStatic {
	
	public Vec2f getPosition();
	public void setPosition(double x, double y);
	public void setPositionLerp(double x, double y, double lerp);

	public Vec2f getScale();
	public void setScale(double x, double y);
	public void setScaleLerp(double x, double y, double lerp);
	
	public void update(double delta);
	public void draw();
}
