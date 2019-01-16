package arenashooter.game.gameStates;

import java.util.ArrayList;

import arenashooter.engine.Device;
import arenashooter.engine.Input;
import arenashooter.engine.Input.Action;
import arenashooter.entities.Controller;
import arenashooter.game.GameMaster;

public class Start extends GameState {
	public static final Start start = new Start();

	private ArrayList<Controller> controllers = new ArrayList<>();

	private Start() {
		controllers.add(new Controller(Device.KEYBOARD));
	}

	public ArrayList<Controller> getControllers() {
		return controllers;
	}

	@Override
	public void update(double delta) {
		for (Device device : Device.values()) {
			if(Input.actionPressed(device, Action.JUMP)) {
				controllers.add(new Controller(device));
			}
			// TODO : remove controller when B is pressed
		}
		if(Input.actionPressed(Device.KEYBOARD, Action.JUMP)) {
			GameMaster.gm.requestNextState();
		}
	}

}
