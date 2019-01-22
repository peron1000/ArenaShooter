package arenashooter.game.gameStates;

import arenashooter.entities.Map;

public abstract class GameState {
	public GameState() {
		map = new Map();
	}
	
	protected Map map;
	public abstract void update(double delta);
	public void draw() {
		map.draw();
	}
	public Map getMap() {
		return map;
	}
	public void init() {
		
	}
}
