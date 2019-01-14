package arenashooter.game.gameStates;

import arenashooter.entities.Map;

public abstract class GameState {
	protected GameState() {
		// TODO Auto-generated constructor stub
	}
	
	protected Map map;
	public abstract void update(double delta);
	public abstract void draw();
}
