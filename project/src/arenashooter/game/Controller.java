package arenashooter.game;

import arenashooter.engine.DamageInfo;
import arenashooter.engine.DamageType;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;
import arenashooter.engine.input.Action;
import arenashooter.engine.input.Axis;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.spatials.Character;
import arenashooter.game.gameStates.Game;

public class Controller {
	/** Input device used by this controller */
	private Device device;
	/** This controller's character information */
	public CharacterInfo info;
	/** Currently possessed character */
	private Character character;
	public int team = 1;
	boolean deadChar = false;
	
	/**
	 * Stats for Score
	 */
	public int roundsWon = 0;
	public int deaths = 0;
	public int kills = 0;
	public int flagCatch = 0;
	public int flagCapture = 0;
	public int flagRetrieve = 0;
	
	
	public boolean hasDeadChar() {
		return deadChar;
	}

	public Controller(Device device) {
		this.device = device;
		info = new CharacterInfo(CharacterClass.Agile);

		Main.log.info("Added controller for: " + Input.getDeviceInfo(device));
	}

	public Character createNewCharacter(Vec2f spawn) {
		if (character != null)
			character.takeDamage(new DamageInfo(0, DamageType.MISC_ONE_SHOT, new Vec2f(), null));
		character = info.createNewCharacter(spawn);
		character.controller = this;
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
					Vec2f charPos = Vec2f.worldToScreen(character.getWorldPos());
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

				if (Input.actionJustPressed(device, Action.JUMP))
					character.jump();
				else if(Input.actionPressed(device, Action.JUMP)) {
					character.planer();
				} else if(character.jumpi) {
					character.jumpStop();
				}
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
