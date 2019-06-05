package arenashooter.game.gameStates.editor;

import java.util.LinkedList;

import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.Navigable;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.entities.Editable;
import arenashooter.entities.spatials.Camera;
import arenashooter.game.gameStates.GameState;

public class Editor extends GameState {

	static final float forVisible = -64, forNotVisible = -110;

	private UiImage background = new UiImage(0, new Vec2f(50, 150), new Vec4f(.5, .5, .5, .2));

	private boolean menuVisible = true;

	private InputListener inputs = new InputListener();
	private Camera cam;

	LinkedList<Editable> allEditable = new LinkedList<>();
	Editable onSetting = null;

	private MainMenu mainMenu = new MainMenu(current, this);

	private Navigable currentMenu = mainMenu;

	AnimEditor animEditor = new AnimEditor();

	public Editor() {
		super(1);

		background.setPosition(new Vec2f(forVisible, 0));

		inputs.actions.add(new EventListener<InputActionEvent>() {

			@Override
			public void launch(InputActionEvent event) {
				if (event.getActionState() == ActionState.JUST_PRESSED) {
					switch (event.getAction()) {
					case UI_LEFT:
						currentMenu.leftAction();
						break;
					case UI_RIGHT:
						currentMenu.rightAction();
						break;
					case UI_UP:
						currentMenu.upAction();
						break;
					case UI_DOWN:
						currentMenu.downAction();
						break;
					case UI_OK:
						currentMenu.selectAction();
						break;
					case UI_CONTINUE:
						if (!currentMenu.continueAction()) {
							setMenuVisible(!menuVisible);
						}
						break;
					case UI_BACK:
						currentMenu = mainMenu;
						onSetting = null;
						break;
					case UI_CHANGE:
						if (currentMenu instanceof EntityEditor) {
							((EntityEditor) currentMenu).changeAction();
						}
						break;
					case UI_CANCEL:
						if (currentMenu instanceof EntityEditor) {
							((EntityEditor) currentMenu).cancelAction();
						}
						break;
					default:
						break;
					}
				} else if (event.getActionState() == ActionState.PRESSED && currentMenu instanceof EntityEditor
						&& onSetting != null) {
					EntityEditor edit = (EntityEditor) currentMenu;
					final double scaleSpeed = 0.005, positionSpeed = 0.02, rotationSpeed = 0.005;
					switch (event.getAction()) {
					case UI_DOWN2:
						switch (edit.getModificationType()) {
						case POSITION:
							onSetting.editorAddPosition(new Vec2f(0, positionSpeed));
							break;
						case ROTATION:
							onSetting.editorAddRotation(-rotationSpeed);
							break;
						case SCALE:
							onSetting.editorAddScale(new Vec2f(0, -scaleSpeed));
							break;
						case DEEP:
							// TODO
							break;
						default:
							break;
						}
						break;
					case UI_UP2:
						switch (edit.getModificationType()) {
						case POSITION:
							onSetting.editorAddPosition(new Vec2f(0, -positionSpeed));
							break;
						case ROTATION:
							onSetting.editorAddRotation(rotationSpeed);
							break;
						case SCALE:
							onSetting.editorAddScale(new Vec2f(0, scaleSpeed));
							break;
						case DEEP:
							// TODO
							break;
						default:
							break;
						}
						break;
					case UI_RIGHT2:
						switch (edit.getModificationType()) {
						case POSITION:
							onSetting.editorAddPosition(new Vec2f(positionSpeed, 0));
							break;
						case ROTATION:
							onSetting.editorAddRotation(rotationSpeed);
							break;
						case SCALE:
							onSetting.editorAddScale(new Vec2f(scaleSpeed, 0));
							break;
						case DEEP:
							// TODO
							break;
						default:
							break;
						}
						break;
					case UI_LEFT2:
						switch (edit.getModificationType()) {
						case POSITION:
							onSetting.editorAddPosition(new Vec2f(-positionSpeed, 0));
							break;
						case ROTATION:
							onSetting.editorAddRotation(-rotationSpeed);
							break;
						case SCALE:
							onSetting.editorAddScale(new Vec2f(-scaleSpeed, 0));
							break;
						case DEEP:
							// TODO
							break;
						default:
							break;
						}
						break;
					default:
						break;
					}
				}
			}
		});

	}

	void setCurrentMenu(Navigable currentMenu) {
		this.currentMenu = currentMenu;
	}

	@Override
	public void init() {
		super.init();
		cam = (Camera) current.getChild("camera");
	}

	private void setMenuVisible(boolean visible) {
		if (visible) {
			background.setPositionLerp(new Vec2f(forVisible, 0), 30);
			currentMenu.setPositionLerp(new Vec2f(forVisible, 0), 40);
		} else {
			background.setPositionLerp(new Vec2f(forNotVisible, 0), 30);
			currentMenu.setPositionLerp(new Vec2f(forNotVisible, 0), 40);
		}
		menuVisible = visible;
	}

	@Override
	public void update(double delta) {
		inputs.step(delta);
		background.update(delta);
		currentMenu.update(delta);
		for (Editable editable : allEditable) {
			if (onSetting != null && editable == onSetting) {
				editable.setEditorTarget(true);
			} else {
				editable.setEditorTarget(false);
			}
		}
	}

	@Override
	public void draw() {
		for (Editable editable : allEditable) {
			editable.editorDraw();
		}
		Window.beginUi();
		background.draw();
		currentMenu.draw();
		Window.endUi();
	}

}
