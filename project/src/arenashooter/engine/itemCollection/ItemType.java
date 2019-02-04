package arenashooter.engine.itemCollection;

import arenashooter.engine.math.Vec2f;

public class ItemType {
	private String spritePath;
	private String type;
	private Vec2f collider;
	private double proba;
	public ItemType(String spritePath , String type , Vec2f colliderExtent , double proba) {
		this.collider = colliderExtent;
		this.proba = proba;
		this.spritePath = spritePath;
		this.type = type;
	}
	
	public double getProba() {
		return proba;
	}
	
	public String getType() {
		return type;
	}
}
