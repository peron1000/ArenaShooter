package arenashooter.entities.items;

import arenashooter.engine.math.Vec2f;

public class Armor extends Item {

	private static int idNumber;

	public Armor(Vec2f position, ItemSprite itemSprite) {
		super(position , itemSprite);
		tag = "Armure";
	}

	@Override
	public String getId() {
		idNumber++;
		return "Item_Arme"+idNumber;
	}
}
