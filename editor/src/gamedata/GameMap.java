package gamedata;

import java.util.HashMap;

import gamedata.entities.Entity;
import math.Vec2;

public class GameMap {

	public Vec2 gravity = new Vec2(0, 9.807);
	
	public HashMap<String, Entity> children = new HashMap<String, Entity>();
	
	public GameMap() {
		// TODO Auto-generated constructor stub
	}

}
