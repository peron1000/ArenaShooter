package arenashooter.game.gameStates;

import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.MenuSettings;
import arenashooter.engine.ui.MenuSelectionV;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiActionable;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.game.GameMaster;
import arenashooter.game.Main;
import arenashooter.game.gameStates.editor.Editor;

public class MenuStart extends GameState {
	/* MenuStart Menu */
	private Vec2f forVisible = new Vec2f(0, 25);
	private MenuSelectionV<UiActionable> menustart = new MenuSelectionV<>(10, forVisible.x, forVisible.y,
			new Vec2f(100, 12), "data/sprites/interface/Selector_MainMenu_tr.png");
	// Selector_Ajustable_4

	private final Vec2f scale = new Vec2f(37f);
	private Button button1 = new Button(0, new Vec2f(50, 5.5), "Play");
	private Button button2 = new Button(0, new Vec2f(50, 5.5), "Map Editor");
	private Button button3 = new Button(0, new Vec2f(50, 5.5), "Option");
	private Button button4 = new Button(0, new Vec2f(50, 5.5), "Quit");

	private InputListener inputs = new InputListener();
	private MenuSettings menu;
	public MenuStart() {
		super(1);

		menu = new MenuSettings();
		menu.selectorVisible = false;
		
		Texture texture1 = Texture.loadTexture("data/sprites/interface/Fond Menu_Main.png");
		texture1.setFilter(false);

//		Texture texture2 = Texture.loadTexture("data/sprites/interface/Selector.png");
//		texture2.setFilter(false);

		UiImage bg = new UiImage(0, new Vec2f(177.78, 100), texture1, new Vec4f(1, 1, 1, 1));
		
		menustart.setBackground(bg);
		bg.setPosition(new Vec2f(0));
		
		menustart.setPositionRef(new Vec2f(forVisible.x, forVisible.y - 15));
		menustart.setEcartement(10f);
		button1.setScaleText(scale);
		button2.setScaleText(scale);
		button3.setScaleText(scale);
		button4.setScaleText(scale);

		button1.setColorFond(new Vec4f(0, 1, 1, 0));
		button2.setColorFond(new Vec4f(1, 0, 1, 0));
		button3.setColorFond(new Vec4f(1, 1, 0, 0));
		button4.setColorFond(new Vec4f(1, 1, 0, 0));
		// button4.setColorFond(new Vec4f(0.25, 0.75, 0.25, 1));
		// button1.setColorText(new Vec4f(0, 0, 0, 1));

		// Label a = new Label(0, new Vec2f(57f), "ArenaShooter : SuperBlep");
		// a.setScale(new Vec2f(57f));
		// a.setColor(new Vec4f(0.95, 0, 0.15, 1));
		// menustart.addUiElement(a, 2);
		// a.setPos(new Vec2f(0,-40));

		menustart.addElementInListOfChoices(button1, 3);
		menustart.addElementInListOfChoices(button2, 3);
		menustart.addElementInListOfChoices(button3, 3);
		menustart.addElementInListOfChoices(button4, 3);

		/* Logo */
		Texture SuperLogo = Texture.loadTexture("data/logo.png");
		SuperLogo.setFilter(false);
		UiImage Logo = new UiImage(0, new Vec2f(SuperLogo.getWidth() / 6, SuperLogo.getHeight() / 6), SuperLogo,
				new Vec4f(1, 1, 1, 1));
		menustart.addUiElement(Logo, 1);
		Logo.setPosition(new Vec2f(-2.25, -25.5));

		button1.setOnArm(new Trigger() {

			@Override
			public void make() {
				GameMaster.gm.requestNextState(new Config(), "data/mapXML/menu_empty.xml");
			}
		});
		button2.setOnArm(new Trigger() {

			@Override
			public void make() {
				GameMaster.gm.requestNextState(new Editor(), "data/mapXML/menu_empty.xml");
			}
		});
		button3.setOnArm(new Trigger() {

			@Override
			public void make() {
				menu.selectorVisible = !menu.selectorVisible;
				System.out.println("option");
				//GameMaster.gm.requestNextState(new MenuOption(), "data/mapXML/menu_empty.xml");
			}
		});
		button4.setOnArm(new Trigger() {

			@Override
			public void make() {
				// GameMaster.gm.requestNextState(new Intro(), "data/mapXML/menu_empty.xml");
//				Robot robot;
//				try {
				/* Robot qui émule Alt+f4 */
//					robot = new Robot();
//					robot.keyPress(java.awt.event.KeyEvent.VK_ALT);
//					robot.keyPress(java.awt.event.KeyEvent.VK_F4);
//					robot.keyRelease(java.awt.event.KeyEvent.VK_ALT);
//					robot.keyRelease(java.awt.event.KeyEvent.VK_F4);
				// System.out.println("close the windows");
				Main.Reqclose();
				// System.out.println("zae");
//				} catch (AWTException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					System.out.println("no Blep");
//				}

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
						GameMaster.gm.requestNextState(new Intro(), "data/mapXML/menu_empty.xml");
						break;

					default:
						break;
					}
				}
			}
		});
	}

	@Override
	public void update(double delta) {
		menustart.update(delta);
		if(menu.selectorVisible) {
			menu.update(delta);
		}
		else{
			inputs.step(delta);
		}
		super.update(delta);
	}

	@Override
	public void draw() {
		super.draw();
		Window.beginUi();
		menustart.draw();
		if (menu.selectorVisible) {
		menu.draw();
		}
		Window.endUi();
	}

}