package arenashooter.game.gameStates;

import java.util.Collection;
import java.util.HashMap;

import arenashooter.engine.Device;
import arenashooter.engine.Input;
import arenashooter.engine.Input.Action;
import arenashooter.entities.Controller;
import arenashooter.game.GameMaster;

public class Start extends GameState {
	private HashMap<Device, Controller> controllers = new HashMap<>(1);

	public Start() {
		controllers.put(Device.KEYBOARD, new Controller(Device.KEYBOARD));
	}

	public Collection<Controller> getControllers() {
		return controllers.values();
	}

	@Override
	public void update(double delta) {
		for (Device device : Device.values()) {
			if(Input.actionPressed(device, Action.JUMP) && !controllers.keySet().contains(device)) {
				controllers.put(device, new Controller(device));
			}
			// TODO : remove controller when B is pressed
		}
		if(Input.actionPressed(Device.KEYBOARD, Action.JUMP)) {
			GameMaster.gm.requestNextState();
		}
	}

}
