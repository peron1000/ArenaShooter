package application.customevents;

import javafx.event.EventType;

public class Vec2ChangeEvent extends CustomEvent {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4593452023148155377L;

	public static final EventType<CustomEvent> CUSTOM_EVENT_TYPE_1 = new EventType<CustomEvent>(CUSTOM_EVENT_TYPE, "Vec2Change");

    private final double newX, newY;

    public Vec2ChangeEvent(double newX, double newY) {
        super(CUSTOM_EVENT_TYPE_1);
        this.newX = newX;
        this.newY = newY;
    }

    @Override
    public void invokeHandler(CustomEventHandler handler) {
        handler.onEventVec2Change(newX, newY);
    }

}