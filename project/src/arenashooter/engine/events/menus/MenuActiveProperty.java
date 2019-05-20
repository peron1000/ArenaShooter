package arenashooter.engine.events.menus;

import java.util.LinkedList;
import java.util.Observable;

import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.NewValueEvent;

public class MenuActiveProperty extends Observable {
	private boolean value = true;
	public LinkedList<EventListener<NewValueEvent<Boolean>>> listener = new LinkedList<>();
	
	public void setValue(boolean newValue) {
		value = newValue;
		for (EventListener<NewValueEvent<Boolean>> event : listener) {
			event.action(new NewValueEvent<>(!newValue, newValue));
		}
	}
	
	public boolean getValue() {
		return value;
	}
}
