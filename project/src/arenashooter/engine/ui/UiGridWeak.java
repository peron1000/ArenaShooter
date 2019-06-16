package arenashooter.engine.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class UiGridWeak extends UiElement implements Iterable<UiElement> {

	private List<UiListVertical<UiElement>> vLists = new ArrayList<>();

	private int indexX = 0, indexY = 0;
	
	public void addListVertical(UiListVertical<UiElement> newVList) {
		vLists.add(newVList);
	}

	public void removeLisVertical(UiListVertical<UiElement> vList) {
		vLists.remove(vList);
	}

	public UiElement getTarget() {
		if (!vLists.isEmpty()) {
			return vLists.get(indexX).get(indexY);
		} else {
			Exception e = new Exception("There is no UiListVertical in the UiGridWeak");
			e.printStackTrace();
			return null;
		}
	}

	public UiListVertical<UiElement> curentList() {
		return vLists.get(indexX);
	}

	@Override
	public void setPosition(double x, double y) {
		double xDif = x - getPosition().x, yDif = y - getPosition().y;
		super.setPosition(x, y);
		forEach(e -> e.addToPosition(xDif, yDif));
	}

	@Override
	public void setPositionLerp(double x, double y, double lerp) {
		double xDif = x - getPosition().x, yDif = y - getPosition().y;
		super.setPositionLerp(x, y, lerp);
		forEach(e -> e.addToPositionLerp(xDif, yDif, lerp));
	}

	@Override
	public void setScale(double x, double y) {
		double xDif = x - getScale().x, yDif = y - getScale().y;
		super.setScale(x, y);
		forEach(e -> e.addToScale(xDif, yDif));
	}

	@Override
	public void setScaleLerp(double x, double y, double lerp) {
		double xDif = x - getScale().x, yDif = y - getScale().y;
		super.setScaleLerp(x, y, lerp);
		forEach(e -> e.addToScaleLerp(xDif, yDif, lerp));
	}

	@Override
	public boolean rightAction() {
		if (!getTarget().rightAction()) {
			UiListVertical<UiElement> vList = curentList();
			do {
				indexX++;
				if (indexX >= vLists.size()) {
					indexX = 0;
				}
			} while (curentList().size() == 0 && vList != curentList());
			while (indexY >= curentList().size()) {
				indexY--;
			}
		}
		return true;
	}

	@Override
	public boolean leftAction() {
		if (!getTarget().leftAction()) {
			UiListVertical<UiElement> vList = curentList();
			do {
				indexX--;
				if (indexX < 0) {
					indexX = vLists.size() - 1;
				}
			} while (curentList().size() == 0 && vList != curentList());
			while (indexY >= curentList().size()) {
				indexY--;
			}
		}
		return true;
	}

	@Override
	public boolean upAction() {
		if (!getTarget().upAction()) {
			indexY--;
			if (indexY < 0) {
				indexY = curentList().size() - 1;
			}
			if (curentList().size() == 0) {
				indexY = 0;
			}
		}
		return true;
	}

	@Override
	public boolean downAction() {
		if (!getTarget().downAction()) {
			indexY++;
			if (indexY >= curentList().size()) {
				indexY = 0;
			}
		}
		return true;
	}

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
		forEach(e -> e.update(delta));
		super.update(delta);
	}

	@Override
	public void draw() {
		for (UiListVertical<UiElement> uiListVertical : vLists) {
			uiListVertical.draw();
		}
	}

	@Override
	public Iterator<UiElement> iterator() {
		Set<UiElement> set = new HashSet<>();
		for (UiListVertical<UiElement> vList : vLists) {
			for (UiElement uiElement : vList) {
				set.add(uiElement);
			}
		}
		return set.iterator();
	}

}
