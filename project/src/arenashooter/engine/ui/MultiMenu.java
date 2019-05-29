package arenashooter.engine.ui;

import java.util.TreeMap;

import arenashooter.engine.events.BooleanProperty;
import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.NewValueEvent;
import arenashooter.engine.events.menus.MenuExitEvent;
import arenashooter.engine.events.menus.MenuExitEvent.Side;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.simpleElement.UiImage;

public class MultiMenu<T extends Enum<T>> extends Menu implements Navigable {

	private static final MenuSelectionV<UiActionable> defaultMenu = new MenuSelectionV<>(10, 0, 0, new Vec2f(),
			"data/sprites/interface/Selector.png");

	private UiImage selec;
	private Vec2f positionRef = new Vec2f(0, 10);
	private TreeMap<T, MenuSelectionV<UiActionable>> menus = new TreeMap<>();
	public BooleanProperty selectorVisible = new BooleanProperty(true);
	private ScrollerH<T> title;
	private boolean onTitle = true;
	private MenuSelectionV<UiActionable> currentMem = defaultMenu;

	public MultiMenu(int maxLayout, T[] values, String texturePathSelector, Vec2f scaleSelec) {
		super(maxLayout);
		title = new ScrollerH<>(0, new Vec2f(50), values);
		title.setAlwaysScrollable(true);
		title.setPos(new Vec2f());
		this.addUiElement(title, 1);
		Texture t = Texture.loadTexture(texturePathSelector);
		t.setFilter(false);
		selec = new UiImage(0, scaleSelec, t, new Vec4f(1, 1, 1, 1));
		selec.setPos(new Vec2f());
		setPosition(new Vec2f());
		selectorVisible.listener.add(new EventListener<NewValueEvent<Boolean>>() {

			@Override
			public void launch(NewValueEvent<Boolean> event) {
				selec.setVisible(event.getNewValue());
			}
		});
	}

	@Override
	public void update(double delta) {
		if (selectorVisible.getValue()) {
			title.update(delta);
			if (getMenuCurrent() != currentMem) {
				currentMem.setVisible(false);
				getMenuCurrent().setVisible(true);
				currentMem = getMenuCurrent();
			}
			getMenuCurrent().update(delta);
			if (onTitle) {
				selec.setPositionLerp(title.getPos(), 40);
			} else {
				UiElement e = getMenuCurrent().getTarget();
				if (e != null) {
					selec.setPositionLerp(e.getPos(), 40);
				}
			}
			selec.update(delta);
		}
		super.update(delta);
	}

	@Override
	public void draw() {
		if (isVisible()) {
			super.draw();
			getMenuCurrent().draw();
			title.draw();
			if (selectorVisible.getValue()) {
				selec.draw();
			}
		}
	}

	public void setScaleTitle(Vec2f scale) {
		title.setScale(scale);
	}

	@Override
	public void setPosition(Vec2f newPosition) {
		Vec2f dif = Vec2f.subtract(newPosition, getPosition());
		setPositionRef(Vec2f.add(positionRef, dif));
		title.setPos(Vec2f.add(title.getPos(), dif));
		super.setPosition(newPosition);
	}

	@Override
	public void setPositionLerp(Vec2f position , double lerp) {
		Vec2f dif = Vec2f.subtract(position, getPosition());
		for (MenuSelectionV<UiActionable> menu : menus.values()) {
			menu.setPositionLerp(Vec2f.add(menu.getPosition(), dif) , lerp);
		}
		super.setPositionLerp(position , lerp);
	}

	public void setPositionRef(Vec2f position) {
		positionRef.set(position);
		for (Menu menu : menus.values()) {
			menu.setPosition(positionRef);
		}
		title.setPos(Vec2f.add(position, new Vec2f(0, -10)));
	}

	@Override
	public void upAction() {
		if (!onTitle) {
			getMenuCurrent().upAction();
		}
	}

	@Override
	public void downAction() {
		if (onTitle) {
			onTitle = false;
			UiElement target = getMenuCurrent().getTarget();
			if (target != null) {
				selec.setPositionLerp(target.getPos() , 40);
			}
		} else {
			getMenuCurrent().downAction();
		}
	}

	@Override
	public void rightAction() {
		if (onTitle) {
			title.rightAction();
		} else {
			getMenuCurrent().rightAction();
		}
	}

	@Override
	public void leftAction() {
		if (onTitle) {
			title.leftAction();
		} else {
			getMenuCurrent().leftAction();
		}
	}

	@Override
	public void selectAction() {
		if (!onTitle && getTarget() != null) {
			getTarget().selectAction();
		}
	}

	@Override
	public boolean isSelected() {
		if (onTitle) {
			return false;
		} else {
			return getTarget().isSelected();
		}
	}

	@Override
	public void unSelec() {
		if (!onTitle) {
			getTarget().unSelec();
		}
	}

	public void addMenu(MenuSelectionV<UiActionable> menu, T bind) {
		menus.put(bind, menu);
		if (getMenuCurrent() == defaultMenu) {
			menu.setVisible(true);
		} else {
			menu.setVisible(false);
		}
		menu.selectorVisible = false;
		menu.exit = new EventListener<MenuExitEvent>() {

			@Override
			public void launch(MenuExitEvent event) {
				if (event.getSide() == Side.Up) {
					onTitle = true;
				}
			}
		};
		menu.setLoop(false);
		menu.setPosition(positionRef);
	}

	public T getState() {
		return title.get();
	}

	public MenuSelectionV<UiActionable> getMenuCurrent() {
		return menus.getOrDefault(getState(), defaultMenu);
	}

	public UiElement getTarget() {
		if (onTitle) {
			return title;
		} else {
			return getMenuCurrent().getTarget();
		}
	}

}
