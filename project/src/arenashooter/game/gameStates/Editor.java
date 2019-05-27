package arenashooter.game.gameStates;

import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.MenuSelectionV;
import arenashooter.engine.ui.ScrollerH;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiActionable;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.engine.ui.simpleElement.Rectangle;
import arenashooter.entities.spatials.Camera;
import arenashooter.entities.spatials.Mesh;
import arenashooter.game.gameStates.editorEnum.NameFile;
import arenashooter.game.gameStates.editorEnum.Prime;

public class Editor extends GameState {
	/* Prime Menu */
	private Vec2f forVisible = new Vec2f(-64, 0), forNotVisible = new Vec2f(-110, 0);
	private boolean primeVisible = true;
	private MenuSelectionV<UiActionable> menuPrime = new MenuSelectionV<>(10, forVisible.x, forVisible.y,
			new Vec2f(20, 6), "data/sprites/interface/Selector.png");

	private final Vec2f scale = new Vec2f(15);
	private ScrollerH<Prime> prime = new ScrollerH<>(0, scale, Prime.values());
	private ScrollerH<NameFile> name = new ScrollerH<>(0, scale, NameFile.values());
	private Button button = new Button(0, new Vec2f(50, 5.5), "Object");
	private InputListener inputs = new InputListener();
	private Camera cam;

	public Editor() {
		super(1);
		menuPrime.active.setValue(true);
		Rectangle bg = new Rectangle(0, new Vec2f(50, 150), new Vec4f(0.5, 0.5, 0.5, 0.2));
		menuPrime.setBackground(bg);
		menuPrime.setPositionRef(new Vec2f(forVisible.x, forVisible.y-45));
		menuPrime.addElementInListOfChoices(prime, 1);
		menuPrime.addElementInListOfChoices(name, 1);
		menuPrime.addElementInListOfChoices(button, 1);
		name.setTitle("Name File");
		prime.setAlwaysScrollable(true);
		button.setColorFond(new Vec4f(0, 0, 0, 0));
		button.setOnArm(new Trigger() {

			@Override
			public void make() {
				Vec2f position = new Vec2f(70, -1), extent = new Vec2f(20, 0.25);

				Mesh mesh = new Mesh(new Vec3f(-15, -100, -800), new Quat(0, 0, 0, 1), new Vec3f(100),
						"data/meshes/catwalk/catwalk_10.obj");
				mesh.attachToParent(current, "test");

				Mesh test = new Mesh(new Vec3f(0, 535, -220), new Quat(-0.2f, 0, 0, 1), new Vec3f(3),
						"data/meshes/landscape_field/Hill.obj");
				test.attachToParent(current, "test2");
			}
		});

		inputs.actions.add(new EventListener<InputActionEvent>() {

			@Override
			public void launch(InputActionEvent event) {
				if (event.getActionState() == ActionState.JUST_PRESSED) {
					switch (event.getAction()) {
					case UI_LEFT:
						menuPrime.left();
						break;
					case UI_RIGHT:
						menuPrime.right();
						break;
					case UI_UP:
						menuPrime.up();
						break;
					case UI_DOWN:
						menuPrime.down();
						break;
					case UI_OK:
						menuPrime.getTarget().selectAction();
						break;
					case UI_CONTINUE:
						setPrimeVisible(!primeVisible);
						break;
					default:
						break;
					}
					majPrime();
				}
			}
		});
		majPrime();
	}

	@Override
	public void init() {
		super.init();
		cam = (Camera) current.getChild("camera");
	}

	private void majPrime() {
		switch (prime.get()) {
		case Save:
			button.visible = false;
			name.visible = true;
			break;
		case Add:
			button.visible = true;
			name.visible = false;
			break;
		default:
			button.visible = false;
			name.visible = false;
			break;
		}
	}

	private void setPrimeVisible(boolean visible) {
		if (visible) {
			menuPrime.setPositionLerp(forVisible);
		} else {
			menuPrime.setPositionLerp(forNotVisible);
		}
		primeVisible = visible;
	}

	@Override
	public void update(double delta) {
		menuPrime.update(delta);
		inputs.step(delta);
		super.update(delta);
	}

	@Override
	public void draw() {
		super.draw();
		menuPrime.draw();
	}

}
