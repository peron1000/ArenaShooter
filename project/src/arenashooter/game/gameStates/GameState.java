package arenashooter.game.gameStates;

import arenashooter.engine.graphics.Window;
import arenashooter.entities.Map;

public abstract class GameState {
	protected Map map;
	
	public GameState() {
		map = new Map();
	}
	
	public void init() { }
	
	public abstract void update(double delta);
	
	public void draw() {
		map.drawSelfAndChildren();
		Window.endTransparency(); //Make sure to end transparency
	}
	
	public Map getMap() {
		return map;
	}
}
