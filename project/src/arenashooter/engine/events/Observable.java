package arenashooter.engine.events;

import java.util.LinkedList;

public class Observable<T> extends java.util.Observable {
	private T value;
	public LinkedList<EventListener<BasicEvent>> listener = new LinkedList<>();
	
	public Observable(T value) {
		this.value = value;
	}
	
	public T getValue() {
		return value;
	}
	
	public void setValue(T value) {
		for (EventListener<BasicEvent> eventListener : listener) {
			eventListener.action(new BasicEvent("new value : "+this.value+" -> "+value));
		}
		this.value = value;
	}
}
