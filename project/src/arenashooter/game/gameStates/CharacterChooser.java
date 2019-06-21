package arenashooter.game.gameStates;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import arenashooter.engine.audio.Audio;
import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.input.Device;

import arenashooter.engine.input.ActionState;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.ui.Imageinput;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.entities.spatials.Camera;
import arenashooter.entities.spatials.CharacterSprite;
import arenashooter.entities.spatials.Sprite;
import arenashooter.entities.spatials.TextSpatial;
import arenashooter.game.Controller;
import arenashooter.game.ControllerPlayer;
import arenashooter.game.Main;
import arenashooter.game.gameStates.engineParam.GameParam;

public class CharacterChooser extends GameState {

	private Map<Device, Controller> controllers = new HashMap<>(1);
	private Map<Controller, CharacterSprite> sprites = new HashMap<>(1);
	Stack<Controller> pileOrdreJoueur = new Stack<Controller>();
	private final double firstX = -10.9;
	private final Vec2f secondRow = new Vec2f(-26.621, 4.6);
	private double nextSpriteX = firstX;
	private final double charOffset = 3.121;
	private InputListener inputs = new InputListener();
	private UiImage Escape = Imageinput.ESCAPE.getImage(), Up = Imageinput.UP.getImage(),
			Down = Imageinput.DOWN.getImage(), Right = Imageinput.RIGHT.getImage(), Left = Imageinput.LEFT.getImage(),
			Space = Imageinput.SPACE.getImage(), A = Imageinput.A.getImage(), B = Imageinput.B.getImage(), enter;

	public CharacterChooser() {
		inputs.actions.add(new EventListener<InputActionEvent>() {
			@Override
			public void launch(InputActionEvent event) {
				if (event.getActionState() == ActionState.JUST_PRESSED) {
					switch (event.getAction()) {
					case UI_UP:
						if (controllers.keySet().contains(event.getDevice())) {
							controllers.get(event.getDevice()).info.nextSkin();
							Vec2f posu = sprites.get(controllers.get(event.getDevice())).localPosition;
							Sprite numberu = (Sprite) sprites.get(controllers.get(event.getDevice())).getChild("body")
									.getChild("Player_Number");
							sprites.get(controllers.get(event.getDevice())).detach();
							CharacterSprite cu = new CharacterSprite(controllers.get(event.getDevice()).info);
							cu.localPosition.set(posu);
							sprites.put(controllers.get(event.getDevice()), cu);
							cu.attachToParent(current, cu.genName());
							numberu.attachToParent(cu.getChild("body"), "Player_Number");
							updatePlayers();
						}
						break;

					case UI_DOWN:
						if (controllers.keySet().contains(event.getDevice())) {
							controllers.get(event.getDevice()).info.previousSkin();
							Vec2f posd = sprites.get(controllers.get(event.getDevice())).localPosition;
							Sprite numberd = (Sprite) sprites.get(controllers.get(event.getDevice())).getChild("body")
									.getChild("Player_Number");
							sprites.get(controllers.get(event.getDevice())).detach();
							CharacterSprite cd = new CharacterSprite(controllers.get(event.getDevice()).info);
							cd.localPosition.set(posd);
							sprites.put(controllers.get(event.getDevice()), cd);
							cd.attachToParent(current, cd.genName());
							numberd.attachToParent(cd.getChild("body"), "Player_Number");
							updatePlayers();
						}
						break;

					case UI_RIGHT:
						if (controllers.keySet().contains(event.getDevice())) {
							controllers.get(event.getDevice()).info.nextClass();
							Vec2f posr = sprites.get(controllers.get(event.getDevice())).localPosition;
							Sprite numberr = (Sprite) sprites.get(controllers.get(event.getDevice())).getChild("body")
									.getChild("Player_Number");
							sprites.get(controllers.get(event.getDevice())).detach();
							CharacterSprite cr = new CharacterSprite(controllers.get(event.getDevice()).info);
							cr.localPosition.set(posr);
							sprites.put(controllers.get(event.getDevice()), cr);
							cr.attachToParent(current, cr.genName());
							numberr.attachToParent(cr.getChild("body"), "Player_Number");
							updatePlayers();
						}
						break;

					case UI_LEFT:
						if (controllers.keySet().contains(event.getDevice())) {
							controllers.get(event.getDevice()).info.previousClass();
							Vec2f posl = sprites.get(controllers.get(event.getDevice())).localPosition;
							Sprite numberl = (Sprite) sprites.get(controllers.get(event.getDevice())).getChild("body")
									.getChild("Player_Number");
							sprites.get(controllers.get(event.getDevice())).detach();
							CharacterSprite cl = new CharacterSprite(controllers.get(event.getDevice()).info);
							cl.localPosition.set(posl);
							sprites.put(controllers.get(event.getDevice()), cl);
							cl.attachToParent(current, cl.genName());
							numberl.attachToParent(cl.getChild("body"), "Player_Number");
							updatePlayers();
						}
						break;
					case UI_CONTINUE:
						// Device needs to have a controller to start the game
						if (!controllers.containsKey(event.getDevice())) {
							Main.log.warn(event.getDevice()+" tried to start the game but doesn't have a controller");
							break;
						}

						Main.getGameMaster().controllers.clear();
						for (Controller cont : controllers.values()) {
							Main.getGameMaster().controllers.add(cont);
						}
						Object[] variable = GameParam.maps.toArray();
						String[] chosenMaps = new String[variable.length];
						for (int i = 0; i < variable.length; i++) {
							chosenMaps[i] = (String) variable[i];
						}
						Main.getGameMaster().requestGame();
						break;

					case UI_OK:
						if (!controllers.keySet().contains(event.getDevice())) {
							addController(event.getDevice());
						}

						break;

					case UI_BACK:
						if (!event.getDevice().equals(Device.KEYBOARD)) {
							if (controllers.keySet().contains(event.getDevice())) {
								removeController(event.getDevice());
							}
						} else {
							Main.getGameMaster().requestPreviousState();
						}
						break;

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
		ControllerPlayer newController = new ControllerPlayer(device);
		newController.playerNumber = pileOrdreJoueur.size();
		controllers.put(device, newController);
		pileOrdreJoueur.push(newController);

		Main.getGameMaster().controllers.add(newController);
		CharacterSprite caracSprite = new CharacterSprite(newController.info);

		if (pileOrdreJoueur.size() > 7) {
			caracSprite.localPosition.set(nextSpriteX + secondRow.x, -2.5 + secondRow.y);
		} else {
			caracSprite.localPosition.set(nextSpriteX, -2.5);
		}

		caracSprite.attachToParent(current, "PlayerSprite_" + pileOrdreJoueur.size());
		sprites.put(newController, caracSprite);
		Sprite newNumber = new Sprite(new Vec2f(),
				"data/sprites/interface/Player_" + (pileOrdreJoueur.size()) + "_Arrow.png");
		newNumber.getTexture().setFilter(false);
		newNumber.localPosition = new Vec2f(0, -2.2);
		newNumber.attachToParent(current.getChild("PlayerSprite_" + pileOrdreJoueur.size()).getChild("body"),
				"Player_Number");

		Main.log.debug("CharacterSprite added at coordinates x = " + nextSpriteX);
		Main.log.info("Player Number : " + newController.playerNumber);

		nextSpriteX += charOffset;
		updatePlayers();

		Vec2f iconPos = caracSprite.localPosition.clone();
		iconPos.y += 1.7;
		String texName;
		switch (newController.getCharInfo().getCharClass()) {
		case Heavy:
			texName = "data/sprites/characters/All_Icon_For_Classes/Icon_Heavy.png";
			break;
		case Agile:
			texName = "data/sprites/characters/All_Icon_For_Classes/Icon_Fast.png";
			break;
		case Aqua:
			texName = "data/sprites/characters/All_Icon_For_Classes/Icon_Aqua.png";
			break;
		case Bird:
			texName = "data/sprites/characters/All_Icon_For_Classes/Icon_Birbs.png";
			break;
		default:
			texName = "data/default_texture.png";
			break;
		}
		Sprite classIcon = new Sprite(iconPos, texName);
		float iconWidth = (float) (classIcon.getTexture().getWidth() * .02);
		float iconHeigth = (float) (classIcon.getTexture().getHeight() * .02);
		classIcon.size.set(iconWidth, iconHeigth);
		classIcon.attachToParent(current, "class_Icon_Player_" + newController.playerNumber);
		classIcon.getTexture().setFilter(false);

		Audio.playSound("data/sound/ui/ui_join.ogg", AudioChannel.UI, 1, 1);
	}

	private void removeController(Device device) {
		Main.log.info("Before\nCharacterChooser.controllers.size() : " + controllers.size());
		Main.log.info("CharacterChooser.sprites.size()" + sprites.size());
		Main.log.info("CharacterChoose.pileOrdreJoueur.size()" + pileOrdreJoueur.size());
		Main.log.info("GameMaster.gm.controllers.size()" + Main.getGameMaster().controllers.size());

		if (current.getChild("class_Icon_Player_" + controllers.get(device).playerNumber) != null) {
			current.getChild("class_Icon_Player_" + controllers.get(device).playerNumber).detach();
		}
		CharacterSprite charSprite = sprites.get(controllers.get(device));
		charSprite.detach();
		sprites.remove((controllers.get(device)));
		pileOrdreJoueur.remove(controllers.get(device).playerNumber);
		Main.getGameMaster().controllers.remove(controllers.get(device));
		controllers.remove(device);
		updatePlayers();
		// i -= charOffset;

		// Reposition sprites
		for (Map.Entry<Controller, CharacterSprite> entry : sprites.entrySet()) {
			CharacterSprite sprite = entry.getValue();
			if (sprite.localPosition.x > charSprite.localPosition.x) {
				sprite.localPosition.x -= charOffset;
			}
		}
		nextSpriteX -= charOffset;
		Main.log.info("After\nCharacterChooser.controllers.size() : " + controllers.size());
		Main.log.info("CharacterChooser.sprites.size()" + sprites.size());
		Main.log.info("CharacterChoose.pileOrdreJoueur.size()" + pileOrdreJoueur.size());
		Main.log.info("GameMaster.gm.controllers.size()" + Main.getGameMaster().controllers.size());

		Audio.playSound("data/sound/ui/ui_leave.ogg", AudioChannel.UI, 1, 1);
	}

	/**
	 * Update player number in each controller and reposition character numbers on
	 * screen
	 */
	private void updatePlayers() {

		for (int i = 0; i < pileOrdreJoueur.size(); i++) {
			Controller currentController = pileOrdreJoueur.get(i);
			currentController.playerNumber = i;

			if (i < 8)
				sprites.get(currentController).localPosition.set(firstX + charOffset * i, -2.5);
			else
				sprites.get(currentController).localPosition.set(firstX + charOffset * i + secondRow.x,
						-2.5 + secondRow.y);

			Sprite number = new Sprite(new Vec2f(),
					"data/sprites/interface/Player_" + (currentController.playerNumber + 1) + "_Arrow.png");
			number.localPosition = new Vec2f(0, -2.2);
			number.attachToParent(sprites.get(currentController).getChild("body"), "Player_Number");
			sprites.get(currentController).attachToParent(current, "PlayerSprite_" + currentController.playerNumber);

			if (current.getChild("class_Icon_Player_" + i) != null) {
				current.getChild("class_Icon_Player_" + i).detach();
			}

			Vec2f iconPos = sprites.get(currentController).localPosition.clone();
			iconPos.y += 1.7;
			String texName;
			switch (currentController.getCharInfo().getCharClass()) {
			case Heavy:
				texName = "data/sprites/characters/All_Icon_For_Classes/Icon_Heavy.png";
				break;
			case Agile:
				texName = "data/sprites/characters/All_Icon_For_Classes/Icon_Fast.png";
				break;
			case Aqua:
				texName = "data/sprites/characters/All_Icon_For_Classes/Icon_Aqua.png";
				break;
			case Bird:
				texName = "data/sprites/characters/All_Icon_For_Classes/Icon_Birbs.png";
				break;
			default:
				texName = "data/default_texture.png";
				break;
			}
			Sprite classIcon = new Sprite(iconPos, texName);
			float iconWidth = (float) (classIcon.getTexture().getWidth() * .02);
			float iconHeigth = (float) (classIcon.getTexture().getHeight() * .02);
			classIcon.size.set(iconWidth, iconHeigth);
			classIcon.attachToParent(current, "class_Icon_Player_" + currentController.playerNumber);
			classIcon.getTexture().setFilter(false);
		}
	}

	@Override
	public void init() {
		super.init();

		Vec4f color1 = new Vec4f(1, 1, 1, 0.5);
		Text text = new Text(Main.font, Text.TextAlignH.CENTER, Text.TextAlignV.TOP, "Choose your Failleterre");
		TextSpatial textEnt = new TextSpatial(new Vec3f(0, -7.5, 0), new Vec3f(7.3f), text);
		textEnt.setColor(color1);
		textEnt.attachToParent(current, "Text_Select");

		// Join
		Text text66 = new Text(Main.font, Text.TextAlignH.CENTER, Text.TextAlignV.TOP, " : Join");
		TextSpatial textEnt66 = new TextSpatial(new Vec3f(1.5, 5.55, 0), new Vec3f(5f), text66);
		textEnt66.setColor(color1);
		textEnt66.attachToParent(current, "Text66");

		Space.setScale(Space.getScale().x / 2, Space.getScale().y / 2);
		Space.setPosition(-3, 37);
		Space.getMaterial().getParamTex("image").setFilter(false);

		A.setScale(A.getScale().x / 3.142, A.getScale().y / 3.142);
		A.setPosition(2, 37);
		A.getMaterial().getParamTex("image").setFilter(false);

		// Back
		Text text77 = new Text(Main.font, Text.TextAlignH.CENTER, Text.TextAlignV.TOP, "  : Back");
		TextSpatial textEnt77 = new TextSpatial(new Vec3f(1.5, 6.55, 0), new Vec3f(5f), text77);
		textEnt77.setColor(color1);
		textEnt77.attachToParent(current, "Text77");

		Escape.setScale(Escape.getScale().x / 2, Escape.getScale().y / 2);
		Escape.setPosition(-3, 43);
		Escape.getMaterial().getParamTex("image").setFilter(false);

		B.setScale(B.getScale().x / 3.142, B.getScale().y / 3.142);
		B.setPosition(2, 43);
		B.getMaterial().getParamTex("image").setFilter(false);
		// Skins

		Text text2 = new Text(Main.font, Text.TextAlignH.CENTER, Text.TextAlignV.TOP, " : change skin");
		TextSpatial textEnt2 = new TextSpatial(new Vec3f(7, 5.55, 0), new Vec3f(4.25f), text2);
		textEnt2.attachToParent(current, "Text_char");

		Left.setScale(Left.getScale().x / 3.142, Left.getScale().y / 3.142);
		Left.setPosition(27, 37);
		Left.getMaterial().getParamTex("image").setFilter(false);

		Right.setScale(Right.getScale().x / 3.142, Right.getScale().y / 3.142);
		Right.setPosition(32, 37);
		Right.getMaterial().getParamTex("image").setFilter(false);

		// Class

		Text text3 = new Text(Main.font, Text.TextAlignH.CENTER, Text.TextAlignV.TOP, "   : change class");
		TextSpatial textEnt3 = new TextSpatial(new Vec3f(7, 6.55, 0), new Vec3f(4.25f), text3);
		textEnt3.attachToParent(current, "Text_touch");

		Up.setScale(Up.getScale().x / 3.142, Up.getScale().y / 3.142);
		Up.setPosition(27, 43);
		Up.getMaterial().getParamTex("image").setFilter(false);
		Down.setScale(Down.getScale().x / 3.142, Down.getScale().y / 3.142);
		Down.setPosition(32, 43);
		Down.getMaterial().getParamTex("image").setFilter(false);

		// botton_start
		Texture enterTex = Texture.loadTexture("data/sprites/interface/Button_Start.png");
		enter = new UiImage(enterTex);
		enter.setPosition(new Vec2f(72, 39));
		enter.setScale(enterTex.getWidth() / 3, enterTex.getHeight() / 3);
		enter.getMaterial().getParamTex("image").setFilter(false);

		// TODO: Classes explanation.

		Sprite backGroundCharacChooser = new Sprite(new Vec2f(),
				"data/sprites/interface/BackGround_CharacterChooser_PreRendered.png");
		backGroundCharacChooser.attachToParent(current, "BackGround");
		Texture tex = backGroundCharacChooser.getTexture();
		backGroundCharacChooser.size.set(tex.getWidth() * .0711, tex.getHeight() * .0711);
		backGroundCharacChooser.zIndex = -126;
		backGroundCharacChooser.getTexture().setFilter(false);

		Sprite birdsIcon = new Sprite(new Vec2f(-13, 6), "data/sprites/characters/All_Icon_For_Classes/Icon_Birbs.png");
		birdsIcon.attachToParent(current, "Icon_Bird");
		float iconWidth = (float) (birdsIcon.getTexture().getWidth() * .05);
		float iconHeigth = (float) (birdsIcon.getTexture().getHeight() * .05);
		birdsIcon.size.set(iconWidth, iconHeigth);
		birdsIcon.getTexture().setFilter(false);
		Sprite heavyIcon = new Sprite(new Vec2f(-11, 6), "data/sprites/characters/All_Icon_For_Classes/Icon_Heavy.png");
		heavyIcon.attachToParent(current, "Icon_Heavy");
		heavyIcon.size.set(iconWidth, iconHeigth);
		heavyIcon.getTexture().setFilter(false);
		Sprite agileIcon = new Sprite(new Vec2f(-9, 6), "data/sprites/characters/All_Icon_For_Classes/Icon_Fast.png");
		agileIcon.attachToParent(current, "Icon_Agile");
		agileIcon.size.set(iconWidth, iconHeigth);
		agileIcon.getTexture().setFilter(false);
		Sprite aquaIcon = new Sprite(new Vec2f(-7, 6), "data/sprites/characters/All_Icon_For_Classes/Icon_Aqua.png");
		aquaIcon.attachToParent(current, "Icon_Aqua");
		aquaIcon.size.set(iconWidth, iconHeigth);
		aquaIcon.getTexture().setFilter(false);

		// Set camera
		Camera cam = new Camera(new Vec3f(0, 0, 8));
		cam.setFOV(90);
		current.attachToParent(cam, "camera");
		Window.setCamera(cam);
	}

	@Override
	public void update(double delta) {
		super.update(delta);
		inputs.step(delta);
	}

	public void draw() {
		super.draw();
		A.draw();
		Space.draw();
		B.draw();
		Escape.draw();
		Down.draw();
		Up.draw();
		Left.draw();
		Right.draw();
		enter.draw();
	}

}