package arenashooter.engine.ui;

import java.util.Stack;

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
	private Vec2f positionRef = new Vec2f();
	private int index = 0;
	private Stack<E> elements = new Stack<>();
	public BooleanProperty active = new BooleanProperty(true);

	public MenuSelectionV(int maxLayout, float x, float y, Vec2f scaleSelec, String pathTextureSelec) {
		super(maxLayout);
		Texture t = Texture.loadTexture(pathTextureSelec);
		t.setFilter(false);
		selec = new UiImage(0, scaleSelec, t, new Vec4f(1, 1, 1, 1));
		selec.visible = true;
		Vec2f pos = new Vec2f(x, y);
		setPosition(pos);
		setImageSelec(selec, 2);
		active.listener.add(new EventListener<NewValueEvent<Boolean>>() {

			@Override
			public void launch(NewValueEvent<Boolean> e) {
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
		element.setPos(newPosition);
		element.visible = true;
		restart();
	}

	public void removeElementInListOfChoices(E element) {
		if (elements.contains(element)) {
			elements.remove(element);
			if (element.getPos().x == selec.getPos().x && element.getPos().y == selec.getPos().y) {
				selec.setPos(elements.peek().getPos());
			}
			element.visible = false;
		}
	}

	public void down() {
		int save = index;
		index++;
		if (index >= elements.size()) {
			index = 0;
		}
		while(index != save && !getTarget().visible) {
			index++;
			if (index >= elements.size()) {
				index = 0;
			}
		}
		majSelecPosition();
	}

	public void up() {
		int save = index;
		index--;
		if (index < 0) {
			index = elements.size() - 1;
		}
		while(index != save && !getTarget().visible) {
			index--;
			if (index < 0) {
				index = elements.size() - 1;
			}
		}
		majSelecPosition();
	}

	public void right() {
		if (getTarget().isSelected()) {
			getTarget().rightAction();
		} else {
			exit.launch(new MenuExitEvent(Side.Right));
		}
	}

	public void left() {
		if (getTarget().isSelected()) {
			getTarget().leftAction();
		} else {
			exit.launch(new MenuExitEvent(Side.Left));
		}
	}

	public void setPositionRef(Vec2f position) {
		positionRef.set(position);
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
	
	@Override
	public void setPositionLerp(Vec2f position) {
		Vec2f dif = Vec2f.subtract(position, getPosition());
		positionRef.set(Vec2f.add(positionRef, dif));
		super.setPositionLerp(position);
	}
	
	public void setImageSelec(UiImage image, int layout) {
		addUiElement(image, layout);
		selec = image;
		selec.setPos(positionRef.clone());
	}

	public E getTarget() {
		return elements.get(index);
	}

	public void restart() {
		index = 0;
		E e = elements.get(index);
		if (e != null) {
			selec.setPosLerp(e.getPos(), 5);
		}
	}

	protected void majSelecPosition() {
		selec.setPosLerp(getTarget().getPos(), 40);
	}
	
	@Override
	public void draw() {
		for (E e : elements) {
			if(e.visible) {
				e.draw();
			}
		}
		super.draw();
	}
	
	@Override
	public void update(double delta) {
		int i =0;
		for (E e : elements) {
			if(e.visible) {
				e.setPos(new Vec2f(positionRef.x, positionRef.y + ecartement*i));
				i++;
			}
			e.update(delta);
		}
		super.update(delta);
	}
}
