package arenashooter.entities;

import arenashooter.engine.graphics.Window;
import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;
import arenashooter.engine.input.Action;
import arenashooter.engine.input.Axis;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.spatials.Character;
import arenashooter.game.CharacterClass;
import arenashooter.game.GameMaster;
import arenashooter.game.gameStates.Game;

public class Controller {
	/** Input device used by this controller */
	private Device device;
	/** This controller's character information */
	public CharacterClass info;
	/** Currently possessed character */
	private Character character;
	boolean deadChar = false;

	public boolean hasDeadChar() {
		return deadChar;
	}

	public Controller(Device device) {
		this.device = device;
		info = CharacterClass.Agile;

		System.out.println("Added controller for:\n " + Input.getDeviceInfo(device));
	}

	public Character createNewCharacter(Game game, Vec2f spawn) {
		if (character != null)
			character.death();
		character = info.createNewCharacter(spawn);
		character.controller = this;
		game.registerCharacter(character);
		deadChar = false;
		return character;
	}

	public Character getCharacter() {
		return character;
	}

	public void death() {
		deadChar = true;
		if (GameMaster.current instanceof Game) {
			((Game) GameMaster.current).characterDeath(this, character);
		}
		character.controller = null;
		character = null;
	}

	public void step(double d) {
		if (character != null) {
			if (!character.isDead()) {
				character.movementInput = Input.getAxis(device, Axis.MOVE_X);
				if (device == Device.KEYBOARD) {
					Vec2f charPos = Vec2f.worldToScreen(character.pos());
					charPos.y *= -1;

					Vec2f mouseCentered = Vec2f.add(Input.mousePos,
							new Vec2f(-Window.getWidth() / 2, -Window.getHeight() / 2));

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
