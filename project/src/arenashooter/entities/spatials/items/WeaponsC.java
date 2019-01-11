package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Vec2f;

public class WeaponsC extends Item{

	public WeaponsC(Vec2f position , ItemSprite itemSprite) {
		super(position , itemSprite);
		tag = "Arme";
	}
}
