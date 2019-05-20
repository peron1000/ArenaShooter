package arenashooter.engine.ui;

import java.util.LinkedList;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;

public class MenuSelectionH<Element extends UiElement> extends Menu {
	private UiImage selec;
	public float ecartement = 5;
	private Vec2f positionRef = getPosition();
	private int index = 0;
	private LinkedList<Element> elements = new LinkedList<>();
	public boolean active = true;

	public MenuSelectionH(int maxLayout) {
		super(maxLayout);
	}

	public void addElementInListOfChoices(Element element, int layout) {
		if (elements.contains(element))
			return;
		Vec2f newPosition = new Vec2f(positionRef.x + ecartement * elements.size(), positionRef.y);
		if (elements.isEmpty()) {
			selec.setPos(newPosition);
		}
		if (!elements.contains(element)) {
			elements.add(element);
		}
		addUiElement(element, layout);
		element.setPos(newPosition);
		element.visible = true;
	}

	public void removeElementInListOfChoices(Element element) {
		if (elements.contains(element)) {
			elements.remove(element);
			if (element.getPos().x == selec.getPos().x && element.getPos().y == selec.getPos().y) {
				selec.setPos(elements.getLast().getPos());
			}
			element.visible = false;
			removeUiElement(element);
		}
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
			uiElement.setPos(new Vec2f(positionRef.x, positionRef.y + ecartement * i));
		}
	}

	@Override
	public void setPosition(Vec2f newPosition) {
		Vec2f dif = Vec2f.subtract(newPosition, getPosition());
		super.setPosition(newPosition);
		setPositionRef(Vec2f.add(positionRef, dif));
	}

	public void setImageSelec(UiImage image, int layout) {
		addUiElement(image, layout);
		selec = image;
		selec.setPos(positionRef.clone());
	}

	public Element getTarget() {
		return elements.get(index);
	}

	@Override
	public void update(double delta) {
		if (elements.isEmpty() || !active) {
			selec.visible = false;
		} else if (active) {
			selec.visible = true;
		}
		for (Element uiElement : elements) {
			uiElement.visible = true;
			uiElement.update();
		}

		selec.setPos(Vec2f.lerp(selec.getPos(), elements.get(index).getPos(), Utils.clampD(delta * 20, 0, 1)));
		super.update(delta);
	}
}