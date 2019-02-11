package arenashooter.engine.itemCollection;

import arenashooter.engine.math.Vec2f;

public class ItemConcept {
	public String spritePath;
	private String type;
	private double proba;
	private Vec2f colliderExtent;
	public ItemConcept(String type , Vec2f colliderExtent, double proba) {
		this.proba = proba;
		this.type = type;
	}
	
	public double getProba() {
		return proba;
	}
	
	public String getType() {
		return type;
	}
	
	public Vec2f getColliderExtent () {
		return colliderExtent;
	}
}
