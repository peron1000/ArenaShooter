package arenashooter.engine.events;

public interface EventListener<E extends Event> extends java.util.EventListener {
	public void action(E e);
}
