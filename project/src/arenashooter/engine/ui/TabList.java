package arenashooter.engine.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import arenashooter.engine.audio.Audio;
import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.engine.util.CircleList;

public class TabList<E extends UiElement> extends UiElement implements MultiUi {

	private CircleList<UiListVertical<? extends E>> circleList = new CircleList<>();
	private Label tabTitle = new Label("");
	private Map<UiListVertical<? extends E>, String> binding = new HashMap<>();
	private Map<UiListVertical<? extends E>, UiListVertical<Label>> labelsInfo = new HashMap<>();
	private double titleSpacing = 10, arrowsDistance = 10, arrowLerp = 5, lerp = 50;
	private int indexTarget = 0;
	private UiImage background = new UiImage(0.5, 0.5, 0.5, 1);
	private boolean backgroundVisible = false, scissor = false;
	private double spacing = 1;
	private UiImage arrowRight = new UiImage(
			Texture.loadTexture("data/sprites/interface/WhatsRight.png").setFilter(false)),
			arrowLeft = new UiImage(Texture.loadTexture("data/sprites/interface/WhatsLeft.png").setFilter(false));

	public TabList() {
		setTitleSpacing(titleSpacing);
	}

	/**
	 * The position of the title is above the position of <i>this</i> update arrows
	 * position
	 * 
	 * @param titleSpacing
	 */
	public void setTitleSpacing(double titleSpacing) {
		this.titleSpacing = titleSpacing;
		double y = getPosition().y - titleSpacing;
		tabTitle.setPosition(getPosition().x, y);
		setArrowsDistance(arrowsDistance);
	}

	public boolean isBackgroundVisible() {
		return backgroundVisible;
	}

	public void setBackgroundVisible(boolean backgroundVisible) {
		this.backgroundVisible = backgroundVisible;
	}

	public UiImage getBackground() {
		return background;
	}

	public void setBackground(UiImage background) {
		this.background = background;
	}

	public void setScissor(boolean scissor) {
		this.scissor = scissor;
		for (UiListVertical<? extends E> uiListVertical : circleList) {
			uiListVertical.forEach(e -> e.setScissorOk(!scissor));
		}
	}

	public boolean isScissor() {
		return scissor;
	}

	public void setSpacing(double spacing) {
		this.spacing = spacing;
	}

	public double getSpacing() {
		return spacing;
	}

	public void setTitleVisible(boolean visible) {
		tabTitle.setVisible(visible);
	}

	public void setTitleScale(double x, double y) {
		tabTitle.setScale(x, y);
		setTitleSpacing(titleSpacing);
	}

	public void setScaleArrows(double x, double y) {
		arrowLeft.setScale(x, y);
		arrowRight.setScale(x, y);
		setArrowsDistance(arrowsDistance);
	}

	public double getArrowsDistance() {
		return arrowsDistance;
	}

	public double getArrowLerp() {
		return arrowLerp;
	}

	public double getArowsLerp() {
		return lerp;
	}

	public void setArrowsLerp(double lerp) {
		this.lerp = lerp;
	}

	public void setArrowLerp(double arrowLerp) {
		this.arrowLerp = arrowLerp;
	}

	public void setArrowsDistance(double arrowsDistance) {
		this.arrowsDistance = arrowsDistance;
		double y = getPosition().y - titleSpacing;
		arrowLeft.setPosition(getPosition().x - arrowsDistance, y);
		arrowRight.setPosition(getPosition().x + arrowsDistance, y);
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
		if (circleList.size() == 0) {
			this.tabTitle.setText(tabTitle);
		}
		circleList.add(uiList);
		uiList.setSpacing(spacing);
		resetPositionOfList(uiList);
		setScissor(scissor);
	}

	public void addLabelInfo(UiListVertical<? extends E> uiList, Label info) {
		UiListVertical<Label> list = labelsInfo.get(uiList);
		if (list == null) {
			list = new UiListVertical<>();
			labelsInfo.put(uiList, list);
		}
		list.setSpacing(spacing);
		list.addElement(info);
		resetPositionOfList(uiList);
	}

	private void resetPositionOfList(UiListVertical<? extends E> uiList) {
		UiListVertical<Label> infos = labelsInfo.get(uiList);
		if (infos != null && infos.size() > 0) {
			infos.setPosition(getPosition());
			uiList.setPosition(getPosition().x, infos.getLast().getBottom() + spacing);
		} else {
			uiList.setPosition(getPosition());
		}
		if (uiList.size() > 0) {
			uiList.addToPosition(0, uiList.getFisrt().getScale().y / 2);
		}
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
		c.accept(arrowLeft);
		c.accept(arrowRight);
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
		c.accept(arrowLeft);
		c.accept(arrowRight);
	}

	private void slideUpdate() {
		double top = topMainList(), bottom = top + getScale().y;

		if (getTarget().getTop() < top) {
			double d = top - getTarget().getTop();
			circleList.get().addToPositionLerp(0, d, 20);
		} else if (bottom < getTarget().getBottom()) {
			double d = bottom - getTarget().getBottom();
			circleList.get().addToPositionLerp(0, d, 20);
		}
	}

	private float topMainList() {
		UiListVertical<Label> vList = labelsInfo.get(circleList.get());
		if (vList != null && vList.size() > 0) {
			return vList.getBottom();
		} else {
			return getPosition().y;
		}
	}

	public void resetCurrentList() {
		indexTarget = 0;
		resetPositionOfList(circleList.get());
	}

	@Override
	public void setPosition(double x, double y) {
		double xDif = x - getPosition().x , yDif = y - getPosition().y;
		super.setPosition(x, y);
		actionOnAllSimpleElement(e -> e.addToPosition(xDif, yDif));
	}

	@Override
	public void setPositionLerp(double x, double y, double lerp) {
		double xDif = x - getPosition().x , yDif = y - getPosition().y;
		super.setPositionLerp(x, y, lerp);
		actionOnAllSimpleElement(e -> e.addToPositionLerp(xDif, yDif, lerp));
	}

	@Override
	public void setScale(double x, double y) {
		super.setScale(x, y);
		setTitleSpacing(titleSpacing);
	}

	@Override
	public void setScaleLerp(double x, double y, double lerp) {
		super.setScaleLerp(x, y, lerp);
		setTitleSpacing(titleSpacing);
	}

	@Override
	public boolean rightAction() {
		if (!getTarget().rightAction()) {
			if (circleList.size() <= 1)
				return false;

			circleList.next();
			indexTarget = 0;
			resetPositionOfList(circleList.get());
			tabTitle.setText(binding.getOrDefault(circleList.get(), "title error"));
			arrowRight.addToPositionSuperLerp(arrowLerp, 0, lerp);
			Audio.playSound("data/sound/ui/pop.ogg", AudioChannel.UI, .5f, 1);
		}
		return true;
	}

	@Override
	public boolean leftAction() {
		if (!getTarget().leftAction()) {
			if (circleList.size() <= 1)
				return false;

			circleList.get().setPosition(getPosition());
			circleList.previous();
			indexTarget = 0;
			resetPositionOfList(circleList.get());
			tabTitle.setText(binding.getOrDefault(circleList.get(), "title error"));
			arrowLeft.addToPositionSuperLerp(-arrowLerp, 0, lerp);
			Audio.playSound("data/sound/ui/pop.ogg", AudioChannel.UI, .5f, 0.95f);
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
			if (scissor) {
				slideUpdate();
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
			if (scissor) {
				slideUpdate();
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

			if (!scissor) {
				vlist.draw();
			} else {
				Window.stackScissor(getLeft(), topMainList() + getScale().y, getScale().x, getScale().y);
				vlist.draw();
				Window.popScissor();
			}
			labelsInfo.get(vlist).forEach(l -> l.draw());
			if (circleList.size() > 1) {
				arrowLeft.draw();
				arrowRight.draw();
			}
		}
	}

}
