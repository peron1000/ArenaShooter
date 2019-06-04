package arenashooter.engine.ui;

import java.util.LinkedList;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.ui.simpleElement.UiImage;

public class MenuSelectionH<Element extends UiElement> extends Menu {
	private UiImage selec;
	public float ecartement = 5;
	private Vec2f positionRef = getPosition();
	private int index = 0;
	private LinkedList<Element> elements = new LinkedList<>();
	public boolean selectorVisible = true;

	public MenuSelectionH(int maxLayout) {
		super(maxLayout);
	}

	public void addElementInListOfChoices(Element element, int layout) {
		if (elements.contains(element))
			return;
		Vec2f newPosition = new Vec2f(positionRef.x + ecartement * elements.size(), positionRef.y);
		if (elements.isEmpty()) {
			selec.setPosition(newPosition);
		}
		if (!elements.contains(element)) {
			elements.add(element);
		}
		addUiElement(element, layout);
		element.setPosition(newPosition);
		element.setVisible(true);
	}

	public void removeElementInListOfChoices(Element element) {
		if (elements.contains(element)) {
			elements.remove(element);
			if (element.getPosition().x == selec.getPosition().x && element.getPosition().y == selec.getPosition().y) {
				selec.setPosition(elements.getLast().getPosition());
			}
			element.setVisible(false);
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
			uiElement.setPosition(new Vec2f(positionRef.x, positionRef.y + ecartement * i));
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
		selec.setPosition(positionRef.clone());
	}

	public Element getTarget() {
		return elements.get(index);
	}

	@Override
	public void update(double delta) {
		if (elements.isEmpty() || !selectorVisible) {
			selec.setVisible(false);
		} else if (selectorVisible) {
			selec.setVisible(true);
		}
		for (Element uiElement : elements) {
			uiElement.setVisible(true);
			uiElement.update(delta);
		}

		selec.setPosition(Vec2f.lerp(selec.getPosition(), elements.get(index).getPosition(), Utils.clampD(delta * 20, 0, 1)));
		super.update(delta);
	}
}
