package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.spatials.items.Item.ItemSprite;

public class Equipement extends Item {

	private static int idNumber;

	public Equipement(Vec2f position, ItemSprite itemSprite) {
		super(position , itemSprite);
		tag = "Armure";
	}

	@Override
	public String getId() {
		idNumber++;
		return "Item_Arme"+idNumber;
	}
}
