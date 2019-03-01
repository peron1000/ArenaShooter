package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Timer;

public abstract class Weapon extends Item {

	public Vec2f handPosL = null;
	public Vec2f handPosR = null;
	protected float damage = 0f;
	protected Timer timerAttack = new Timer(0.15);
	

	public Weapon(Vec2f position, String itemSprite, float damage) {
		super(position, itemSprite);
		this.damage = damage;
		timerAttack.attachToParent(this, timerAttack.genName());
	}

	public void attackStart() {
		
	}

	public void attackStop() {
		
	}

	public void step(double d) {
		super.step(d);
	}
}
