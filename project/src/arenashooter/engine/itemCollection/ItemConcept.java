package arenashooter.engine.itemCollection;

import arenashooter.engine.math.Vec2f;

public class ItemConcept {
	// public
	public String spritePath = "data/weapons/Assaut_1.png";
	public String name;
	public float damage = 25f;
	public String bangSound = "Bang1";
	public String pickupSound = "GunCock1";
	public String chargeSound = "GunCock1";
	public String noAmmoSound = "jump";
	public double fireRate = 0.10;
	public int bulletType = 0;
	public float bulletSpeed = 4000;
	public double cannonLength = 40.0;
	public double recoil = 0.5f;
	public double thrust = 500;
	public double tpsCharge = 0;
	
	// private
	private String type;
	private double proba;
	private Vec2f colliderExtent;
	private double size;
	private boolean transparency;
	public ItemConcept(String type, double proba , Vec2f colliderExtent, double size , boolean transparency) {
		this.proba = proba;
		this.type = type;
		this.size = size;
		this.transparency = transparency;
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
	
	public double getSize() {
		return size;
	}
	
	public boolean getTransparency() {
		return transparency;
	}
}
