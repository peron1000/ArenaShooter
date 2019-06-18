package arenashooter.game.gameStates.editor;

import java.util.LinkedList;
import java.util.List;

import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.graphics.Material;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.UiElement;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.entities.Editable;
import arenashooter.entities.Sky;
import arenashooter.entities.spatials.Camera;
import arenashooter.entities.spatials.Sprite;
import arenashooter.game.Main;
import arenashooter.game.gameStates.GameState;

public class Editor extends GameState {

	static final float forVisible = -64, forNotVisible = -110;

	private UiImage background = new UiImage(new Vec4f(.5, .5, .5, .2));

	private boolean menuVisible = true;

	private InputListener inputs = new InputListener();

	List<Editable> allEditable = new LinkedList<>();
	Editable onSetting = null;

	private MainMenu mainMenu = new MainMenu(current, this);

	private UiElement currentMenu = mainMenu;
	
	AnimEditor animEditor = new AnimEditor();

	private Camera cam;
	
	private Sprite grid2d;

	public Editor() {
		super(1);
		
		grid2d = new Sprite(new Vec2f(), Material.loadMaterial("data/materials/editor_grid.xml"));

		background.setPosition(forVisible, 0);
		background.setScale(50, 150);
		
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
						if(!currentMenu.backAction()) {
							currentMenu = mainMenu;
							onSetting = null;
						}
						break;
					case UI_CHANGE:
						currentMenu.changeAction();
						break;
					case UI_CANCEL:
						currentMenu.cancelAction();
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
						case ROTATION_Z:
							onSetting.editorAddRotationZ(-rotationSpeed);
							break;
						case ROTATION_X:
							onSetting.editorAddRotationX(-rotationSpeed);
							break;
						case ROTATION_Y:
							onSetting.editorAddRotationY(-rotationSpeed);
							break;
						case SCALE:
							onSetting.editorAddScale(new Vec2f(0, -scaleSpeed));
							break;
						case DEEP:
							onSetting.editorAddDeep((float) positionSpeed);
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
						case ROTATION_Z:
							onSetting.editorAddRotationZ(rotationSpeed);
							break;
						case ROTATION_X:
							onSetting.editorAddRotationX(rotationSpeed);
							break;
						case ROTATION_Y:
							onSetting.editorAddRotationY(rotationSpeed);
							break;
						case SCALE:
							onSetting.editorAddScale(new Vec2f(0, scaleSpeed));
							break;
						case DEEP:
							onSetting.editorAddDeep(-(float) positionSpeed);
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
						case ROTATION_Z:
							onSetting.editorAddRotationZ(rotationSpeed);
							break;
						case ROTATION_X:
							onSetting.editorAddRotationX(rotationSpeed);
							break;
						case ROTATION_Y:
							onSetting.editorAddRotationY(rotationSpeed);
							break;
						case SCALE:
							onSetting.editorAddScale(new Vec2f(scaleSpeed, 0));
							break;
						case DEEP:
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
						case ROTATION_Z:
							onSetting.editorAddRotationZ(-rotationSpeed);
							break;
						case ROTATION_X:
							onSetting.editorAddRotationX(-rotationSpeed);
							break;
						case ROTATION_Y:
							onSetting.editorAddRotationY(-rotationSpeed);
							break;
						case SCALE:
							onSetting.editorAddScale(new Vec2f(-scaleSpeed, 0));
							break;
						case DEEP:
							break;
						default:
							break;
						}
						break;
					default:
						break;
					}
				} else if(event.getActionState() == ActionState.PRESSED) {
					switch (event.getAction()) {
					case UI_ZOOMR:
						cam.editorAddDeep(-0.1f);
						break;
					case UI_ZOOML:
						cam.editorAddDeep(0.1f);
						break;
					default:
						break;
					}
				}
			}
		});
		
	}

	void setCurrentMenu(UiElement currentMenu) {
		this.currentMenu = currentMenu;
	}

	@Override
	public void init() {
		super.init();
		cam = (Camera) current.getChild("camera");
		cam.interpolate = false;
		mainMenu.constructCamerabutton(cam);
		
		Sky sky = (Sky) current.getChild("sky");
		if(sky == null) {
			sky = new Sky(new Vec3f(), new Vec3f());
			sky.attachToParent(current, "sky");
			Main.log.warn("Sky not correctly named or not existing from the xml");
		}
		allEditable.add(sky);
		sky.setColors(new Vec3f(.016, .145, .565), new Vec3f(.659, .835, .996));
	}

	private void setMenuVisible(boolean visible) {
		if (visible) {
			background.setPositionLerp(forVisible, 0, 30);
			currentMenu.setPositionLerp(forVisible, 0, 40);
		} else {
			background.setPositionLerp(forNotVisible, 0, 30);
			currentMenu.setPositionLerp(forNotVisible, 0, 40);
		}
		menuVisible = visible;
	}

	@Override
	public void update(double delta) {
		inputs.step(delta);
		background.update(delta);
		currentMenu.update(delta);
		cam.step(delta);
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
		
		Window.beginTransparency();
		float gridSize = (float) (getCamera().getWorldPos().z * 1.5);
		grid2d.size.set(gridSize, gridSize);
		grid2d.localPosition.set(getCamera().getWorldPos().x, getCamera().getWorldPos().y);
		grid2d.material.setParamF("lineThickness", gridSize*.002f);
		grid2d.draw();
		Window.endTransparency();
		
		Window.beginUi();
		background.draw();
		currentMenu.draw();
		Window.endUi();
	}

}
