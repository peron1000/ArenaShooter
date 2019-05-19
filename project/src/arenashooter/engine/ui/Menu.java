package arenashooter.engine.ui;

import java.util.HashMap;
import java.util.LinkedList;

import arenashooter.engine.graphics.Window;

public class Menu {
	protected HashMap<Integer, LinkedList<UiElement>> elems = new HashMap<>();
	protected final int maxLayout;

	public Menu(int maxLayout) {
		if(maxLayout < 1) {
			Exception e = new Exception("Max layout trop petit");
			e.printStackTrace();
			this.maxLayout = maxLayout = 1;
		} else {
			this.maxLayout = maxLayout;
		}
		for (int i = 0; i < maxLayout; i++) {
			elems.put(Integer.valueOf(i), new LinkedList<>());
		}
	}

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

	public void update(double delta) {
		for (LinkedList<UiElement> elem : elems.values()) {
			for (UiElement uiElement : elem) {
				uiElement.update();
			}
		}
	}

	public void draw() {
		Window.beginUi();
		for (LinkedList<UiElement> elem : elems.values()) {
			for (UiElement uiElement : elem) {
				if(uiElement.visible) {
					uiElement.draw();
				}
			}
		}
		Window.endUi();
	}
}
