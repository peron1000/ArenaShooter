package arenashooter.engine.ui;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Consumer;

import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.menus.MenuExitEvent;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;

public class Menu {
	protected HashMap<Integer, LinkedList<UiElement>> elems = new HashMap<>();
	protected final int maxLayout;
	protected double lerp = 10;
	private Vec2f position = new Vec2f() , positionLerp = new Vec2f();
	public EventListener<MenuExitEvent> exit = new EventListener<MenuExitEvent>() {

		@Override
		public void launch(MenuExitEvent e) {
			// Nothing
		}
	};

	public Menu(int maxLayout) {
		if (maxLayout < 2) {
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
	
	public void setPosition(Vec2f newPosition) {
		position.set(newPosition);
		positionLerp.set(position);
		foreach(e -> e.setPos(position));
	}
	
	public void setPositionLerp(Vec2f position) {
		positionLerp.set(position);
		Vec2f dif = Vec2f.subtract(position, getPosition());
		foreach( t->t.setPosLerp(Vec2f.add(t.getPos(), dif) , lerp));
	}
	
	public Vec2f getPosition() {
		return position;
	}

	public void update(double delta) {
		foreach(e -> e.update(delta));
		position.set(Vec2f.lerp(position, positionLerp, Utils.clampD(delta * lerp, 0, 1)));
	}

	public void setBackground(UiElement background) {
		LinkedList<UiElement> bg = new LinkedList<>();
		bg.add(background);
		elems.put(Integer.valueOf(0), bg);
		background.setPos(position);
	}

	public void addUiElement(UiElement element, int layout) {
		if (maxLayout < 1) {
			Exception e = new Exception(
					"Layout trop petit [valeur donnee = " + layout + " ; max layout = " + (elems.size() - 1) + "]");
			e.printStackTrace();
			LinkedList<UiElement> list = elems.get(Integer.valueOf(1));
			list.add(element);
		} else {
			LinkedList<UiElement> list = elems.get(Integer.valueOf(layout));
			list.add(element);
		}
		element.owner = this;
		element.layout = layout;
		element.setPos(position.clone());
	}
	
	public void removeUiElement(UiElement element) {
		elems.get(element.layout).remove(element);
		element.layout = -1;
		element.owner = null;
		
	}
	
	protected void foreach(Consumer<UiElement> cons) {
		for (LinkedList<UiElement> elem : elems.values()) {
			for (UiElement uiElement : elem) {
				cons.accept(uiElement);
			}
		}
	}

	public void draw() {
		Window.beginUi();
		for (int i = 0; i < maxLayout; i++) {
			LinkedList<UiElement> list = elems.get(Integer.valueOf(i));
			for (UiElement uiElement : list) {
				if (uiElement.visible) {
					uiElement.draw();
				}
			}
		}
		Window.endUi();
	}
}
