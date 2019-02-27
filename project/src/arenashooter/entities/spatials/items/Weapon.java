package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Timer;

public abstract class Weapon extends Item {

	public Vec2f handPosL = null;
	public Vec2f handPosR = null;
	protected float damage = 0f;
	protected Timer attackSpeed = new Timer(0.05);
	

	public Weapon(Vec2f position, String itemSprite, float damage) {
		super(position, itemSprite);
		this.damage = damage;
		attackSpeed.attachToParent(this, attackSpeed.genName());
	}

	public abstract void attackStart();

	public abstract void attackStop();

	public void step(double d) {
		super.step(d);
	}
}
