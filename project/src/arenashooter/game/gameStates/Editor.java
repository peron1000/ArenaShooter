package arenashooter.game.gameStates;

import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.MenuSelectionV;
import arenashooter.engine.ui.MultiMenu;
import arenashooter.engine.ui.UiActionable;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.engine.ui.simpleElement.Rectangle;
import arenashooter.entities.spatials.Camera;
import arenashooter.game.gameStates.editorEnum.Prime;

public class Editor extends GameState {

	private Vec2f forVisible = new Vec2f(-64, 0), forNotVisible = new Vec2f(-110, 0);
	private boolean primeVisible = true, pause = false;

	/* Save-quit Menu */
	private MenuSelectionV<UiActionable> saveQuitMenu = new MenuSelectionV<>(5, 0, 0, new Vec2f(30, 10),
			"data/sprites/interface/Selector.png");

	/* Set Menu */
	private MenuSelectionV<UiActionable> setMenu = new MenuSelectionV<>(10, forVisible.x, forVisible.y,
			new Vec2f(30, 10), "data/sprites/interface/Selector.png");

	/* Add Menu */
	private MenuSelectionV<UiActionable> addMenu = new MenuSelectionV<>(10, forVisible.x, forVisible.y,
			new Vec2f(30, 10), "data/sprites/interface/Selector.png");

	private InputListener inputs = new InputListener();
	private Camera cam;

	/* Main Menu */
	private MultiMenu<Prime> multiMenu = new MultiMenu<>(10, Prime.values(), "data/sprites/interface/Selector.png",
			new Vec2f(30, 10));

	public Editor() {
		super(1);
		/* Save-Quit Menu */
		saveQuitMenu.setBackground(new Rectangle(0, new Vec2f(30), new Vec4f(0.5, 0.5, 0.8, 0.25)));
		saveQuitMenu.setEcartement(12);
		saveQuitMenu.setPositionRef(new Vec2f(0, -6.5));
		Button save = new Button(0, new Vec2f(25, 10), "Save");
		save.setColorFond(new Vec4f(0.25, 0.25, 1, 1));
		Button quit = new Button(0, new Vec2f(25, 10), "Quit");
		quit.setColorFond(new Vec4f(0.25, 0.25, 1, 1));
		saveQuitMenu.addElementInListOfChoices(save, 1);
		saveQuitMenu.addElementInListOfChoices(quit, 1);
		saveQuitMenu.setVisible(false);

		/* Add Menu */
		addMenu.addElementInListOfChoices(new Button(0, new Vec2f(15, 6), "Add"), 1);

		/* Set Menu */
		setMenu.addElementInListOfChoices(new Button(0, new Vec2f(15, 5), "set entity"), 1);

		/* Main Menu */
		Rectangle bg = new Rectangle(0, new Vec2f(50, 150), new Vec4f(0.5, 0.5, 0.5, 0.2));
		multiMenu.setBackground(bg);
		multiMenu.addMenu(setMenu, Prime.Set);
		multiMenu.addMenu(addMenu, Prime.Add);
		multiMenu.setPosition(forVisible);
		multiMenu.setPositionRef(new Vec2f(forVisible.x, -30));

		inputs.actions.add(new EventListener<InputActionEvent>() {

			@Override
			public void launch(InputActionEvent event) {
				if (event.getActionState() == ActionState.JUST_PRESSED) {
					switch (event.getAction()) {
					case UI_LEFT:
						if(!pause) {
							multiMenu.leftAction();
						}
						break;
					case UI_RIGHT:
						if(!pause) {
							multiMenu.rightAction();
						}
						break;
					case UI_UP:
						if(!pause) {
							multiMenu.upAction();
						} else {
							saveQuitMenu.up();
						}
						break;
					case UI_DOWN:
						if(!pause) {
							multiMenu.downAction();
						} else {
							saveQuitMenu.down();
						}
						break;
					case UI_OK:
						if(!pause) {
							multiMenu.selectAction();
						}
						break;
					case UI_CONTINUE:
						if(!pause) {
							setPrimeVisible(!primeVisible);
						}
						break;
					case UI_PAUSE:
						pause = !pause;
						saveQuitMenu.setVisible(pause);
						saveQuitMenu.restart();
						break;
					default:
						break;
					}
				}
			}
		});

	}

	@Override
	public void init() {
		super.init();
		cam = (Camera) current.getChild("camera");
	}

	private void setPrimeVisible(boolean visible) {
		if (visible) {
			multiMenu.setPositionLerp(forVisible);
		} else {
			multiMenu.setPositionLerp(forNotVisible);
		}
		primeVisible = visible;
	}

	@Override
	public void update(double delta) {
		inputs.step(delta);
		// TODO : Ajouter les entites au setMenu
		multiMenu.update(delta);
		saveQuitMenu.update(delta);
	}

	@Override
	public void draw() {
		super.draw();
		Window.beginUi();
		saveQuitMenu.draw();
		multiMenu.draw();
		Window.endUi();
	}

}
