package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Vec2f;

public class Melee extends Weapon {

	private static int idNumber;

	public Melee(Vec2f position, SpritePath itemSprite) {
		super(position, itemSprite);
	}
	
	@Override
	public void attack() {
		
	}
}
