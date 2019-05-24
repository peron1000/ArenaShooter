package arenashooter.engine.ui;

import java.util.LinkedList;

import arenashooter.engine.events.BooleanProperty;
import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.NewValueEvent;
import arenashooter.engine.events.menus.MenuExitEvent;
import arenashooter.engine.events.menus.MenuExitEvent.Side;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.simpleElement.UiImage;

public class MenuSelectionV<E extends UiElement> extends Menu {
	private UiImage selec;
	private float ecartement = 5;
	private Vec2f positionRef;
	private int index = 0;
	private LinkedList<E> elements = new LinkedList<>();
	public BooleanProperty active = new BooleanProperty(true);

	public MenuSelectionV(int maxLayout, float x, float y, Vec2f scaleSelec , String pathTextureSelec) {
		super(maxLayout);
		Texture t = Texture.loadTexture(pathTextureSelec);
		t.setFilter(false);
		selec = new UiImage(0, scaleSelec,t , new Vec4f(1, 1, 1, 1));
		selec.visible = false;
		Vec2f pos = new Vec2f(x, y);
		positionRef = pos.clone();
		setPosition(pos);
		setImageSelec(selec, maxLayout - 1);
		active.listener.add(new EventListener<NewValueEvent<Boolean>>() {

			@Override
			public void action(NewValueEvent<Boolean> e) {
				if (selec != null) {
					selec.visible = e.getNewValue();
				}
			}
		});
	}
	
	public void setEcartement(float e) {
		ecartement = e;
	}

	public void addElementInListOfChoices(E element, int layout) {
		if (elements.contains(element))
			return;
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

	public void removeElementInListOfChoices(E element) {
		if (elements.contains(element)) {
			elements.remove(element);
			if (element.getPos().x == selec.getPos().x && element.getPos().y == selec.getPos().y) {
				selec.setPos(elements.getLast().getPos());
			}
			element.visible = false;
			removeUiElement(element);
		}
	}

	public void down() {
		index++;
		if (index >= elements.size()) {
			index = 0;
		}
		majSelecPosition();
	}

	public void up() {
		index--;
		if (index < 0) {
			index = elements.size() - 1;
		}
		majSelecPosition();
	}

	public void right() {
		if(getTarget().isSelected()) {
			getTarget().rightAction();
		} else {
			exit.action(new MenuExitEvent(Side.Right));
		}
	}

	public void left() {
		if(getTarget().isSelected()) {
			getTarget().leftAction();
		} else {
			exit.action(new MenuExitEvent(Side.Left));
		}
	}

	public void setPositionRef(Vec2f position) {
		positionRef = position;
		for (int i = 0; i < elements.size(); i++) {
			E element = elements.get(i);
			element.setPos(new Vec2f(positionRef.x, positionRef.y + ecartement * i));
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

	public E getTarget() {
		return elements.get(index);
	}

	@Override
	public void update(double delta) {
		for (E uiElement : elements) {
			uiElement.visible = active.getValue();
		}
		super.update(delta);
	}

	public void restart() {
		index = 0;
		E e = elements.get(index);
		if (e != null) {
			selec.setPos(e.getPos());
		}
	}

	protected void majSelecPosition() {
		selec.setPosLerp(getTarget().getPos(), 40);
	}
}
