package arenashooter.engine.events;

public interface EventListener<E extends Event> extends java.util.EventListener {
	/**
	 * @param event A package with informations related to the Event
	 * The action to do
	 */
	public void launch(E event);
}
