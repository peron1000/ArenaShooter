package arenashooter.engine.ui;

import java.util.Iterator;
import java.util.LinkedList;

import arenashooter.engine.math.Vec2f;

public class UiListVertical<E extends UiElement> extends UiElement implements Iterable<E> {

	private LinkedList<E> list = new LinkedList<>();
	private double spacing = 1;

	public double getSpacing() {
		return spacing;
	}

	public void setSpacing(double spacing) {
		this.spacing = spacing;
		updatePositionElements();
	}

	public void addElement(E element) {
		int size = list.size();
		if (size > 0) {
			E e = list.get(size - 1);
			element.setPosition(getPosition().x,
					e.getPosition().y + spacing + e.getScale().y / 2 + element.getScale().y / 2);
		} else {
			element.setPosition(getPosition().x, getPosition().y);
		}
		list.add(element);
		updatePositionElements();
	}

	public void addElements(E... elements) {
		for (E uiElement : elements) {
			addElement(uiElement);
		}
	}

	public void removeElement(UiElement element) {
		list.remove(element);
		updatePositionElements();
	}

	public void replaceElement(E newElement, E oldElement) {
		int index = list.indexOf(oldElement);
		if (index != -1) {
			list.set(index, newElement);
			newElement.setPosition(oldElement.getPosition().x, oldElement.getPosition().y);
		}
	}

	public E getFisrt() {
		return list.getFirst();
	}

	public E getLast() {
		return list.getLast();
	}

	public UiElement get(int i) {
		if(list.isEmpty()) return null;
		return list.get(i);
	}
	
	public int getIndexOf(UiElement element) {
		return list.indexOf(element);
	}

	public int size() {
		return list.size();
	}

	public boolean contain(UiElement element) {
		return list.contains(element);
	}

	private void updatePositionElements() {
		for (E e : list) {
			if (e == list.getFirst()) {
				e.setPosition(getPosition());
			} else {
				E prev = list.get(list.indexOf(e) - 1);
				e.setPosition(getPosition().x,
						prev.getPosition().y + spacing + prev.getScale().y / 2 + e.getScale().y / 2);
			}
		}
	}

	@Override
	public void setPosition(double x, double y) {
		double xDif = x - getPosition().x, yDif = y - getPosition().y;
		super.setPosition(x, y);
		for (UiElement uiElement : list) {
			uiElement.addToPosition(xDif, yDif);
		}
	}

	@Override
	public void setPositionLerp(double x, double y, double lerp) {
		double xDif = x - getPosition().x, yDif = y - getPosition().y;
		super.setPositionLerp(x, y, lerp);
		for (UiElement uiElement : list) {
			uiElement.addToPositionLerp(xDif, yDif, lerp);
		}
	}
	
	@Override
	public float getBottom() {
		if(list.isEmpty()) {
			return (float) (getPosition().y + spacing);
		} else {
			return (float) (list.getLast().getBottom() + spacing);
		}
	}

	@Override
	public void setScale(double x, double y) {
		// Don't do anything
	}
	
	@Override
	public Vec2f getScale() {
		if(list.isEmpty()) {
			return new Vec2f();
		} else {
			double maxX = list.getFirst().getScale().x , y = 0;
			for (E e : list) {
				y += spacing + e.getScale().y;
				maxX = Math.max(maxX, e.getScale().x);
			}
			return new Vec2f(maxX, y);
		}
	}

	@Override
	public void update(double delta) {
		super.update(delta);
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
