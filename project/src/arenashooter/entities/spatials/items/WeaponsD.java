package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Vec2f;

public class WeaponsD extends Item {

	private static int idNumber;

	public WeaponsD(Vec2f position, ItemSprite itemSprite) {
		super(position, itemSprite);
		tag = "Arme";
	}

	@Override
	public String getId() {
		idNumber++;
		return "Item_Arme" + idNumber;
	}
}
