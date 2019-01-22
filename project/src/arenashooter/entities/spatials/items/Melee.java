package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Collider;
import arenashooter.entities.Timer;

public class Melee extends Weapon {

	private Timer fire = new Timer(0.15);
	Collider coll;

	public Melee(Vec2f position, SpritePath itemSprite) {
		super(position, itemSprite);
	}
	
	@Override
	public void attack() {
		
	}
}
