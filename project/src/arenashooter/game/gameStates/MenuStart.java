package arenashooter.game.gameStates;

import java.util.function.Consumer;

import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.ui.MenuSettings;
import arenashooter.engine.ui.TabList;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiActionable;
import arenashooter.engine.ui.UiElement;
import arenashooter.engine.ui.UiListVertical;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.game.GameMaster;
import arenashooter.game.Main;
import arenashooter.game.gameStates.editor.Editor;

public class MenuStart extends GameState {
	/* MenuStart Menu */
	private UiImage selector = new UiImage(Texture.loadTexture("data/sprites/interface/Selector_MainMenu_tr.png")),
			background = new UiImage(Texture.loadTexture("data/sprites/interface/Fond Menu_Main.png")), 
			logo;
	private TabList<UiActionable> mainMenu = new TabList<>();
	private TabList<UiActionable> current = mainMenu;

	private InputListener inputs = new InputListener();

	public MenuStart() {
		super(1);
		selector.setScale(100, 8);
		
		//logo
		Texture logoTex = Texture.loadTexture("data/logo.png");
		logo = new UiImage(logoTex);
		logo.setScale(logoTex.getWidth() / 6, logoTex.getHeight() / 6);
		logo.setPosition(-2.25, -24);
		
		//Button
		Button buttonPlay = new Button("Play");
		Button buttonEditor = new Button("Map Editor");
		Button buttonSettings = new Button("Settings");
		Button buttonQuit = new Button("Quit");
		
		background.setScale(178, 100);

		
		UiListVertical<Button> uilist = new UiListVertical<>();
		uilist.setSpacing(8);
		uilist.addElements(buttonPlay , buttonEditor , buttonSettings , buttonQuit);
		for (Button button : uilist) {
//			button.setScale(50, 5.5);
			button.setScale(8);
			button.setRectangleVisible(false);
		}
		mainMenu.addBind("Main Menu", uilist);
		uilist.setPosition(0, -5);
		mainMenu.setPosition(0, 20);

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
				current = new MenuSettings(20 , new Trigger() {
					
					@Override
					public void make() {
						current = mainMenu;
					}
				});
			}
		});

		buttonQuit.setOnArm(new Trigger() {
			@Override
			public void make() {
				Main.reqestClose();
			}
		});
		
		selector.setPosition(mainMenu.getTarget().getPosition().x, mainMenu.getTarget().getPosition().y);

		inputs.actions.add(new EventListener<InputActionEvent>() {
			@Override
			public void launch(InputActionEvent event) {
				if (event.getActionState() == ActionState.JUST_PRESSED) {
					switch (event.getAction()) {
					case UI_LEFT:
						current.leftAction();
						break;
					case UI_RIGHT:
						current.rightAction();
						break;
					case UI_UP:
						current.upAction();
						break;
					case UI_DOWN:
						current.downAction();
						break;
					case UI_OK:
						current.selectAction();
						break;
					case UI_CONTINUE:
						current.selectAction();
						break;
					case UI_BACK:
						if(!current.backAction()) {
							GameMaster.gm.requestNextState(new Intro(), "data/mapXML/menu_intro.xml");
						}
						break;

					default:
						break;
					}
					selector.setPosition(current.getTarget().getPosition().x, current.getTarget().getPosition().y);
				}
			}
		});
	}
	
	private void actionOnAll(Consumer<UiElement> c) {
		c.accept(background);
		c.accept(logo);
		c.accept(current);
		c.accept(selector);
	}

	@Override
	public void update(double delta) {
		inputs.step(delta);
		actionOnAll(e -> e.update(delta));
		super.update(delta);
	}

	@Override
	public void draw() {
		super.draw();
		Window.beginUi();
		actionOnAll(e -> e.draw());
		Window.endUi();
	}

}