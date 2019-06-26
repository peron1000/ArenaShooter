package arenashooter.game.gameStates.editor.addItem;

import arenashooter.engine.ui.MultiUi;
import arenashooter.engine.ui.TabList;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiActionable;
import arenashooter.engine.ui.UiElement;
import arenashooter.engine.ui.UiListVertical;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.engine.ui.simpleElement.UiImage;

public class AddItemEditor extends UiElement {

	private TabList<UiActionable> typeChoice = new TabList<>();

	private MultiUi current = typeChoice;

	public AddItemEditor() {

		UiListVertical<UiActionable> list = new UiListVertical<UiActionable>();
		for (ItemType type : ItemType.values()) {
			String name = type.name();
			char first = name.charAt(0);
			name = name.toLowerCase();
			name = name.replaceFirst(name.charAt(0) + "", first + "");
			Button button = new Button(name);
			button.setOnArm(new Trigger() {

				@Override
				public void make() {
					if (type.getEditor() != null)
						current = type.getEditor();
				}
			});
			list.addElement(button);
		}

		typeChoice.addBind("Choose the type", list);

		UiImage bg = new UiImage(.7, .75, .7, .9);
		bg.setScale(90, 60);
		typeChoice.setBackground(bg);
		typeChoice.setScaleForeach(30, 5, false);
		typeChoice.setSpacing(2);
		typeChoice.addToPosition(0, -typeChoice.getScaleY(false) / 2 + typeChoice.getSpacing());
		typeChoice.setBackgroundVisible(true);
		bg.addToPosition(0, -5.5);

		UiImage.selector.setPosition(typeChoice.getTarget().getPosition());
	}

	@Override
	public boolean upAction() {
		boolean boo = current.upAction();
		if (boo)
			UiImage.selector.setPositionLerp(current.getTarget().getPosition().x, current.getTarget().getPosition().y,
					30);
		return boo;
	}

	@Override
	public boolean downAction() {
		boolean boo = current.downAction();
		if (boo)
			UiImage.selector.setPositionLerp(current.getTarget().getPosition().x, current.getTarget().getPosition().y,
					30);
		return boo;
	}

	@Override
	public boolean rightAction() {
		boolean boo = current.rightAction();
		if (boo)
			UiImage.selector.setPositionLerp(current.getTarget().getPosition().x, current.getTarget().getPosition().y,
					30);
		return boo;
	}

	@Override
	public boolean leftAction() {
		boolean boo = current.leftAction();
		if (boo)
			UiImage.selector.setPositionLerp(current.getTarget().getPosition().x, current.getTarget().getPosition().y,
					30);
		return boo;
	}

	@Override
	public boolean continueAction() {
		boolean boo = current.continueAction();
		return boo;
	}

	@Override
	public boolean cancelAction() {
		boolean boo = current.cancelAction();
		return boo;
	}

	@Override
	public boolean changeAction() {
		boolean boo = current.changeAction();
		return boo;
	}

	@Override
	public boolean selectAction() {
		boolean boo = current.selectAction();
		return boo;
	}

	@Override
	public boolean backAction() {
		boolean boo = current.backAction();
		return boo;
	}

	@Override
	public void update(double delta) {
		UiImage.selector.update(delta);
		current.update(delta);
		super.update(delta);
	}

	@Override
	public void draw() {
		current.draw();
		if (current.getTarget() != null)
			UiImage.selector.draw();
	}

}
