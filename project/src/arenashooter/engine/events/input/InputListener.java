package arenashooter.engine.events.input;

import java.util.LinkedList;

import arenashooter.engine.events.EventListener;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.input.ActionTest;
import arenashooter.engine.input.AxisTest;
import arenashooter.engine.input.Device;

/**
 * @author Nathan
 * This Class launch an Event if an Input is detected
 * </br>[Don't forget to step]
 */
public class InputListener {
	/**
	 * List of Event in case of an inputs from Keyboard or Gamepad's button
	 */
	public LinkedList<EventListener<InputActionEvent>> actions = new LinkedList<>();
	/**
	 * List of Event in case of an inputs from Gamepad's joysticks
	 */
	public LinkedList<EventListener<InputAxisEvent>> axis = new LinkedList<>();
	
	public void step(double delta) {
		for (Device device : Device.values()) {
			if(!actions.isEmpty()) {
				for (ActionTest action : ActionTest.values()) {
					ActionState state = device.getActionState(action);
					if(state != ActionState.RELEASED) {
						InputActionEvent e = new InputActionEvent(device, action, state);
						actions.forEach(a -> a.action(e));
					}
				}
			}
			if(!axis.isEmpty()) {
				for (AxisTest axi : AxisTest.values()) {
					float f = device.getAxisFloat(axi);
					InputAxisEvent event = new InputAxisEvent(device, axi, f);
					axis.forEach(a -> a.action(event));
				}
			}
		}
	}
}
