package arenashooter.engine.ui.simpleElement;

import arenashooter.engine.events.menus.MenuExitEvent;
import arenashooter.engine.events.menus.MenuExitEvent.Side;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.ui.UiElement;

public abstract class UiSimpleElementNavigable extends UiElement {

	public UiSimpleElementNavigable(double rot, Vec2f scale) {
		super(rot, scale);
	}

	@Override
	public boolean isSelected() {
		return false;
	}
	
	@Override
	public void unSelec() {
		// Nothing
	}
	
	@Override
	public boolean downAction() {
		owner.exit.launch(new MenuExitEvent(Side.Down));
		return true;
	}

	@Override
	public boolean leftAction() {
		owner.exit.launch(new MenuExitEvent(Side.Left));
		return true;
	}

	@Override
	public boolean rightAction() {
		owner.exit.launch(new MenuExitEvent(Side.Right));
		return true;
	}

	@Override
	public boolean upAction() {
		owner.exit.launch(new MenuExitEvent(Side.Up));
		return true;
	}

}
