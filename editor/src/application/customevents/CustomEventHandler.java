package application.customevents;

import javafx.event.EventHandler;

public abstract class CustomEventHandler implements EventHandler<CustomEvent> {

	public abstract void onEventVec2Change(double newX, double newY);

	@Override
	public void handle(CustomEvent event) {
		event.invokeHandler(this);
	}
}