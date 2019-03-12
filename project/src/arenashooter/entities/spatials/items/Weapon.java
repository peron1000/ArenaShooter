package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Timer;

public abstract class Weapon extends Item {

	public Vec2f handPosL = null;
	public Vec2f handPosR = null;
	protected float damage = 0f;
	protected Timer timerAttack = new Timer(0.15);
	protected boolean charged = timerAttack.isOver();

	public Weapon(Vec2f position, String itemSprite, float damage) {
		super(position, itemSprite);
		this.damage = damage;
		timerAttack.attachToParent(this, timerAttack.genName());
	}

	public void attackStart() {
		timerAttack.setIncreasing(true);
		timerAttack.setProcessing(true);
	}

	public void attackStop() {
		timerAttack.setIncreasing(true);
	}

	public void step(double d) {
		if(charged) {
			
		}
		super.step(d);
	}
}
