package arenashooter.game.gameStates;

import java.util.ArrayList;
import java.util.HashMap;

import arenashooter.engine.Device;
import arenashooter.entities.Controller;

public class Start extends GameState {
	public static final Start start = new Start();

	private HashMap<Controller, Boolean> controllers = new HashMap<>(17);

	private Start() {
		for (Device device : Device.values()) {
			controllers.put(new Controller(device), false);
		}
	}

	public ArrayList<Controller> getActivatedControllers() {
		ArrayList<Controller> result = new ArrayList<>();
		for (Controller controller : controllers.keySet()) {
			if(controllers.get(controller)) {
				result.add(controller);
			}
		}
		return result;
	}

	@Override
	public void update(double delta) {
		// TODO Auto-generated method stub
		for (Controller controller : controllers.keySet()) {
			if (controller.isJumping()) {
				controllers.put(controller, true);
			}
		}
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub

	}

}
