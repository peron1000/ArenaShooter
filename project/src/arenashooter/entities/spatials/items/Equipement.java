package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.spatials.items.Item.SpritePath;

public class Equipement extends Item {

	private static int idNumber;

	public Equipement(Vec2f position, SpritePath itemSprite) {
		super(position , itemSprite);
		tag = "Armure";
	}

	@Override
	public String getId() {
		idNumber++;
		return "Item_Arme"+idNumber;
	}
}
