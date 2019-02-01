package arenashooter.entities;

import arenashooter.engine.Device;
import arenashooter.engine.Input;
import arenashooter.engine.Input.Action;
import arenashooter.engine.Input.Axis;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.CharacterInfo;

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

		System.out.println("Added controller for:\n " + Input.getDeviceInfo(device));
	}

	public Character createNewCharacter(Vec2f spawn) {
		character = charInfo.createNewCharacter(spawn);
		return character;
	}

	public CharacterInfo getCharInfo() {
		return charInfo;
	}

	public Character getCharacter() {
		return character;
	}

	public void step(double d) {
		if (character != null) {
			if (!character.isDead()) {
				character.movementInput = Input.getAxis(device, Axis.MOVE_X);
				if (device == Device.KEYBOARD) {
					Vec2f charPos = Vec2f.worldToScreen(character.position);
					Vec2f mouseCentered = Vec2f.add(Input.mousePos,
							new Vec2f(-Window.getWidth() / 2, -Window.getHeight() / 2));

					System.out.println("Char: " + charPos);
					System.out.println("Mouse:" + mouseCentered);
					// TODO: Fix this
					character.aimInput = Vec2f.subtract(mouseCentered, charPos).angle();
					character.isAiming = true;
				} else {
					character.aimInput = new Vec2f(Input.getAxis(device, Axis.AIM_X), Input.getAxis(device, Axis.AIM_Y))
							.angle();
					character.isAiming = isAiming();
				}

				if (Input.actionPressed(device, Action.JUMP))
					character.jump(3000);

				if (Input.actionJustPressed(device, Action.ATTACK))
					character.attackStart();
				else if (Input.actionJustReleased(device, Action.ATTACK))
					character.attackStop();

				
				if (Input.actionJustPressed(device, Action.GET_ITEM))
					character.getItem();
				if (Input.actionJustPressed(device, Action.DROP_ITEM))
					character.dropItem();
			} else
				character.movementInput = 0;
		}
	}

	private boolean isAiming() {
		return Math.abs(Input.getAxis(device, Axis.AIM_X)) > 0.3f || Math.abs(Input.getAxis(device, Axis.AIM_Y)) > 0.3f;
	}

	public Device getDevice() {
		return device;
	}
}
