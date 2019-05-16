package arenashooter.engine.ui;

import java.util.LinkedList;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;

public class MenuSelectionV extends Menu {
	private UiImage selec;
	public float ecartement = 5;
	private Vec2f positionRef = new Vec2f();
	private int index = 0;
	private LinkedList<UiElement> elements = new LinkedList<>();
	public boolean active = true;

	public void addElement(UiElement element) {
		if (elements.isEmpty()) {
			selec.pos = element.pos;
		}
		if (!elements.contains(element)) {
			elements.add(element);
			element.pos.x = positionRef.x;
			element.pos.y = positionRef.y + ecartement * elements.size();
		}
		element.visible = true;
	}

	public void removeElement(UiElement element) {
		if (elements.contains(element))
			elements.remove(element);
		if (element.pos.x == selec.pos.x && element.pos.y == selec.pos.y) {
			selec.pos = elements.getLast().pos;
		}
		element.visible = false;
	}

	public void next(double delta) {
		index++;
		if (index >= elements.size()) {
			index = 0;
		}
	}

	public void previous(double delta) {
		index--;
		if (index < 0) {
			index = elements.size() - 1;
		}
	}

	public void setPositionRef(Vec2f position) {
		positionRef = position;
		for (int i = 0; i < elements.size(); i++) {
			UiElement uiElement = elements.get(i);
			uiElement.pos.x = positionRef.x;
			uiElement.pos.y = positionRef.y + ecartement * i;
		}
	}

	public void setImageSelec(UiImage image) {
		selec = image;
		selec.pos.x = positionRef.x;
		selec.pos.y = positionRef.y;
	}

	public UiElement getTarget() {
		return elements.get(index);
	}

	@Override
	public void update(double delta) {
		if (elements.isEmpty() || !active) {
			selec.visible = false;
		} else if(active) {
			selec.visible = true;
		}
		for (UiElement uiElement : elements) {
			uiElement.visible = true;
			uiElement.update();
		}

		selec.pos = Vec2f.lerp(selec.pos, elements.get(index).pos, Utils.clampD(delta * 20, 0, 1));
		super.update(delta);
	}
}
