package arenashooter.engine.ui;

import java.util.ArrayList;
import java.util.Iterator;

public class UiGrid extends UiElement implements Iterable<UiElement>, MultiUi {

	private int nbOnRow = 6, index = 0;

	private double spacing = 8;

	private ArrayList<UiElement> list = new ArrayList<>();

	private boolean loop = true;

	public double getSpacing() {
		return spacing;
	}

	public void setSpacing(double spacing) {
		this.spacing = spacing;
		refreshPositions();
	}

	public int getNbOnRow() {
		return nbOnRow;
	}

	public void setNbOnRow(int nbOnRow) {
		this.nbOnRow = nbOnRow;
		refreshPositions();
	}
	
	public boolean isLoop() {
		return loop;
	}
	
	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	private void refreshPositions() {
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setPosition(getPosition().x + getX(i) * spacing, getPosition().y + getY(i) * spacing);
		}
	}

	private int getX(int index) {
		return index % nbOnRow;
	}

	private int getY(int index) {
		return index / nbOnRow;
	}

	public void addElement(UiElement element) {
		int x = getX(list.size()), y = getY(list.size());
		list.add(element);
		element.setPosition(getPosition().x + x * spacing, getPosition().y + y * spacing);
	}
	
	public void addElements(UiElement ...elements) {
		for (UiElement uiElement : elements) {
			addElement(uiElement);
		}
	}

	public void removeElement(UiElement element) {
		int index = list.indexOf(element);
		if (list.remove(element)) {
			for (int i = index; i < list.size(); i++) {
				UiElement e = list.get(i);
				e.setPosition(getPosition().x + getX(i) * spacing, getPosition().y + getY(i) * spacing);
			}
		}
	}

	public UiElement get(int x, int y) {
		int index = y * nbOnRow + x;
		return list.get(index);
	}

	public UiElement getTarget() {
		return list.get(index);
	}

	@Override
	public Iterator<UiElement> iterator() {
		return list.iterator();
	}

	@Override
	public void setPosition(double x, double y) {
		double xDif = x - getPosition().x, yDif = y - getPosition().y;
		getPosition().set(x, y);
		for (UiElement uiElement : list) {
			uiElement.addToPosition(xDif, yDif);
		}
	}

	@Override
	public void setPositionLerp(double x, double y, double lerp) {
		double xDif = x - getPosition().x, yDif = y - getPosition().y;
		getPosition().set(x, y);
		for (UiElement uiElement : list) {
			uiElement.addToPositionLerp(xDif, yDif, lerp);
		}
	}

	@Override
	public void setScale(double x, double y) {
		super.setScale(x, y);
		forEach(e -> e.setScale(x, y));
	}

	@Override
	public void setScaleLerp(double x, double y, double lerp) {
		super.setScale(x, y);
		forEach(e -> e.setScaleLerp(x, y, lerp));
	}

	@Override
	public boolean rightAction() {
		if (!getTarget().rightAction()) {
			if (loop) {
				int saveRow = getY(index);
				index++;
				if (saveRow != getY(index)) {
					index -= nbOnRow;
				}
				if (index >= list.size()) {
					index -= getX(index);
				}
			} else {
				if(getY(index) != getY(index+1) || index+1 >= list.size()) {
					return false;
				} else {
					index++;
				}
			}
		}
		return true;
	}

	@Override
	public boolean leftAction() {
		if (!getTarget().leftAction()) {
			if (loop) {
				int saveRow = getY(index);
				index--;
				if (saveRow != getY(index)) {
					index += nbOnRow;
					if(index >= list.size()) {
						index = list.size()-1;
					}
				}
				if (index < 0) {
					index = nbOnRow - 1;
				}
			} else {
				if(getY(index) != getY(index-1) || index == 0) {
					return false;
				} else {
					index--;
				}
			}
		}
		return true;
	}

	@Override
	public boolean downAction() {
		if (!getTarget().downAction()) {
			if (loop) {
				index += nbOnRow;
				if (index >= list.size()) {
					index = getX(index);
				}
			} else {
				if(index+nbOnRow >= list.size()) {
					return false;
				} else {
					index += nbOnRow;
				}
			}
		}
		return true;
	}

	@Override
	public boolean upAction() {
		if (!getTarget().upAction()) {
			if (loop) {
				int saveCol = getX(index);
				index -= nbOnRow;
				if (index < 0) {
					index = list.size()-1;
					while(getX(index) != saveCol) {
						index--;
					}
				}
			} else {
				if(index-nbOnRow < 0) {
					return false;
				} else {
					index -= nbOnRow;
				}
			}
		}
		return true;
	}
	
	@Override
	public boolean selectAction() {
		return getTarget().selectAction();
	}
	
	@Override
	public boolean continueAction() {
		return getTarget().continueAction();
	}
	
	@Override
	public boolean cancelAction() {
		return getTarget().cancelAction();
	}
	
	@Override
	public boolean changeAction() {
		return getTarget().changeAction();
	}
	
	@Override
	public boolean backAction() {
		return getTarget().backAction();
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

}
