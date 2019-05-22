package arenashooter.engine.events.menus;

import arenashooter.engine.events.Event;

/**
 * @author Nathan
 * Event that can give the side of the ExitEvent
 */
public class MenuExitEvent extends Event {
	
	public static enum Side {
		Right, Left , Up , Down;
	}
	
	private Side side;

	public MenuExitEvent(Side side) {
		this.side = side;
	}
	
	public Side getSide() {
		return side;
	}

}
