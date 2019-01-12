package gamedata;

import java.util.HashMap;

import application.propertiestabs.MapProperties;
import gamedata.entities.Entity;
import math.Vec2;

public class GameMap {

	public Vec2 gravity = new Vec2(0, 9.807);
	
	public HashMap<String, Entity> children = new HashMap<String, Entity>();
	
	public MapProperties propertiesTab;
	
	public GameMap() {
		propertiesTab = new MapProperties(this);
	}

}
