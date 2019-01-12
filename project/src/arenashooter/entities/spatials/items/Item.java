package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Collider;
import arenashooter.entities.spatials.Spatial;
import arenashooter.entities.spatials.Sprite;
import arenashooter.entities.spatials.Character;

public abstract class Item extends Spatial {

	protected String tag = "Item";
	Vec2f vel = new Vec2f();
	Collider collider;
	boolean isOnGround = true;

	public String getId() {
		return "Item";
	}

	public String getTag() {
		return tag;
	}

	public boolean isEquipped() { // Plutôt que de demander à chaque fois le type du parent;
		if (parent != null)
			return parent instanceof Character;
		else
			return false;

	}

	public enum ItemSprite {
		minugun("data/weapons/Minigun_1.png"), assault("data/weapons/Assaut_1.png"), armor(
				"data/armor/shield_of_Pop.png");
		public String sprite;

		private ItemSprite(String sprite) {
			this.sprite = sprite;
		}
	}


	public Item(Vec2f position, ItemSprite itemSprite) {
		super(position);

		Sprite sprite = new Sprite(position, itemSprite.sprite);
		sprite.attachToParent(this, "item_Sprite");
		sprite.size = new Vec2f(sprite.tex.getWidth(), sprite.tex.getHeight());

		collider = new Collider(position, new Vec2f(sprite.tex.getWidth(), sprite.tex.getHeight()));
		collider.attachToParent(this, "coll_item");

	}

	@Override
	public void step(double d) {
		super.step(d);
	}
}