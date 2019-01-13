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

	public enum SpritePath {
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
		sprite.attachToParent(this, "item_Sprite");
		sprite.size = new Vec2f(sprite.tex.getWidth(), sprite.tex.getHeight());

		collider = new Collider(position, new Vec2f(sprite.tex.getWidth(), sprite.tex.getHeight()));
		collider.attachToParent(this, "coll_item");

	}

	@Override
	public void step(double d) {
		if (!isEquipped()) {
			Profiler.startTimer(Profiler.PHYSIC);// Inclu dans la physique

			isOnGround = false;
			for (Entity plat : getParent().children.values()) {
				if (plat instanceof Plateform) {
					for (Entity coll : ((Plateform) plat).children.values()) {
						if (coll instanceof Collider) {
							// Collider c = (Collider) coll;
							// Impact impact = new Impact(collider, c, Vec2f.multiply(vel, (float) d));
							// vel.x = vel.x * impact.getVelMod().x;
							// vel.y = vel.y * impact.getVelMod().y;
							// if (collider.getYBottom() + (vel.y * d) >= c.getYTop()
							// && collider.getYBottom() + (vel.y * d) < c.getYBottom()
							// && Collider.isX1IncluedInX2(collider, c))
							isOnGround = true;
						}
					}
				}
			}
			if (!isOnGround)
				vel.y += 9.807 * 800 * d;

			Profiler.endTimer(Profiler.PHYSIC);
		}

		position.add(Vec2f.multiply(vel, (float) d));

		super.step(d);
	}
}