package arenashooter.game.gameStates.editor;

import java.util.ArrayList;
import java.util.List;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.ui.Navigable;
import arenashooter.engine.ui.UiElement;
import arenashooter.engine.ui.simpleElement.Label;

public class AnimEditor implements Navigable {
	List<UiElement> elems = new ArrayList<>();

	public AnimEditor() {
		Label label = new Label(0, new Vec2f(3), "Bonjour");
		label.setPos(new Vec2f(1, 1));
		elems.add(label);
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
	public void update(double delta) {
		for(UiElement elem : elems)
			elem.update(delta);
	}

	@Override
	public void draw() {
		for(UiElement elem : elems)
			elem.draw();
	}

	@Override
	public void setPositionLerp(Vec2f position, double lerp) {
		// TODO Auto-generated method stub

	}

}
