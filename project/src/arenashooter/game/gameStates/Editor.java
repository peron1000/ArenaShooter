package arenashooter.game.gameStates;

import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.bodies.StaticBody;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.engine.ui.MenuSelectionV;
import arenashooter.engine.ui.ScrollerH;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiActionable;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.engine.ui.simpleElement.Rectangle;
import arenashooter.entities.spatials.Camera;
import arenashooter.entities.spatials.Mesh;
import arenashooter.entities.spatials.RigidBodyContainer;
import arenashooter.entities.spatials.Spatial3;
import arenashooter.entities.spatials.StaticBodyContainer;
import arenashooter.game.gameStates.editorEnum.NameFile;
import arenashooter.game.gameStates.editorEnum.Prime;

public class Editor extends GameState {

	private final Vec2f scale = new Vec2f(15);
	private MenuSelectionV<UiActionable> menu = new MenuSelectionV<>(10, -36.5f, -20f, new Vec2f(20, 6),
			"data/sprites/interface/Selector.png");
	private ScrollerH<Prime> prime = new ScrollerH<>(0, scale, Prime.values());
	private ScrollerH<NameFile> name = new ScrollerH<>(0, scale, NameFile.values());
	private Button button = new Button(0, new Vec2f(50, 5.5), "Object");
	private InputListener inputs = new InputListener();
	private Camera cam;

	public Editor() {
		super(1);
		menu.active.setValue(true);
		menu.addElementInListOfChoices(prime, 1);
		Rectangle bg = new Rectangle(0, new Vec2f(50, 200), new Vec4f(0.5, 0.5, 0.5, 0.2));
		menu.setBackground(bg);
		name.setTitle("Name File");
		prime.setAlwaysScrollable(true);
		button.setColorFond(new Vec4f(0, 0, 0, 0));
		button.setOnArm(new Trigger() {

			@Override
			public void make() {
				Vec2f position = new Vec2f(70, -1), extent = new Vec2f(20, 0.25);

				Mesh mesh = new Mesh(new Vec3f(-15, 0.5, 0), new Quat(0, 0, 0, 1), new Vec3f(100),
						"data/meshes/catwalk/catwalk_10.obj");
				mesh.attachToParent(current, "test");

				Mesh test = new Mesh(new Vec3f(0, 535, -220), new Quat(-0.2f, 0, 0, 1), new Vec3f(3), "data/meshes/landscape_field/Hill.obj");
				test.attachToParent(current, "test2");
			}
		});

		inputs.actions.add(new EventListener<InputActionEvent>() {

			@Override
			public void action(InputActionEvent event) {
				if (event.getActionState() == ActionState.JUST_PRESSED) {
					switch (event.getAction()) {
					case UI_LEFT:
						menu.left();
						break;
					case UI_RIGHT:
						menu.right();
						break;
					case UI_UP:
						menu.up();
						break;
					case UI_DOWN:
						menu.down();
						break;
					case UI_OK:
						menu.getTarget().selectAction();
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
			menu.removeElementInListOfChoices(button);
			menu.addElementInListOfChoices(name, 1);
			break;
		case Add:
			menu.removeElementInListOfChoices(name);
			menu.addElementInListOfChoices(button, 1);
			break;
		default:
			menu.removeElementInListOfChoices(button);
			menu.removeElementInListOfChoices(name);
			break;
		}
	}

	@Override
	public void update(double delta) {
		inputs.step(delta);
		menu.update(delta);
		super.update(delta);
	}

	@Override
	public void draw() {
		super.draw();
		menu.draw();
	}

}
