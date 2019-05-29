package arenashooter.engine.ui;

import arenashooter.engine.math.Vec2f;

public interface Navigable {
	public void upAction();

	public void downAction();

	public void rightAction();

	public void leftAction();
	
	public void selectAction();
	
	public boolean isSelected();
	
	public void unSelec();
	
	public void update(double delta);
	
	public void draw();

	public void setPositionLerp(Vec2f position , double lerp);
	
}
