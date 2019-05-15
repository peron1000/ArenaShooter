package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.spatials.Spatial;
import arenashooter.entities.spatials.Sprite;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.Collider;
import arenashooter.entities.spatials.SoundEffect;

public abstract class Item extends Spatial {

	protected Vec2f vel = new Vec2f();
	Collider collider;
	public Vec2f handPosL = null;
	public Vec2f handPosR = null;
	protected Vec2f position = null;
	protected String name = "";
	protected double weight = 0;
	protected String pathSprite = ""; 
	protected String soundPickup = ""; 
	public SoundEffect pickup = new SoundEffect(parentPosition, "data/sound/GunCock1.ogg", 2);

	public boolean isEquipped() {
		if (parent != null)
			return parent instanceof Character;
		else
			return false;
	}

	public Item(Vec2f position, String name, double weight, String pathSprite, Vec2f handPosL, Vec2f handPosR,
			String soundPickup) {
		super(position);
		
		Sprite sprite = new Sprite(position, pathSprite);
		sprite.attachToParent(this, "Item_Sprite");

		pickup.attachToParent(this, pickup.genName());

		setSizeOfSprite();
		setSizeOfCollider();
		
		this.position = position;
		this.name = name;
		this.weight = weight;
		this.pathSprite = pathSprite;
		this.handPosL = handPosL;
		this.handPosR = handPosR;
		this.soundPickup = soundPickup;
	}

	@Override
	public void step(double d) {
		// SpriteFlip
		if (isEquipped()) {
			setSpriteFlip();
			setLocalPositionOfSprite();
			parentPosition.add(localPosition);
		} else {
			parentPosition.add(Vec2f.multiply(getVel(), (float) d));
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

		return (Sprite) getChildren().get("Item_Sprite");
	}

	/** Set the size of the Sprite by default **/
	protected void setSizeOfSprite() {
		Sprite sprite = getSprite();
		sprite.size = new Vec2f(sprite.tex.getWidth()*2, sprite.tex.getHeight()*2);
		sprite.tex.setFilter(false);
	}

	/** Set the size of the collider by default **/
	protected void setSizeOfCollider() {
		Sprite sprite = getSprite();
		collider = new Collider(parentPosition, new Vec2f(sprite.tex.getWidth(), sprite.tex.getHeight()));
		collider.attachToParent(this, "coll_item");
	}

	/**
	 * Set the rotation of the item Sprite
	 */
	protected void setSpriteFlip() {
		if (getParent() instanceof Character)
			getSprite().flipY = !((Character) getParent()).lookRight;
	}

	/**
	 * Set the local Position of the item Sprite
	 */
	protected void setLocalPositionOfSprite() {
		if (getParent() instanceof Character) {
			if (((Character) getParent()).lookRight) {
				localPosition = new Vec2f(20, 0);
			} else {
				localPosition = new Vec2f(-20, 0);
			}
		}
	}
	
	public Item clone() {
		Item clone = new Item(position, this.genName(), weight, pathSprite, handPosL, handPosL, soundPickup) {
		};
		return clone;
	}
}