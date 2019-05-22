package arenashooter.engine.events.input;

import java.util.LinkedList;

import arenashooter.engine.events.Event;
import arenashooter.engine.events.EventListener;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.input.ActionTest;
import arenashooter.engine.input.AxisTest;
import arenashooter.engine.input.Device;

public class InputListener {
	private LinkedList<EventListener<InputActionEvent>> actions = new LinkedList<>();
	private LinkedList<EventListener<Event>> axis = new LinkedList<>();
	
	public void addAction(EventListener<InputActionEvent> event) {
		actions.add(event);
	}
	
	public void addAxisInput(EventListener<Event> event) {
		axis.add(event);
	}
	
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
