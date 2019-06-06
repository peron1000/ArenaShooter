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
	private Button buttonPlay = new Button(0, new Vec2f(50, 5.5), "Play");
	private Button buttonEditor = new Button(0, new Vec2f(50, 5.5), "Map Editor");
	private Button buttonSettings = new Button(0, new Vec2f(50, 5.5), "Settings");
	private Button buttonQuit = new Button(0, new Vec2f(50, 5.5), "Quit");

	private InputListener inputs = new InputListener();
	private MenuSettings menu;
	public MenuStart() {
		super(1);

		menu = new MenuSettings();
		menu.selectorVisible = false;
		
		Texture texture1 = Texture.loadTexture("data/sprites/interface/Fond Menu_Main.png");
		texture1.setFilter(false);

		UiImage bg = new UiImage(0, new Vec2f(177.78, 100), texture1, new Vec4f(1));
		
		menustart.setBackground(bg);
		bg.setPosition(new Vec2f(0));
		
		menustart.setPositionRef(new Vec2f(forVisible.x, forVisible.y - 15));
		menustart.setEcartement(10f);
		buttonPlay.setScaleText(scale);
		buttonEditor.setScaleText(scale);
		buttonSettings.setScaleText(scale);
		buttonQuit.setScaleText(scale);

		buttonPlay.setColorFond(new Vec4f(0, 1, 1, 0));
		buttonEditor.setColorFond(new Vec4f(1, 0, 1, 0));
		buttonSettings.setColorFond(new Vec4f(1, 1, 0, 0));
		buttonQuit.setColorFond(new Vec4f(1, 1, 0, 0));

		menustart.addElementInListOfChoices(buttonPlay, 1);
		menustart.addElementInListOfChoices(buttonEditor, 1);
		menustart.addElementInListOfChoices(buttonSettings, 1);
		menustart.addElementInListOfChoices(buttonQuit, 1);

		// Logo
		Texture logoTex = Texture.loadTexture("data/logo.png");
		logoTex.setFilter(false);
		UiImage Logo = new UiImage(0, new Vec2f(logoTex.getWidth() / 6, logoTex.getHeight() / 6), logoTex, new Vec4f(1));
		menustart.addUiElement(Logo, 5);
		Logo.setPosition(new Vec2f(-2.25, -24));

		buttonPlay.setOnArm(new Trigger() {
			@Override
			public void make() {
				GameMaster.gm.requestNextState(new Config(), "data/mapXML/menu_empty.xml");
			}
		});
		
		buttonEditor.setOnArm(new Trigger() {
			@Override
			public void make() {
				GameMaster.gm.requestNextState(new Editor(), "data/mapXML/menu_empty.xml");
			}
		});
		
		buttonSettings.setOnArm(new Trigger() {
			@Override
			public void make() {
				menu.selectorVisible = !menu.selectorVisible;
				System.out.println("option");
				//GameMaster.gm.requestNextState(new MenuOption(), "data/mapXML/menu_empty.xml");
			}
		});
		
		buttonQuit.setOnArm(new Trigger() {
			@Override
			public void make() {
				Main.Reqclose();
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