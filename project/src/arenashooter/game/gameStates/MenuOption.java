package arenashooter.game.gameStates;

import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.Menu;
import arenashooter.engine.ui.MenuSelectionV;
import arenashooter.engine.ui.ScrollerH;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiActionable;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.engine.ui.simpleElement.Rectangle;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.entities.spatials.Camera;
import arenashooter.entities.spatials.Mesh;
import arenashooter.game.GameMaster;
import arenashooter.game.gameStates.GameState;
import arenashooter.game.gameStates.MenuStart;

public class MenuOption extends GameState {
	/* MenuStart Menu */
	private Vec2f forVisible = new Vec2f(0, 25);
	private MenuSelectionV<UiActionable> menustart = new MenuSelectionV<>(10, forVisible.x, forVisible.y,
			new Vec2f(20, 6), "data/sprites/interface/Selector_Ajustable_4.png");
	/* Save-quit Menu */
	private MenuSelectionV<UiActionable> Resolution = new MenuSelectionV<>(5, 0, 0, new Vec2f(30, 10),
			"data/sprites/interface/Selector.png");

	private int widthinit = Window.getWidth();
	private int heightint = Window.getHeight();

	private final Vec2f scale = new Vec2f(15);

	private Button button1 = new Button(0, new Vec2f(50, 5.5), "Resolution");
	private Button button2 = new Button(0, new Vec2f(50, 5.5), "Camera");
//	private Button button3 = new Button(0, new Vec2f(50, 5.5), "");
	private Button button4 = new Button(0, new Vec2f(50, 5.5), "Quit");

	private String R1 = widthinit + " x " + heightint;
	private String R2 = "1920 x 1080";
	private String R3 = "1280 x 720";

	private InputListener inputs = new InputListener();
	private Camera cam;

	public MenuOption() {
		super(1);

		Texture texture1 = Texture.loadTexture("data/sprites/interface/Fond Menu_Main.png");
		texture1.setFilter(false);

//		Texture texture2 = Texture.loadTexture("data/sprites/interface/Selector.png");
//		texture2.setFilter(false);

		UiImage bg = new UiImage(0, new Vec2f(177.78, 100), texture1, new Vec4f(0, 0, 1, 1));
		menustart.selectorVisible = true;

		menustart.setBackground(bg);
		bg.setPosition(new Vec2f(0));

		menustart.setPositionRef(new Vec2f(forVisible.x, forVisible.y - 45));
		menustart.setEcartement(7f);
		button1.setScaleText(new Vec2f(27f));
		button2.setScaleText(scale);
		// button3.setScaleText(scale);
		button4.setScaleText(scale);

		button1.setColorFond(new Vec4f(0, 1, 1, 1));
		button2.setColorFond(new Vec4f(1, 0, 1, 1));
		// button3.setColorFond(new Vec4f(1, 1, 0, 1));
		button4.setColorFond(new Vec4f(0.25, 0.75, 0.25, 1));

		menustart.addElementInListOfChoices(button1, 2);
		menustart.addElementInListOfChoices(button2, 5);
		// menustart.addElementInListOfChoices(button3, 7);
		menustart.addElementInListOfChoices(button4, 1);

		button1.setOnArm(new Trigger() {

			@Override
			public void make() {
				Window.resize(1980, 1300);
			}
		});
		button2.setOnArm(new Trigger() {

			@Override
			public void make() {
//				cam.setCameraShake(450f);
//				cam.setFOV(798f);			
			}
		});
		button4.setOnArm(new Trigger() {

			@Override
			public void make() {
				GameMaster.gm.requestNextState(new MenuStart(), "data/mapXML/menu_empty.xml");
			}
		});

		inputs.actions.add(new EventListener<InputActionEvent>() {

			@Override
			public void launch(InputActionEvent event) {
				if (event.getActionState() == ActionState.JUST_PRESSED) {
					switch (event.getAction()) {
					case UI_LEFT:
						menustart.leftAction();
						break;
					case UI_RIGHT:
						menustart.rightAction();
						break;
					case UI_UP:
						menustart.upAction();
						break;
					case UI_DOWN:
						menustart.downAction();
						break;
					case UI_OK:
					case UI_CONTINUE:
						menustart.getTarget().selectAction();
						break;
					case UI_BACK:
						GameMaster.gm.requestNextState(new MenuStart(), "data/mapXML/menu_empty.xml");
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

	@Override
	public void update(double delta) {
		menustart.update(delta);
		inputs.step(delta);
		super.update(delta);
	}

	@Override
	public void draw() {
		super.draw();
		menustart.draw();
	}

}
