package arenashooter.game.gameStates;

import java.util.Collection;
import java.util.HashMap;

import arenashooter.engine.Device;
import arenashooter.engine.Input;
import arenashooter.engine.Input.Action;
import arenashooter.engine.graphics.PostProcess;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Controller;
import arenashooter.entities.spatials.CharacterInfo;
import arenashooter.entities.spatials.CharacterSprite;
import arenashooter.game.GameMaster;

public class CharacterChooser extends GameState {
	
	private HashMap<Device, Controller> controllers = new HashMap<>(1);
	
	private int i = -300;
	
	public Collection<Controller> getControllers() {
		return controllers.values();
	}
	
	@Override
	public void init() {
		Window.postProcess = new PostProcess("data/shaders/post_process/pp_default");
		
		controllers.put(Device.KEYBOARD, new Controller(Device.KEYBOARD));
		CharacterSprite c = new CharacterSprite(new Vec2f(i, 0), new CharacterInfo());
		i += 150;
		c.attachToParent(map, c.genName());
	}
	
	@Override
	public void update(double delta) {
		for (Device device : Device.values()) {
			if(Input.actionPressed(device, Action.JUMP) && !controllers.keySet().contains(device)) {
				controllers.put(device, new Controller(device));
				CharacterSprite c = new CharacterSprite(new Vec2f(i, 0), new CharacterInfo());
				c.attachToParent(map, c.genName());
				i += 150;
				System.out.println("add controller");
			}
			// TODO : remove controller when B is pressed
		}

		if(Input.actionPressed(Device.KEYBOARD, Action.JUMP)) {
			GameMaster.gm.requestNextState();
		}
	}

}
