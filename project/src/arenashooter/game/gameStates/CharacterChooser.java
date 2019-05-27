package arenashooter.game.gameStates;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;
import arenashooter.engine.input.Action;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.ui.Menu;
import arenashooter.entities.Entity;
import arenashooter.entities.Music;
import arenashooter.entities.spatials.Camera;
import arenashooter.entities.spatials.CharacterSprite;
import arenashooter.entities.spatials.Sprite;
import arenashooter.entities.spatials.TextSpatial;
import arenashooter.game.Controller;
import arenashooter.game.GameMaster;
import arenashooter.game.Main;
import arenashooter.game.gameStates.engineParam.GameParam;

public class CharacterChooser extends GameState {

	private HashMap<Device, Controller> controllers = new HashMap<>(1);
	private HashMap<Controller, CharacterSprite> sprites = new HashMap<>(1);
	Stack<Controller> pileOrdreJoueur = new Stack<Controller>();
	private final double firstX = -3;
	private double nextSpriteX = firstX;
	private final double charOffset = 2;
	Menu menu = new Menu(6);
	private InputListener inputs = new InputListener();

	public CharacterChooser() {
		super(1);
		inputs.actions.add(new EventListener<InputActionEvent>() {
			@Override
			public void launch(InputActionEvent event) {
				// TODO Auto-generated method stub
				if (event.getActionState() == ActionState.JUST_PRESSED) {
					switch (event.getAction()) {
					case UI_CONTINUE:
						GameMaster.gm.controllers.clear();
						for (Controller cont : controllers.values()) {
							GameMaster.gm.controllers.add(cont);
						}
						Object[] variable = GameParam.maps.toArray();
						String[] chosenMaps = new String[variable.length];
						for (int i = 0; i < variable.length; i++) {
							chosenMaps[i] = (String) variable[i];
						}
						GameMaster.gm.requestNextState(new Game(GameParam.maps.size()), chosenMaps);
						break;

					case UI_OK:
						if (controllers.get(event.getDevice()) == null)
							addController(event.getDevice());
						break;

					case UI_BACK:
						if (!event.getDevice().equals(Device.KEYBOARD)) {
							if(controllers.get(event.getDevice()) != null) {
								removeController(event.getDevice());
							}
						} else {
							GameMaster.gm.requestPreviousState();
						}
						break;
					// case UI_DOWN:
					// controller.info.previousSkin();
					// Vec2f pos = sprites.get(controller).parentPosition;
					// Sprite number = (Sprite)
					// sprites.get(controller).getChild("body").getChild("Player_Number");
					// sprites.get(controller).detach();
					// CharacterSprite c = new CharacterSprite(pos, controller.info);
					// sprites.put(controller, c);
					// c.attachToParent(current, c.genName());
					// number.attachToParent(c.getChild("body"), "Player_Number");
					// break;
					default:
						break;
					}

				}
			}
		});
	}

	public Collection<Controller> getControllers() {
		return controllers.values();
	}

	private void addController(Device device) {
		Controller newController = new Controller(device);
		newController.playerNumber = pileOrdreJoueur.size();
		controllers.put(device, newController);
		pileOrdreJoueur.push(newController);

		GameMaster.gm.controllers.add(newController);
		CharacterSprite caracSprite = new CharacterSprite(new Vec2f(nextSpriteX, 0), newController.info);
		caracSprite.attachToParent(current, "PlayerSprite_" + pileOrdreJoueur.size());
		sprites.put(newController, caracSprite);
		Sprite newNumber = new Sprite(new Vec2f(),
				"data/sprites/interface/Player_" + (pileOrdreJoueur.size()) + "_Arrow.png");
		newNumber.getTexture().setFilter(false);
		newNumber.attachToParent(current.getChild("PlayerSprite_" + pileOrdreJoueur.size()).getChild("body"),
				"Player_Number");
		newNumber.localPosition = new Vec2f(0, -2);

		Main.log.info("CharacterSprite added at coordinates x = "+ nextSpriteX);
		Main.log.info("Player Number : " + controllers.get(device).playerNumber);
		
		nextSpriteX += charOffset;
		updatePlayersNumber();
	}

	private void removeController(Device device) {
		
		Main.log.info("Before\nCharacterChooser.controllers.size() : " + controllers.size());
		Main.log.info("CharacterChooser.sprites.size()" + sprites.size());
		Main.log.info("CharacterChoose.pileOrdreJoueur.size()" + pileOrdreJoueur.size());
		Main.log.info("GameMaster.gm.controllers.size()"+GameMaster.gm.controllers.size());

		CharacterSprite sp = sprites.get(controllers.get(device));
		sp.detach();
		sprites.remove((controllers.get(device)));
		pileOrdreJoueur.remove(controllers.get(device).playerNumber);
		GameMaster.gm.controllers.remove(controllers.get(device));
		controllers.remove(device);
		updatePlayersNumber();
		// i -= charOffset;

		// replacement des persos après suppr
		for (Map.Entry<Controller, CharacterSprite> entry : sprites.entrySet()) {
			Controller key = entry.getKey();
			CharacterSprite value = entry.getValue();
			// int j = i;
			float jj = value.parentPosition.x;
			if (!key.getDevice().equals(Device.KEYBOARD)) {

				if (jj > sp.parentPosition.x) {
					jj -= charOffset;
					Vec2f pos = new Vec2f(jj, 0);
					value.parentPosition.set(pos);
				}
			}
		}
		nextSpriteX -= charOffset;
		Main.log.info("After\nCharacterChooser.controllers.size() : " + controllers.size());
		Main.log.info("CharacterChooser.sprites.size()" + sprites.size());
		Main.log.info("CharacterChoose.pileOrdreJoueur.size()" + pileOrdreJoueur.size());
		Main.log.info("GameMaster.gm.controllers.size()"+GameMaster.gm.controllers.size());
	}

	/**
	 * Update player number in each controller and reposition character numbers on
	 * screen
	 */
	private void updatePlayersNumber() {

		for (int i = 0; i < pileOrdreJoueur.size(); i++) {
			Controller currentController = pileOrdreJoueur.get(i);
			currentController.playerNumber = i;
			
			Sprite number = new Sprite(new Vec2f(),
					"data/sprites/interface/Player_" + (currentController.playerNumber+1) + "_Arrow.png");
			number.attachToParent(sprites.get(currentController).getChild("body"),
					"Player_Number");
			number.localPosition = new Vec2f(0, -2);
			sprites.get(currentController).attachToParent(current, "PlayerSprite_" + currentController.playerNumber);
		}
	}

	@Override
	public void init() {
		super.init();

		Texture fondMenuTex = Texture.loadTexture("data/sprites/interface/Fond Menu_Score.png");
		fondMenuTex.setFilter(false);
		// UiImage bg = new UiImage(0, new Vec2f(177.78, 100), fondMenuTex, new Vec4f(1,
		// 1, 1, 1));
		// menu.setBackground(bg);

		Text text = new Text(Main.font, Text.TextAlignH.CENTER, "Choose your failleterre");
		TextSpatial textEnt = new TextSpatial(new Vec3f(0, -7, 0), new Vec3f(7.3f), text);
		textEnt.attachToParent(current, "Text_Select");

		Text text2 = new Text(Main.font, Text.TextAlignH.CENTER, "←Left and Right→to change class");
		TextSpatial textEnt2 = new TextSpatial(new Vec3f(0, -5.6, 0), new Vec3f(4.25f), text2);
		textEnt2.attachToParent(current, "Text_char");

		Text text3 = new Text(Main.font, Text.TextAlignH.CENTER, "↑Up and ↓Downto change skin");
		TextSpatial textEnt3 = new TextSpatial(new Vec3f(0, -5, 0), new Vec3f(4.25f), text3);
		textEnt3.attachToParent(current, "Text_touch");

		Text text4 = new Text(Main.font, Text.TextAlignH.CENTER, "Press Start to continue");
		TextSpatial textEnt4 = new TextSpatial(new Vec3f(0, 5.65, 0), new Vec3f(7.15f), text4);
		textEnt4.attachToParent(current, "Text_touch2");

		// Set camera
		Camera cam = new Camera(new Vec3f(0, 0, 8));
		cam.setFOV(90);
		current.attachToParent(cam, "camera");
		Window.setCamera(cam);

		addController(Device.KEYBOARD);
	}

	@Override
	public void update(double delta) {
		inputs.step(delta);

		// Update controllers
		for (Controller controller : controllers.values()) {
			controller.step(delta);

			// Temp sprite changing
			if (Input.actionJustPressed(controller.getDevice(), Action.UI_RIGHT)) {
				controller.info.nextClass();
				Vec2f pos = sprites.get(controller).parentPosition;
				Sprite number = (Sprite) sprites.get(controller).getChild("body").getChild("Player_Number");
				sprites.get(controller).detach();
				CharacterSprite c = new CharacterSprite(pos, controller.info);
				sprites.put(controller, c);
				c.attachToParent(current, c.genName());
				number.attachToParent(c.getChild("body"), "Player_Number");
			} else if (Input.actionJustPressed(controller.getDevice(), Action.UI_LEFT)) {
				controller.info.previousClass();
				Vec2f pos = sprites.get(controller).parentPosition;
				Sprite number = (Sprite) sprites.get(controller).getChild("body").getChild("Player_Number");
				sprites.get(controller).detach();
				CharacterSprite c = new CharacterSprite(pos, controller.info);
				sprites.put(controller, c);
				c.attachToParent(current, c.genName());
				number.attachToParent(c.getChild("body"), "Player_Number");
			} else if (Input.actionJustPressed(controller.getDevice(), Action.UI_UP)) {
				controller.info.nextSkin();
				Vec2f pos = sprites.get(controller).parentPosition;
				Sprite number = (Sprite) sprites.get(controller).getChild("body").getChild("Player_Number");
				sprites.get(controller).detach();
				CharacterSprite c = new CharacterSprite(pos, controller.info);
				sprites.put(controller, c);
				c.attachToParent(current, c.genName());
				number.attachToParent(c.getChild("body"), "Player_Number");
			} else if (Input.actionJustPressed(controller.getDevice(), Action.UI_DOWN)) {
				controller.info.previousSkin();
				Vec2f pos = sprites.get(controller).parentPosition;
				Sprite number = (Sprite) sprites.get(controller).getChild("body").getChild("Player_Number");
				sprites.get(controller).detach();
				CharacterSprite c = new CharacterSprite(pos, controller.info);
				sprites.put(controller, c);
				c.attachToParent(current, c.genName());
				number.attachToParent(c.getChild("body"), "Player_Number");
			}

		}

		super.update(delta);
		menu.update(delta);
	}

	public void draw() {

		super.draw();
		menu.draw();
	}

}