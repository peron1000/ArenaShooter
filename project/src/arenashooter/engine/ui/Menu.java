package arenashooter.engine.ui;

import java.util.ArrayList;

import arenashooter.engine.graphics.Window;

public class Menu {
	protected ArrayList<UiElement> elems = new ArrayList<>();

	public UiElement focus = null;

	public void focusUp() {
		if (focus == null)
			return;
		if (focus.up != null)
			focus = focus.up;
	}

	public void focusDown() {
		if (focus == null)
			return;
		if (focus.down != null)
			focus = focus.down;
	}

	public void focusRight() {
		if (focus == null)
			return;
		if (focus.right != null)
			focus = focus.right;
	}

	public void focusLeft() {
		if (focus == null)
			return;
		if (focus.left != null)
			focus = focus.left;
	}

	public void update() {
		for (UiElement elem : elems)
			elem.update();
	}

	public void draw() {
		Window.beginUi();
		for (UiElement elem : elems) {
			if (elem.visible)
				elem.draw();
		}
		Window.endUi();
	}
}
