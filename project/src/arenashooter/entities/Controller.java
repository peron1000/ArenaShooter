package arenashooter.entities;

import arenashooter.engine.Device;
import arenashooter.engine.Input;
import arenashooter.engine.Input.Action;
import arenashooter.engine.Input.Axis;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.CharacterInfo;
import arenashooter.game.GameMaster;

public class Controller {
	/** Input device used by this controller */
	private Device device;
	/** This controller's character information */
	private CharacterInfo charInfo;
	
	/** Currently possessed character */
	private Character character;

	public Controller(Device device) {
		this.device = device;
		charInfo = new CharacterInfo();
	}

	public Character createNewCharacter(Vec2f spawn) {
		character = charInfo.createNewCharacter(spawn);
		return character;
	}
	
	public CharacterInfo getCharInfo() { return charInfo; }

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
				character.isAiming = isAiming();
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

	private boolean isAiming() {
		return Math.abs(Input.getAxis(device, Axis.AIM_X)) > 0.3f || Math.abs(Input.getAxis(device, Axis.AIM_Y)) > 0.3f;
	}
}
