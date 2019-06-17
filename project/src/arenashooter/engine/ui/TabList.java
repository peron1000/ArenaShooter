package arenashooter.engine.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.engine.util.CircleList;

public class TabList<E extends UiElement> extends UiElement implements MultiUi {

	private CircleList<UiListVertical<? extends E>> circleList = new CircleList<>();
	private Label tabTitle = new Label("");
	private Map<UiListVertical<? extends E>, String> binding = new HashMap<>();
	private Map<UiListVertical<? extends E>, UiListVertical<Label>> labelsInfo = new HashMap<>();
	private double titleSpacing = 1.5;
	private int indexTarget = 0;
	private UiImage background = new UiImage(0.5, 0.5, 0.5, 1);
	private boolean backgroundVisible = false, scissor = false;
	private double spacing = 1;

	public TabList() {
		tabTitle.addToPosition(0, -getScale().y * titleSpacing);
	}

	/**
	 * The position of the title is above the position of <i>this</i>
	 * 
	 * @param titleSpacing
	 */
	public void setTitleSpacing(double titleSpacing) {
		this.titleSpacing = titleSpacing;
		tabTitle.setPosition(getPosition().x, getPosition().y - getScale().y * titleSpacing);
	}

	public void setTitleVisible(boolean visible) {
		tabTitle.setVisible(visible);
	}

	/**
	 * Set the position and the spacing of the uiList to match with <i>this</i>
	 * values
	 * 
	 * @param tabTitle
	 * @param uiList
	 */
	public void addBind(String tabTitle, UiListVertical<? extends E> uiList) {
		binding.put(uiList, tabTitle);
		UiListVertical<Label> list = labelsInfo.get(uiList);
		if (list == null) {
			list = new UiListVertical<>();
			labelsInfo.put(uiList, list);
		}
		list.setPosition(getPosition());
		uiList.setPosition(getPosition().x, getPosition().y);
		if (circleList.size() == 0) {
			this.tabTitle.setText(tabTitle);
		}
		circleList.add(uiList);
		uiList.setSpacing(spacing);
	}

	public void addLabelInfo(UiListVertical<? extends E> uiList, Label info) {
		UiListVertical<Label> list = labelsInfo.get(uiList);
		if (list == null) {
			list = new UiListVertical<>();
			labelsInfo.put(uiList, list);
		}
		list.setSpacing(spacing);
		list.addElement(info);
		uiList.setPosition(getPosition().x,
				info.getPosition().y + spacing + info.getScale().y / 2 + uiList.getFisrt().getScale().y / 2);
	}

	/**
	 * Set at each list the spacing
	 * 
	 * @param spacing
	 */
	public void setSpacingForeachList(double spacing) {
		this.spacing = spacing;
		for (UiListVertical<? extends E> uiListVertical : circleList) {
			UiListVertical<Label> listInfos = labelsInfo.get(uiListVertical);

			if (listInfos.size() > 0) {
				Label last = listInfos.getLast();
				listInfos.setSpacing(spacing);

				uiListVertical.setPosition(getPosition().x, last.getPosition().y + last.getScale().y / 2 + spacing
						+ uiListVertical.getFisrt().getScale().y / 2);
				uiListVertical.setSpacing(spacing);
			} else {
				uiListVertical.setSpacing(spacing);
			}
		}
	}

	public UiElement get(int index) {
		return circleList.get().get(index);
	}

	public UiElement getTarget() {
		return circleList.get().get(indexTarget);
	}

	public int getIndexTarget() {
		return indexTarget;
	}

	public void setPositionForeach(double x, double y) {
		actionOnAllSimpleElement(e -> e.setPosition(x, y));
	}

	public void addToPositionForeach(double x, double y) {
		actionOnAllSimpleElement(e -> e.addToPosition(x, y));
	}

	public void setScaleForeach(double x, double y) {
		actionOnAllSimpleElement(e -> e.setScale(x, y));
	}

	public void addToScaleForeach(double x, double y) {
		actionOnAllSimpleElement(e -> e.addToScale(x, y));
		setSpacingForeachList(spacing);
	}

	private void actionOnAllSimpleElement(Consumer<UiElement> c) {
		for (UiListVertical<Label> info : labelsInfo.values()) {
			info.forEach(l -> c.accept(l));
		}
		for (UiListVertical<? extends E> uiList : circleList) {
			uiList.forEach(e -> c.accept(e));
		}
		c.accept(tabTitle);
	}

	private void actionOnAll(Consumer<NoStatic> c) {
		c.accept(background);
		for (UiListVertical<Label> info : labelsInfo.values()) {
			for (Label label : info) {
				c.accept(label);
			}
		}
		for (UiListVertical<? extends E> uiList : circleList) {
			c.accept(uiList);
		}
		c.accept(tabTitle);
	}

	@Override
	public void setPosition(double x, double y) {
		double xDif = x - getPosition().x, yDif = y - getPosition().y;
		super.setPosition(x, y);
		actionOnAll(e -> e.setPosition(e.getPosition().x + xDif, e.getPosition().y + yDif));
	}

	@Override
	public void setPositionLerp(double x, double y, double lerp) {
		double xDif = x - getPosition().x, yDif = y - getPosition().y;
		super.setPositionLerp(x, y, lerp);
		actionOnAll(e -> e.setPositionLerp(e.getPosition().x + xDif, e.getPosition().y + yDif, lerp));
	}

	@Override
	public void setScale(double x, double y) {
		super.setScale(x, y);
		tabTitle.setPosition(getPosition().x, getPosition().y - y * titleSpacing);
	}

	@Override
	public void setScaleLerp(double x, double y, double lerp) {
		super.setScaleLerp(x, y, lerp);
		tabTitle.setPositionLerp(getPosition().x, getPosition().y - y * titleSpacing, lerp);
	}

	@Override
	public boolean rightAction() {
		if (!getTarget().rightAction()) {
			circleList.next();
			indexTarget = 0;
			tabTitle.setText(binding.getOrDefault(circleList.get(), "title error"));
		}
		return true;
	}

	@Override
	public boolean leftAction() {
		if (!getTarget().leftAction()) {
			circleList.previous();
			indexTarget = 0;
			tabTitle.setText(binding.getOrDefault(circleList.get(), "title error"));
		}
		return true;
	}

	@Override
	public boolean upAction() {
		if (!getTarget().upAction()) {
			indexTarget--;
			if (indexTarget < 0) {
				indexTarget = circleList.get().size() - 1;
			}
		}
		return true;
	}

	@Override
	public boolean downAction() {
		if (!getTarget().downAction()) {
			indexTarget++;
			if (indexTarget >= circleList.get().size()) {
				indexTarget = 0;
			}
		}
		return true;
	}

	@Override
	public boolean backAction() {
		return getTarget().backAction();
	}

	@Override
	public boolean continueAction() {
		return getTarget().continueAction();
	}

	@Override
	public boolean changeAction() {
		return getTarget().changeAction();
	}

	@Override
	public boolean cancelAction() {
		return getTarget().cancelAction();
	}

	@Override
	public boolean selectAction() {
		return getTarget().selectAction();
	}

	@Override
	public void update(double delta) {
		super.update(delta);
		actionOnAll(e -> e.update(delta));
	}

	@Override
	public void draw() {
		if (isVisible()) {
			if (backgroundVisible) {
				background.draw();
			}
			tabTitle.draw();
			UiListVertical<? extends E> vlist = circleList.get();
			vlist.draw();
			labelsInfo.get(vlist).forEach(l -> l.draw());
		}
	}

	public void reset() {
		indexTarget = 0;
	}

}
