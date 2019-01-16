package gamedata.entities;

import math.Vec2;

public class Spatial extends Entity {

	public Vec2 position;
	public double rotation;
	
	public Spatial(Vec2 position, double rotation) {
		this.position = position.clone();
		this.rotation = rotation;
	}
	
	@Override
	public String getIcon() {
		return "file:editor_data/icons/spatial.png";
	}

}
