package arenashooter.engine.events.input;

import java.util.LinkedList;
import java.util.TreeMap;

import arenashooter.engine.events.Event;
import arenashooter.engine.events.EventListener;
import arenashooter.engine.input.Action;
import arenashooter.engine.input.ActionState;
import arenashooter.engine.input.Axis;
import arenashooter.engine.input.Device;
import arenashooter.engine.input.Input;

public class InputListener {
	private TreeMap<Action, LinkedList<EventListener<InputActionEvent>>> actions = new TreeMap<>();
	private TreeMap<Axis, LinkedList<EventListener<Event>>> axis = new TreeMap<>();
	
	public void addAction(Action action , EventListener<InputActionEvent> event) {
		if(!actions.containsKey(action)) {
			LinkedList<EventListener<InputActionEvent>> list = new LinkedList<>();
			actions.put(action, list);
		}
		actions.get(action).add(event);
	}
	
	public void addAxisInput(Axis axis , EventListener<Event> event) {
		if(!this.axis.containsKey(axis)) {
			LinkedList<EventListener<Event>> list = new LinkedList<>();
			this.axis.put(axis, list);
		}
		this.axis.get(axis).add(event);
	}
	
	public void step(double delta) {
		for (Device device : Device.values()) {
			for (Action action : actions.keySet()) {
				ActionState state = Input.getActionState(device, action);
				if(state != ActionState.RELEASED) {
					InputActionEvent e = new InputActionEvent(device, action, state);
					actions.get(action).forEach(l -> l.action(e));
				}
			}
			for (Axis axi : axis.keySet()) {
				float f = Input.getAxis(device, axi);
				if(Math.abs(f) > 0.3f) {
					Event e = null;
					axis.get(axi).forEach(l -> l.action(e));
				}
			}
		}
	}
}
