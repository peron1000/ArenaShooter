package arenashooter.engine.events;

public class NewValueEvent<T> extends Event {
	
	private T oldValue , newValue;

	public NewValueEvent(T oldValue ,T newValue) {
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public T getOldValue() {
		return oldValue;
	}

	public T getNewValue() {
		return newValue;
	}
	
}
