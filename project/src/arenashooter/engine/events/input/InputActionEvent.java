package arenashooter.engine.events.input;

import arenashooter.engine.events.Event;
import arenashooter.engine.input.Action;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.input.Device;

public class InputActionEvent extends Event {
	
	private final Device device;
	private final Action action;
	private final ActionState actionState;

	public InputActionEvent(Device device , Action action , ActionState actionState) {
		this.action = action;
		this.actionState = actionState;
		this.device = device;
	}

	public Device getDevice() {
		return device;
	}

	public Action getAction() {
		return action;
	}

	public ActionState getActionState() {
		return actionState;
	}
}
