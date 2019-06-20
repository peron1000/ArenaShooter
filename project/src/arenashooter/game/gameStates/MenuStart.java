package arenashooter.game.gameStates;

import java.util.function.Consumer;

import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.ui.Imageinput;
import arenashooter.engine.ui.MenuSettings;
import arenashooter.engine.ui.TabList;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiActionable;
import arenashooter.engine.ui.UiElement;
import arenashooter.engine.ui.UiListVertical;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.entities.spatials.TextSpatial;
import arenashooter.game.Main;
import arenashooter.game.gameStates.editor.Editor;

public class MenuStart extends GameState {
	/* MenuStart Menu */
	private UiImage selector = new UiImage(Texture.loadTexture("data/sprites/interface/Selector_MainMenu_tr.png")),
			background = new UiImage(Texture.loadTexture("data/sprites/interface/Fond Menu_Main.png")), logo,
			Escape = Imageinput.ESCAPE.getImage(), Up = Imageinput.UP.getImage(), Down = Imageinput.DOWN.getImage(),
			Right = Imageinput.RIGHT.getImage(), Left = Imageinput.LEFT.getImage(), Space = Imageinput.SPACE.getImage(),
			A = Imageinput.A.getImage(), B = Imageinput.B.getImage();
	private Label Confirm, Back, Change;
	private TabList<UiActionable> mainMenu = new TabList<>();
	private TabList<UiActionable> current = mainMenu;

	private InputListener inputs = new InputListener();

	public MenuStart() {
		super(1);
		selector.setScale(100, 8);

		// logo
		Texture logoTex = Texture.loadTexture("data/logo.png");
		logo = new UiImage(logoTex);
		logo.setScale(logoTex.getWidth() / 6, logoTex.getHeight() / 6);
		logo.setPosition(-2.25, -24);

		// Text explanation
//		Right.setScale(Right.getScale().x / 3.142, Right.getScale().y / 3.142);
//		Right.setPosition(-30, 43);
//
//		Left.setScale(Left.getScale().x / 3.142, Left.getScale().y / 3.142);
//		Left.setPosition(-35, 43);

		Space.setScale(Space.getScale().x / 2, Space.getScale().y / 2);
		Space.setPosition(-5, 47);

		A.setScale(A.getScale().x / 3.142, A.getScale().y / 3.142);
		A.setPosition(0, 47);

		Confirm = new Label(" : Confirm", 3);
		Confirm.setPosition(8, 47);

//		Back = new Label(" : Back", 3);
//		Back.setPosition(25, 43);
//
//		Change = new Label(" : Change", 3);
//		Change.setPosition(-25, 43);
//
//		Escape.setScale(Escape.getScale().x / 2, Escape.getScale().y / 2);
//		Escape.setPosition(15, 43);
//
//		B.setScale(B.getScale().x / 3.142, B.getScale().y / 3.142);
//		B.setPosition(20, 43);

		// Button
		Button buttonPlay = new Button("Play");
		Button buttonEditor = new Button("Map Editor");
		Button buttonSettings = new Button("Settings");
		Button buttonQuit = new Button("Quit");

		background.setScale(178, 100);

		UiListVertical<Button> uilist = new UiListVertical<>();
		uilist.addElements(buttonPlay, buttonEditor, buttonSettings, buttonQuit);
		for (Button button : uilist) {
			button.setRectangleVisible(false);
			button.setScale(80, 8);
		}
		mainMenu.addBind("MainMenu", uilist);
		mainMenu.setTitleVisible(false);
		mainMenu.setSpacingForeachList(0.1);
		mainMenu.setPosition(0, 10);

		buttonPlay.setOnArm(new Trigger() {
			@Override
			public void make() {
				Main.getGameMaster().requestNextState(new Config(), "data/mapXML/menu_empty.xml");
			}
		});

		buttonEditor.setOnArm(new Trigger() {
			@Override
			public void make() {
				Main.getGameMaster().requestNextState(new Editor(), "data/mapXML/menu_empty.xml");
			}
		});

		buttonSettings.setOnArm(new Trigger() {
			@Override
			public void make() {
				current = new MenuSettings(20, new Trigger() {

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

		selector.setPosition(mainMenu.getTarget().getPosition());

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
						//current.continueAction();
						current.selectAction();
						break;
					case UI_BACK:
						if (!current.backAction()) {
							Main.getGameMaster().requestNextState(new Intro(), "data/mapXML/menu_intro.xml");
						}
						break;
					
					default:
						break;
					}
					selector.setPositionLerp(current.getTarget().getPosition().x, current.getTarget().getPosition().y,
							32);
				}
			}
		});
	}

	private void actionOnAll(Consumer<UiElement> c) {
		c.accept(background);
		c.accept(logo);
//		if (current != mainMenu) {
//			c.accept(Left);
//			c.accept(Right);
//			c.accept(Change);
//		}
		c.accept(A);
		c.accept(Space);
		c.accept(Confirm);
		
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