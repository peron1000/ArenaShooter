package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Sprite;

public abstract class Weapon extends Item {

	public Vec2f handPosL = null;
	public Vec2f handPosR = null;
	private float damage = 0f;
	private Timer attackSpeed = new Timer(0.5);
	

	public Weapon(Vec2f position, String itemSprite, double damage) {
		super(position, itemSprite);
	}

	public abstract void attackStart();

	public abstract void attackStop();

	public void step(double d) {
		super.step(d);
	}
}
