package arenashooter.engine.events.input;

import arenashooter.engine.events.Event;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.input.ActionV2;
import arenashooter.engine.input.Device;

/**
 * @author Nathan
 * Package of information inherit for a Action Event
 */
public class InputActionEvent extends Event {
	
	private final Device device;
	private final ActionV2 action;
	private final ActionState actionState;

	public InputActionEvent(Device device , ActionV2 action , ActionState actionState) {
		this.action = action;
		this.actionState = actionState;
		this.device = device;
	}

	public Device getDevice() {
		return device;
	}

	public ActionV2 getAction() {
		return action;
	}

	public ActionState getActionState() {
		return actionState;
	}
}
