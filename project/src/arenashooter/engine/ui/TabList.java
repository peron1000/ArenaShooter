package arenashooter.engine.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.engine.util.CircleList;

public class TabList<E extends UiElement> extends UiElement implements MultiUi {
	
	private CircleList<UiListVertical<? extends E>> circleList = new CircleList<>();
	private Label tabTitle = new Label("");
	private HashMap<UiListVertical<? extends E>, String> binding = new HashMap<>();
	private HashMap<UiListVertical<? extends E>, ArrayList<Label>> labelsInfo = new HashMap<>();
	private double titleSpacing = 1.5;
	private int indexTarget = 0;
	private UiImage background = new UiImage(0.5, 0.5, 0.5, 1);
	private boolean backgroundVisible = false;
	private double spacing = 10;
	
	public TabList() {
		tabTitle.addToPosition(0, -getScale().y*titleSpacing);
	}
	
	public void setTitleSpacing(double titleSpacing) {
		this.titleSpacing = titleSpacing;
		tabTitle.setPosition(getPosition().x, getPosition().y-getScale().y*titleSpacing);
	}
	
	public void addBind(String tabTitle , UiListVertical<? extends E> uiList) {
		binding.put(uiList, tabTitle);
		ArrayList<Label> list = labelsInfo.get(uiList);
		if(list ==null) {
			labelsInfo.put(uiList, new ArrayList<>());
		}
		uiList.setPosition(getPosition().x, getPosition().y);
		if(circleList.size() == 0) {
			this.tabTitle.setText(tabTitle);
		}
		circleList.add(uiList);
		uiList.setSpacing(spacing);
	}
	
	public void addLabelInfo(UiListVertical<? extends E> uiList , Label info) {
		ArrayList<Label> list = labelsInfo.get(uiList);
		if(list == null) {
			list = new ArrayList<>();
			labelsInfo.put(uiList, list);
		}
		info.setPosition(getPosition().x, getPosition().y+spacing*list.size());
		list.add(info);
		uiList.setPosition(getPosition().x, getPosition().y+spacing*list.size());
	}
	
	public void setSpacing(double spacing) {
		this.spacing = spacing;
		for (UiListVertical<? extends E> uiListVertical : circleList) {
			ArrayList<Label> listInfos = labelsInfo.get(uiListVertical);
			int i = 0;
			for (Label label : listInfos) {
				label.setPosition(getPosition().x, getPosition().y+spacing*i);
				i++;
			}
			uiListVertical.setPosition(getPosition().x, getPosition().y+spacing*listInfos.size());
			uiListVertical.setSpacing(spacing);
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
	
	private void actionOnAll(Consumer<NoStatic> c) {
		c.accept(background);
		for (ArrayList<Label> info : labelsInfo.values()) {
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
		double xDif = x-getPosition().x , yDif = y-getPosition().y;
		super.setPosition(x, y);
		actionOnAll(e -> e.setPosition(e.getPosition().x+xDif, e.getPosition().y+yDif));
	}
	
	@Override
	public void setPositionLerp(double x, double y, double lerp) {
		double xDif = x-getPosition().x , yDif = y-getPosition().y;
		super.setPositionLerp(x, y, lerp);
		actionOnAll(e -> e.setPositionLerp(e.getPosition().x+xDif, e.getPosition().y+yDif , lerp));
	}
	
	@Override
	public void setScale(double x, double y) {
		super.setScale(x, y);
		for (UiListVertical<? extends E> uiList : circleList) {
			uiList.setSpacing(y*0.75);
		}
		actionOnAll(e -> e.setScale(x, y));
		tabTitle.setPosition(getPosition().x, getPosition().y-y*titleSpacing);
	}
	
	@Override
	public void setScaleLerp(double x, double y, double lerp) {
		super.setScaleLerp(x, y, lerp);
		for (UiListVertical<? extends E> uiList : circleList) {
			uiList.setSpacing(y*0.75);
		}
		actionOnAll(e -> e.setScaleLerp(x, y , lerp));
		tabTitle.setPositionLerp(getPosition().x, getPosition().y-y*titleSpacing , lerp);
	}
	
	@Override
	public boolean rightAction() {
		if(!getTarget().rightAction()) {
			circleList.next();
			indexTarget = 0;
			tabTitle.setText(binding.getOrDefault(circleList.get(), "title error"));
		}
		return true;
	}
	
	@Override
	public boolean leftAction() {
		if(!getTarget().leftAction()) {
			circleList.previous();
			indexTarget = 0;
			tabTitle.setText(binding.getOrDefault(circleList.get(), "title error"));
		}
		return true;
	}
	
	@Override
	public boolean upAction() {
		if(!getTarget().upAction()) {
			indexTarget--;
			if(indexTarget < 0) {
				indexTarget = circleList.get().size()-1;
			}
		}
		return true;
	}
	
	@Override
	public boolean downAction() {
		if(!getTarget().downAction()) {
			indexTarget++;
			if(indexTarget >= circleList.get().size()) {
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
		if(isVisible()) {
			if(backgroundVisible) {
				background.draw();
			}
			tabTitle.draw();
			UiListVertical<? extends E> vlist = circleList.get();
			vlist.draw();
			labelsInfo.get(vlist).forEach(l -> l.draw());
		}
	}

}
