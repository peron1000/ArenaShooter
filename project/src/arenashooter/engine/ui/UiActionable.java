package arenashooter.engine.ui;

import arenashooter.engine.math.Vec2f;

public abstract class UiActionable extends UiElement implements Actionable {
	private boolean selected = false;
	private Trigger arm = new Trigger() {
		
		@Override
		public void make() {
			// Nothing by default
		}
	};

	public UiActionable(double rot, Vec2f scale) {
		super(rot, scale);
	}
	
	@Override
	public boolean isSelected() {
		return selected;
	}
	
	@Override
	public boolean selectAction() {
		selected = !selected;
		return true;
	}
	
	@Override
	public void unSelec() {
		selected = false;
	}
	
	@Override
	public void setOnArm(Trigger t) {
		arm = t;
	}
	
	@Override
	public void arm() {
		arm.make();
	}
}
