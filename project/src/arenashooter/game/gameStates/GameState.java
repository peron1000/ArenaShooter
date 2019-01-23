package arenashooter.game.gameStates;

import java.util.LinkedList;

import arenashooter.engine.graphics.Window;
import arenashooter.entities.Entity;
import arenashooter.entities.Map;

public abstract class GameState {
	protected Map map;
	
	public GameState() {
		map = new Map();
	}
	
	public void init() { }
	
	public abstract void update(double delta);
	
	public void draw() {
		//Transparent entities
		LinkedList<Entity> transparent = new LinkedList<>();
		//Opaque pass
		map.drawOpaque(transparent);
		
		//Transparency pass
		Window.beginTransparency();
		for(Entity e : transparent)
			e.draw();
		Window.endTransparency();
	}
	
	public Map getMap() {
		return map;
	}
}
