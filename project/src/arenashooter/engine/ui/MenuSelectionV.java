package arenashooter.engine.ui;

import java.util.LinkedList;

import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.NewValueEvent;
import arenashooter.engine.events.menus.MenuActiveProperty;
import arenashooter.engine.events.menus.MenuEventExit;
import arenashooter.engine.events.menus.MenuEventExit.Side;
import arenashooter.engine.math.Vec2f;

public class MenuSelectionV<Element extends UiElement> extends Menu {
	private UiImage selec;
	public float ecartement = 5;
	private Vec2f positionRef = getPosition();
	private int index = 0;
	private LinkedList<Element> elements = new LinkedList<>();
	public MenuActiveProperty active = new MenuActiveProperty();
	public EventListener<MenuEventExit> exit = new EventListener<MenuEventExit>() {
		
		@Override
		public void action(MenuEventExit e) {
			// Nothing
		}
	};

	public MenuSelectionV(int maxLayout) {
		super(maxLayout);
		active.listener.add(new EventListener<NewValueEvent<Boolean>>() {
			
			@Override
			public void action(NewValueEvent<Boolean> e) {
				if(selec != null) {
					selec.visible = e.getNewValue();
				}
			}
		});
	}

	public void addElementInListOfChoices(Element element, int layout) {
		if(elements.contains(element))return;
		Vec2f newPosition = new Vec2f(positionRef.x, positionRef.y + ecartement * elements.size());
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

	public void down(double delta) {
		index++;
		if (index >= elements.size()) {
			index = 0;
			exit.action(new MenuEventExit(Side.Down));
		}
		majSelecPosition();
	}

	public void up(double delta) {
		index--;
		if (index < 0) {
			index = elements.size() - 1;
			exit.action(new MenuEventExit(Side.Up));
		}
		majSelecPosition();
	}
	
	public void right() {
		exit.action(new MenuEventExit(Side.Right));
	}
	
	public void left() {
		exit.action(new MenuEventExit(Side.Left));
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
		for (Element uiElement : elements) {
			uiElement.visible = true;
			//uiElement.update(delta);
		}

		//selec.setPosLerp(getTarget().getPos(), 20);
		//selec.setPos(Vec2f.lerp(selec.getPos(), elements.get(index).getPos(), Utils.clampD(delta * 20, 0, 1)));
		super.update(delta);
	}
	
	public void restart() {
		index = 0;
		Element e = elements.get(index);
		if(e != null) {
			selec.setPos(e.getPos());
		}
	}
	
	private void majSelecPosition() {
		selec.setPosLerp(getTarget().getPos(), 40);
	}
}
