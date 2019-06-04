package arenashooter.engine.ui;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.simpleElement.Rectangle;

public class AnimEditorTimeline extends UiElement {
	Rectangle bg;

	public AnimEditorTimeline(double rot, Vec2f scale) {
		super(rot, scale);
		
		bg = new Rectangle(rot, scale, new Vec4f(0,0,0, .8f));
	}
	
	@Override
	public void setPos(Vec2f pos) {
		bg.setPos(pos);
		super.setPos(pos);
	}

	@Override
	public void upAction() {
		// TODO Auto-generated method stub

	}

	@Override
	public void downAction() {
		// TODO Auto-generated method stub

	}

	@Override
	public void rightAction() {
		// TODO Auto-generated method stub

	}

	@Override
	public void leftAction() {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectAction() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSelected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void unSelec() {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw() {
		bg.draw();
	}

}
