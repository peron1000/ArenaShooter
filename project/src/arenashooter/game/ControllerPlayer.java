package arenashooter.game;

import arenashooter.engine.graphics.Window;
import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;
import arenashooter.engine.input.Action;
import arenashooter.engine.input.Axis;
import arenashooter.engine.math.Vec2f;

public class ControllerPlayer extends Controller {
	/** Input device used by this controller */
	private Device device;

	public ControllerPlayer(Device device) {
		super();
		
		this.device = device;
		
		Main.log.info("Added controller for: " + Input.getDeviceInfo(device));
	}

	@Override
	public void step(double d) {
		if (getCharacter() != null) {
			if (!getCharacter().isDead() && device != null) {
				getCharacter().movementInput = Input.getAxis(device, Axis.MOVE_X);
				if (device == Device.KEYBOARD) {
					Vec2f charPos = Vec2f.worldToScreen(getCharacter().getWorldPos());
					charPos.y *= -1;

					Vec2f mouseCentered = Input.mousePos.clone();
					mouseCentered.x /= Window.getWidth();
					mouseCentered.x -= .5;
					mouseCentered.y /= Window.getHeight();
					mouseCentered.y -= .5;
					mouseCentered.multiply(2);
					
					getCharacter().aimInput = Vec2f.direction(charPos, mouseCentered);
					getCharacter().isAiming = true;
				} else {
					getCharacter().aimInput = new Vec2f(Input.getAxis(device, Axis.AIM_X), Input.getAxis(device, Axis.AIM_Y)).angle();
					getCharacter().isAiming = isAiming();
				}

				if (Input.actionJustPressed(device, Action.JUMP))
					getCharacter().jump();
				else if (Input.actionPressed(device, Action.JUMP)) {
					getCharacter().planer();
				} else if (getCharacter().jumpi) {
					getCharacter().jumpStop();
				}

				if (Input.actionJustPressed(device, Action.PARRY)) {
					getCharacter().parryStart();
				} else if (Input.actionJustReleased(device, Action.PARRY)) {
					getCharacter().parryStop();
				} else if (Input.actionJustPressed(device, Action.ATTACK)) {
					getCharacter().attackStart(true);
				} else if (Input.actionPressed(device, Action.ATTACK)) {
					getCharacter().attackStart(false);
				} else if (Input.actionJustReleased(device, Action.ATTACK))
					getCharacter().attackStop();

				if (Input.actionJustPressed(device, Action.GET_ITEM))
					getCharacter().getItem();
				if (Input.actionJustPressed(device, Action.DROP_ITEM))
					getCharacter().throwItem();
			} else
				getCharacter().movementInput = 0;
		}
	}

	private boolean isAiming() {
		return Math.abs(Input.getAxis(device, Axis.AIM_X)) > 0.3f || Math.abs(Input.getAxis(device, Axis.AIM_Y)) > 0.3f;
	}

	public Device getDevice() {
		return device;
	}
}
