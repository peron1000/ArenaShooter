package arenashooter.engine.ui;

import arenashooter.engine.math.Vec2f;

public abstract class UiActionable extends UiElement implements Actionable {
	private boolean selected = false;

	public UiActionable(double rot, Vec2f scale) {
		super(rot, scale);
	}
	
	@Override
	public boolean isSelected() {
		return selected;
	}
	
	@Override
	public void selectAction() {
		selected = !selected;
	}
}
