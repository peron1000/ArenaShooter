package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Collider;
import arenashooter.entities.SoundEffect;
import arenashooter.entities.spatials.Spatial;
import arenashooter.entities.spatials.Sprite;
import arenashooter.entities.spatials.Character;

public abstract class Item extends Spatial {

	private Vec2f vel = new Vec2f();
	Collider collider;
	public SoundEffect pickup = new SoundEffect(position, "data/sound/GunCock1.ogg", 2);

	public boolean isEquipped() {
		if (parent != null)
			return parent instanceof Character;
		else
			return false;
	}

	public Item(Vec2f position, String pathSprite) {
		super(position);

		Sprite sprite = new Sprite(position, pathSprite);
		sprite.attachToParent(this, "Item_Sprite");
		
		pickup.attachToParent(this, pickup.genName());

		setSizeOfSprite();
		setSizeOfCollider();
		
	}

	@Override
	public void step(double d) {
		// SpriteFlip
		if (isEquipped()) {
			setRotationOfSprite();
			setLocalPositionOfSprite();
			position.add(localPosition);
		} else {
			position.add(Vec2f.multiply(getVel(), (float) d));
		}
		super.step(d);
	}

	public Vec2f getVel() {
		return vel;
	}

	public void setVel(Vec2f vel) {
		this.vel = vel;
	}

	/**
	 * @return Sprite of item
	 */
	public Sprite getSprite() {

		return (Sprite) children.get("Item_Sprite");
	}

	/** Set the size of the Sprite by default **/
	protected void setSizeOfSprite() {
		Sprite sprite = getSprite();
		sprite.size = new Vec2f(sprite.tex.getWidth(), sprite.tex.getHeight());
		sprite.tex.setFilter(false);
	}

	/** Set the size of the collider by default **/
	protected void setSizeOfCollider() {
		Sprite sprite = getSprite();
		collider = new Collider(position, new Vec2f(sprite.tex.getWidth(), sprite.tex.getHeight()));
		collider.attachToParent(this, "coll_item");
	}

	/**
	 * Set the rotation of the item Sprite
	 */
	protected void setRotationOfSprite() {
		Character c = (Character) getParent();
		getSprite().flipX = !c.lookRight;
	}

	/**
	 * Set the local Position of the item Sprite
	 */
	protected void setLocalPositionOfSprite() {
		Character c = (Character) getParent();
		if (c.lookRight) {
			localPosition = new Vec2f(20, 0);
		} else {
			localPosition = new Vec2f(-20, 0);
		}
	}
}