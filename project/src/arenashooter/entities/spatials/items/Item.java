package arenashooter.entities.spatials.items;

import arenashooter.engine.Profiler;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Collider;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.Spatial;
import arenashooter.entities.spatials.Sprite;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.Plateform;

public abstract class Item extends Spatial {

	private Vec2f vel = new Vec2f();
	Collider collider;
	boolean isOnGround = true;

	public String getId() {
		return "Item";
	}

	public boolean isEquipped() { // Plutôt que de demander à chaque fois le type du parent;
		if (parent != null)
			return parent instanceof Character;
		else
			return false;

	}

	public enum SpritePath { //TODO: Remove this and load items from XML
		minigun("data/weapons/Minigun_1.png"), assault("data/weapons/Assaut_1.png"), armor(
				"data/armor/shield_of_Pop.png");
		public String path;

		private SpritePath(String path) {
			this.path = path;
		}
	}

	public Item(Vec2f position, SpritePath itemSprite) {
		super(position);

		Sprite sprite = new Sprite(position, itemSprite.path);
		sprite.attachToParent(this, "Item_Sprite");
		sprite.size = new Vec2f(sprite.tex.getWidth(), sprite.tex.getHeight());

		collider = new Collider(position, new Vec2f(sprite.tex.getWidth(), sprite.tex.getHeight()));
		collider.attachToParent(this, "coll_item");

	}

	@Override
	public void step(double d) {
		position.add(Vec2f.multiply(getVel(), (float) d));
		super.step(d);
	}

	public Vec2f getVel() {
		return vel;
	}

	public void setVel(Vec2f vel) {
		this.vel = vel;
	}
}