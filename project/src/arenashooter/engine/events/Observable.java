package arenashooter.engine.events;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Nathan
 *
 * @param <T> Object that launch its list of listener if its value change
 */
public class Observable<T> extends java.util.Observable {
	private T value;
	/**
	 * The list of listener that are launch in case of a value changement
	 */
	public List<EventListener<NewValueEvent<T>>> listener = new LinkedList<>();

	public Observable(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		for (EventListener<NewValueEvent<T>> eventListener : listener) {
			eventListener.launch(new NewValueEvent<>(this.value, value));
		}
		this.value = value;
	}
}
