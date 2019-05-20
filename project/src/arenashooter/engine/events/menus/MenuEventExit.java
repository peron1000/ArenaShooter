package arenashooter.engine.events.menus;

import arenashooter.engine.events.Event;

public class MenuEventExit extends Event {
	
	public enum Side {
		Right, Left , Up , Down;
	}
	
	private Side side;

	public MenuEventExit(Side side) {
		this.side = side;
	}
	
	public Side getSide() {
		return side;
	}

}
