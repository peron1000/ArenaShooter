package arenashooter.entities.items;

import arenashooter.engine.math.Vec2f;

public class Armor extends Item {

	public Armor(Vec2f position, ItemSprite itemSprite) {
		super(position , itemSprite);
		tag = "Armure";
	}

}
