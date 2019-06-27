package arenashooter.engine.ui;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import arenashooter.engine.ui.simpleElement.UiImage;

public class UiGroup<E extends UiElement> extends UiElement implements Iterable<E> {
	
	private UiImage background = null;
	
	private List<E> list = new LinkedList<>();
	
	public UiGroup(E... elements) {
		addElements(elements);
	}
	
	public UiGroup() {
	}
	
	public void addElement(E uiElement) {
		list.add(uiElement);
	}
	
	public void addElements(E...elements) {
		for (E uiElement : elements) {
			addElement(uiElement);
		}
	}
	
	public void setBackground(UiImage background) {
		this.background = background;
	}
	
	@Override
	public void setPosition(double x, double y) {
		double xDif = x-getPosition().x, yDif = y-getPosition().y;
		super.setPosition(x, y);
		forEach(e -> e.addToPosition(xDif, yDif));
	}
	
	@Override
	public void setPositionLerp(double x, double y, double lerp) {
		double xDif = x-getPosition().x, yDif = y-getPosition().y;
		super.setPositionLerp(x, y , lerp);
		forEach(e -> e.addToPositionLerp(xDif, yDif , lerp));
	}
	
	@Override
	public void setScale(double x, double y) {
		double xDif = x-getScale().x, yDif = y-getScale().y;
		super.setScale(x, y);
		forEach(e -> e.addToScale(xDif, yDif));
	}
	
	@Override
	public void setScaleLerp(double x, double y, double lerp) {
		double xDif = x-getScale().x, yDif = y-getScale().y;
		super.setScaleLerp(x, y , lerp);
		forEach(e -> e.addToScaleLerp(xDif, yDif , lerp));
	}
	
	@Override
	public void update(double delta) {
		forEach(e -> e.update(delta));
	}

	@Override
	public void draw() {
		if(background != null) {
			background.draw();
		}
		forEach(e -> e.draw());
	}

	@Override
	public Iterator<E> iterator() {
		return list.iterator();
	}

}
