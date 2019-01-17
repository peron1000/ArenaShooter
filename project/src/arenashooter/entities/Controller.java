package arenashooter.entities;

import arenashooter.engine.Device;
import arenashooter.engine.Input;
import arenashooter.engine.Input.Action;
import arenashooter.engine.Input.Axis;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.spatials.Character;

public class Controller {

	private Device device;
	private Character character;

	public Controller(Device device) {
		this.device = device;
	}

	public void setCharacter(Character c) {
		character = c;
	}

	public Character getCharacter() {
		return character;
	}

	public void step(double d) {
		if (!character.isDead()) {
			character.movementInput = Input.getAxis(device, Axis.MOVE_X);
			if (device == Device.KEYBOARD) {
				character.aimInput = new Vec2f(Input.getAxis(device, Axis.AIM_X) + character.position.x,
						Input.getAxis(device, Axis.AIM_Y) + character.position.y).angle();
			} else {
				character.aimInput = new Vec2f(Input.getAxis(device, Axis.AIM_X), Input.getAxis(device, Axis.AIM_Y))
						.angle();
			}

			if (Input.actionPressed(device, Action.JUMP)) {
				character.jump(3000);
			}
			if (Input.actionPressed(device, Action.ATTACK))
				character.attack();
			if (Input.actionJustPressed(device, Action.GET_ITEM))
				character.getItem();
			if (Input.actionJustPressed(device, Action.DROP_ITEM))
				character.dropItem();
		} else
			character.movementInput = 0;
	}
}
