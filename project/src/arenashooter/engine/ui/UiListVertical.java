package arenashooter.engine.ui;

import java.util.Iterator;
import java.util.LinkedList;

import arenashooter.engine.math.Vec2f;

public class UiListVertical<E extends UiElement> implements NoStatic , Iterable<E> {
	
	private LinkedList<E> list = new LinkedList<>();
	private double spacing = 6;
	private Vec2f positionRef = new Vec2f();
	
	public double getSpacing() {
		return spacing;
	}
	
	public void setSpacing(double spacing) {
		this.spacing = spacing;
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setPosition(positionRef.x, positionRef.y+spacing*i);
		}
	}
	
	public void addElement(E element) {
		element.setPosition(positionRef.x, positionRef.y+spacing*list.size());
		list.add(element);
	}
	
	public void addElements(E... elements) {
		for (E uiElement : elements) {
			addElement(uiElement);
		}
	}
	
	public void removeElement(UiElement element) {
		list.remove(element);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setPosition(positionRef.x, positionRef.y+spacing*i);
		}
	}
	
	public void replaceElement(E newElement , E oldElement) {
		int index = list.indexOf(oldElement);
		if(index != -1) {
			list.set(index, newElement);
			newElement.setPosition(oldElement.getPosition().x, oldElement.getPosition().y);
		}
	}
	
	public int getSize() {
		return list.size();
	}

	public UiElement get(int i) {
		return list.get(i);
	}
	
	public int size() {
		return list.size();
	}
	
	public boolean contain(UiElement element) {
		return list.contains(element);
	}
	
	@Override
	public Vec2f getPosition() {
		return positionRef;
	}

	@Override
	public void setPosition(double x, double y) {
		double xDif = x - positionRef.x , yDif = y - positionRef.y;
		positionRef.set(x, y);
		for (UiElement uiElement : list) {
			uiElement.addToPosition(xDif, yDif);
		}
	}

	@Override
	public void setPositionLerp(double x, double y, double lerp) {
		double xDif = x - positionRef.x , yDif = y - positionRef.y;
		positionRef.set(x, y);
		for (UiElement uiElement : list) {
			uiElement.addToPositionLerp(xDif, yDif, lerp);
		}
	}

	@Override
	public Vec2f getScale() {
		double x = Double.MAX_VALUE , y = 0;
		for (UiElement uiElement : list) {
			x = Math.max(x, uiElement.getScale().x);
			y += uiElement.getScale().y+spacing;
		}
		return new Vec2f(x, y);
	}

	@Override
	public void setScale(double x, double y) {
		for (UiElement uiElement : list) {
			uiElement.setScale(x, y);
		}
	}

	@Override
	public void setScaleLerp(double x, double y, double lerp) {
		for (UiElement uiElement : list) {
			uiElement.setScaleLerp(x, y, lerp);
		}
	}

	@Override
	public void update(double delta) {
		for (UiElement uiElement : list) {
			uiElement.update(delta);
		}
	}

	@Override
	public void draw() {
		for (UiElement uiElement : list) {
			uiElement.draw();
		}
	}

	@Override
	public Iterator<E> iterator() {
		return list.iterator();
	}

}
