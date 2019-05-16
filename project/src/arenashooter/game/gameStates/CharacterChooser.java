package arenashooter.game.gameStates;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import arenashooter.engine.graphics.fonts.Text;
import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;
import arenashooter.engine.input.Action;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.entities.Controller;
import arenashooter.entities.spatials.CharacterSprite;
import arenashooter.entities.spatials.TextSpatial;
import arenashooter.game.GameMaster;
import arenashooter.game.Main;

public class CharacterChooser extends GameState {

	private HashMap<Device, Controller> controllers = new HashMap<>(1);
	private HashMap<Controller, CharacterSprite> sprites = new HashMap<>(1);
	final int posdedepart = -300;
	private int i = posdedepart;

	public Collection<Controller> getControllers() {
		return controllers.values();
	}

	@Override
	public void init() {
		super.init();
		Text text = new Text(Main.font, Text.TextAlignH.CENTER, "Choose your failleterre");
		TextSpatial textEnt = new TextSpatial(new Vec3f(0, -500, -10), new Vec3f(450), text);
		textEnt.attachToParent(map, "Text_Select");

		Text text2 = new Text(Main.font, Text.TextAlignH.CENTER, "Q or D to change your figther");
		TextSpatial textEnt2 = new TextSpatial(new Vec3f(0, -400, -10), new Vec3f(250), text2);
		textEnt2.attachToParent(map, "Text_char");

		Text text3 = new Text(Main.font, Text.TextAlignH.CENTER, "Z and S to change your skin");
		TextSpatial textEnt3 = new TextSpatial(new Vec3f(0, -350, -10), new Vec3f(250), text3);
		textEnt3.attachToParent(map, "Text_touch");

		Text text4 = new Text(Main.font, Text.TextAlignH.CENTER, "Press ENTER to go to the map chooser");
		TextSpatial textEnt4 = new TextSpatial(new Vec3f(0, 400, -10), new Vec3f(350), text4);
		textEnt4.attachToParent(map, "Text_touch2");

		Controller controllerKeyboard = new Controller(Device.KEYBOARD);
		controllers.put(Device.KEYBOARD, controllerKeyboard);
		GameMaster.gm.controllers.add(controllerKeyboard);
		CharacterSprite c = new CharacterSprite(new Vec2f(i, 0), controllerKeyboard.info);
		sprites.put(controllerKeyboard, c);
		i += 150;
		c.attachToParent(map, c.genName());
	}

	@Override
	public void update(double delta) {
		for (Device device : Device.values()) {
			if (Input.actionPressed(device, Action.UI_OK) && !controllers.keySet().contains(device)) {
				Controller newController = new Controller(device);
				controllers.put(device, newController);
				CharacterSprite c = new CharacterSprite(new Vec2f(i, 0), newController.info);
				sprites.put(newController, c);
				c.attachToParent(map, c.genName());
				i += 150;
			}
			// TODO : remove controller when UI_BACK is pressed
			if (Input.actionPressed(device, Action.UI_BACK) && controllers.keySet().contains(device)
					&& !device.equals(Device.KEYBOARD)) {

				CharacterSprite sp = sprites.get(controllers.get(device));
				
				
				sprites.get((controllers.get(device))).detach();
				sprites.remove((controllers.get(device)));
				controllers.remove(device);
				//i -= 150;
				
				// replacement des persos après suppr
				for (Map.Entry<Controller, CharacterSprite> entry : sprites.entrySet()) {
					Controller key = entry.getKey();
					CharacterSprite value = entry.getValue();
					//int j = i;
					float jj = value.parentPosition.x;
					if (!key.getDevice().equals(Device.KEYBOARD)) {
						
						if (jj > sp.parentPosition.x) {
							jj -= 150;
							Vec2f pos = new Vec2f(jj, 0);
							value.parentPosition.set(pos);
							//value.destroy();
							//Vec2f pos = new Vec2f(jj, 0);
							//sprites.remove(key);
							//CharacterSprite c = new CharacterSprite(pos, key.getCharInfo());
							//sprites.put(key, c);
							//c.attachToParent(map, c.genName());
						}
					}
				}
				i -= 150;

			}

		}

		// Update controllers
		for (Controller controller : controllers.values()) {
			controller.step(delta);

			// Temp sprite changing
			if (Input.actionJustPressed(controller.getDevice(), Action.UI_RIGHT)) {
				controller.info.nextClass();
				Vec2f pos = sprites.get(controller).parentPosition;
				sprites.get(controller).detach();
				CharacterSprite c = new CharacterSprite(pos, controller.info);
				sprites.put(controller, c);
				c.attachToParent(map, c.genName());
			} else if (Input.actionJustPressed(controller.getDevice(), Action.UI_LEFT)) {
				controller.info.previousClass();
				Vec2f pos = sprites.get(controller).parentPosition;
				sprites.get(controller).detach();
				CharacterSprite c = new CharacterSprite(pos, controller.info);
				sprites.put(controller, c);
				c.attachToParent(map, c.genName());
			} else if (Input.actionJustPressed(controller.getDevice(), Action.UI_UP)) {
				controller.info.nextSkin();
				Vec2f pos = sprites.get(controller).parentPosition;
				sprites.get(controller).detach();
				CharacterSprite c = new CharacterSprite(pos, controller.info);
				sprites.put(controller, c);
				c.attachToParent(map, c.genName());
			} else if (Input.actionJustPressed(controller.getDevice(), Action.UI_DOWN)) {
				controller.info.previousSkin();
				Vec2f pos = sprites.get(controller).parentPosition;
				sprites.get(controller).detach();
				CharacterSprite c = new CharacterSprite(pos, controller.info);
				sprites.put(controller, c);
				c.attachToParent(map, c.genName());
			}
		}

		if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_OK)) {
			GameMaster.gm.requestNextState(new MapChooser());
		} else if (Input.actionJustPressed(Device.KEYBOARD, Action.UI_BACK)) {
			GameMaster.gm.requestPreviousState();
		}

		super.update(delta);
	}

}