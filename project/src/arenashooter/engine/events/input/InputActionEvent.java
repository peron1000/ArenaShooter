package arenashooter.engine.events.input;

import arenashooter.engine.events.Event;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.input.ActionTest;
import arenashooter.engine.input.Device;

/**
 * @author Nathan
 * Package of information inherit for a Action Event
 */
public class InputActionEvent extends Event {
	
	private final Device device;
	private final ActionTest action;
	private final ActionState actionState;

	public InputActionEvent(Device device , ActionTest action , ActionState actionState) {
		this.action = action;
		this.actionState = actionState;
		this.device = device;
	}

	public Device getDevice() {
		return device;
	}

	public ActionTest getAction() {
		return action;
	}

	public ActionState getActionState() {
		return actionState;
	}
}
