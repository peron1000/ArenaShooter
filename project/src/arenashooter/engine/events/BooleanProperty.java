package arenashooter.engine.events;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

/**
 * @author Nathan
 * Boolean value that launch an event NewValueEvent in case of a value changement
 */
public class BooleanProperty extends Observable {
	private boolean value = true;
	public List<EventListener<NewValueEvent<Boolean>>> listener = new LinkedList<>();
	
	public BooleanProperty() {
	}
	
	public BooleanProperty(boolean start) {
		value = start;
	}
	
	/**
	 * @param newValue
	 * Change the value of the property and launch the events associated
	 */
	public void setValue(boolean newValue) {
		value = newValue;
		for (EventListener<NewValueEvent<Boolean>> event : listener) {
			event.launch(new NewValueEvent<>(!newValue, newValue));
		}
	}
	
	public boolean getValue() {
		return value;
	}
}
