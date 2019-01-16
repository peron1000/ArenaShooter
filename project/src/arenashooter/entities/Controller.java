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
	private boolean isJumping;

	public Controller(Device device) {
		this.device = device;
	}

	public void setCharacter(Character c) {
		character = c;
	}

	public Character getCharacter() {
		return character;
	}
	
	public boolean isJumping() {
		return isJumping;
	}

	public void step(double d) {
		isJumping = false;
		if (!character.isDead()) {
			character.movementInput = Input.getAxis(device, Axis.MOVE_X);
			character.aimInput = new Vec2f(Input.getAxis(device, Axis.AIM_X), Input.getAxis(device, Axis.AIM_Y));
			
			if (Input.actionPressed(device, Action.JUMP)) {
				character.jump(3000);
				isJumping = true;
			}
			if (Input.actionPressed(device, Action.ATTACK))
				character.attack();
			if (Input.actionJustPressed(device, Action.GET_ITEM))
				character.getItem();
			if(Input.actionJustPressed(device, Action.DROP_ITEM))
				character.dropItem();
		} else
			character.movementInput = 0;
	}
}
