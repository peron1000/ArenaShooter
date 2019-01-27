package arenashooter.game.gameStates;

import java.util.Collection;
import java.util.HashMap;

import arenashooter.engine.Device;
import arenashooter.engine.Input;
import arenashooter.engine.Input.Action;
import arenashooter.engine.graphics.PostProcess;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.graphics.fonts.Text;
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
	
	private int i = -300;
	
	public Collection<Controller> getControllers() {
		return controllers.values();
	}
	
	@Override
	public void init() {
		Window.postProcess = new PostProcess("data/shaders/post_process/pp_default");
		
		Text text = new Text(Main.font, Text.TextAlignH.CENTER, "Choose your failleterre");
		TextSpatial textEnt = new TextSpatial(new Vec3f(0, -500, -10), new Vec3f(450), text);
		textEnt.attachToParent(map, "Text_Select");
		
		Controller controllerKeyboard = new Controller(Device.KEYBOARD);
		controllers.put(Device.KEYBOARD, controllerKeyboard);
		CharacterSprite c = new CharacterSprite(new Vec2f(i, 0), controllerKeyboard.getCharInfo());
		sprites.put(controllerKeyboard, c);
		i += 150;
		c.attachToParent(map, c.genName());
	}
	
	@Override
	public void update(double delta) {
		for (Device device : Device.values()) {
			if(Input.actionPressed(device, Action.JUMP) && !controllers.keySet().contains(device)) {
				Controller newController = new Controller(device);
				controllers.put(device, newController);
				CharacterSprite c = new CharacterSprite(new Vec2f(i, 0), newController.getCharInfo());
				sprites.put(newController, c);
				c.attachToParent(map, c.genName());
				i += 150;
			}
			// TODO : remove controller when B is pressed
		}
		
		//Update controllers
		for(Controller controller : controllers.values()) {
			controller.step(delta);
			
			//Temp sprite changing
			if( Input.actionJustPressed(controller.getDevice(), Input.Action.UI_RIGHT) ) {
					controller.getCharInfo().tempSpriteNext();
					Vec2f pos = sprites.get(controller).position;
					sprites.get(controller).detach();
					CharacterSprite c = new CharacterSprite(pos, controller.getCharInfo());
					sprites.put(controller, c);
					c.attachToParent(map, c.genName());
			}
		}

		if(Input.actionPressed(Device.KEYBOARD, Action.JUMP)) {
			GameMaster.gm.requestNextState();
		}
		
		map.step(delta);
	}

}
