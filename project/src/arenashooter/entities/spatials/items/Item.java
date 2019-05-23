package arenashooter.entities.spatials.items;

import arenashooter.engine.DamageInfo;
import arenashooter.engine.DamageType;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.entities.spatials.Spatial;
import arenashooter.entities.spatials.Sprite;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.RigidBodyContainer;
import arenashooter.entities.spatials.SoundEffect;

public abstract class Item extends Spatial {

	private Vec2f vel = new Vec2f();
	public Vec2f handPosL = null;
	public Vec2f handPosR = null;
	public String name = "";
	protected double weight = 0;
	protected String pathSprite = ""; 
	protected String soundPickup = "";
	
	private RigidBodyContainer rigidBody;
	
	private boolean isEquipped = false;

	public Item(Vec2f position, String name, double weight, String pathSprite, Vec2f handPosL, Vec2f handPosR,
			String soundPickup) {
		super(position);
		
		rotationFromParent = false;
		
		//Set sprite
		Sprite sprite = new Sprite(position, pathSprite);
		sprite.attachToParent(this, "Item_Sprite");
		
		SoundEffect pickup = new SoundEffect(parentPosition, "data/sound/"+soundPickup+".ogg", 1);
		pickup.attachToParent(this, "sound_pickup");

		setSizeOfSprite();

		this.name = name;
		this.weight = weight;
		this.pathSprite = pathSprite;
		this.handPosL = handPosL;
		this.handPosR = handPosR;
		this.soundPickup = soundPickup;
	}
	

	/**
	 * Attach this item to a new parent and update its eqipped status
	 * @param newParent new parent Entity
	 * @param name      used as a key in parent's children
	 * @return previous child of the new parent using that name
	 */
	@Override
	public Entity attachToParent(Entity newParent, String name) {
		Entity prev = super.attachToParent(newParent, name);
		
		isEquipped = getParent() instanceof Character;
		updateRigidBodyState();
		
		return prev;
	}
	
	/**
	 * Detach this item and update its equipped status
	 */
	@Override
	public void detach() {
		isEquipped = false;
		updateRigidBodyState();
		super.detach();
	}
	
	/**
	 * @return <b>true</b> if this item is currently equipped on a Character
	 */
	public boolean isEquipped() { return isEquipped; }
	
	/**
	 * Apply an impulse depending on damage received.
	 * <br/>Detach if out of bounds
	 */
	@Override
	public float takeDamage(DamageInfo info) { //TODO: Get impact location
		if(rigidBody != null)
			rigidBody.applyImpulse(Vec2f.multiply(info.direction, info.damage));
		
		//Destroy when out of bounds
		if(info.dmgType == DamageType.OUT_OF_BOUNDS) detach();

		return 0;
	}
	
	/**
	 * Create or destroy the rigid body for this item depending on its equipped state
	 */
	private void updateRigidBodyState() {
		if(!isEquipped() && rigidBody == null) {
			RigidBody body = new RigidBody( new ShapeBox(new Vec2f(1, .2)), getWorldPos(), 0, CollisionFlags.ITEM, 1, 1) ;
			body.setRotation((float)getWorldRot());
			rigidBody = new RigidBodyContainer(getWorldPos(), body);
			rigidBody.attachToParent(this, "rigid_body");
			getSprite().rotationFromParent = false;
		} else if(isEquipped() && rigidBody != null) {
			rigidBody.detach();
			rigidBody = null;
			getSprite().rotationFromParent = true;
		}
	}

	@Override
	public void step(double d) {
		//Lock transform to rigid body when simulating
		if(rigidBody != null) {
			localPosition.set(Vec2f.subtract(rigidBody.getWorldPos(), parentPosition));
			rotation = rigidBody.getWorldRot();
		}

		//Destroy when out of bounds
		if (Math.abs(getWorldPos().x) > 500 || Math.abs(getWorldPos().y) > 500)
			takeDamage(new DamageInfo(0, DamageType.OUT_OF_BOUNDS, new Vec2f(), null));
		
		// SpriteFlip
		if (isEquipped()) {
			setSpriteFlip();
			setLocalPositionOfSprite();
			parentPosition.add(localPosition);
		}
		
		super.step(d);
		
		if(rigidBody != null) {
			getSprite().rotation = rigidBody.getBody().getRotation();
		}
	}

	public Vec2f getVel() {
		return vel;
	}

	public void setVel(Vec2f vel) {
		vel.set(vel);
	}

	/**
	 * @return Sprite of item
	 */
	public Sprite getSprite() {
		return (Sprite) getChild("Item_Sprite");
	}

	/** Set the size of the Sprite from its texture size **/
	protected void setSizeOfSprite() {
		Sprite sprite = getSprite();
		sprite.size = new Vec2f(sprite.getTexture().getWidth()*.035, sprite.getTexture().getHeight()*.035);
		sprite.getTexture().setFilter(false);
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
	
	/**
	 * @return Character if parent is a Character (equipped) 
	 * or NULL if parent is not a Character (not equipped)
	 */
	protected Character getCharacter() {
		if(isEquipped())
			return (Character) getParent();
		else
			return null;
	}
	
	/**
	 * @param position
	 * @return a copy of this item at <i>position</i>
	 */
	public Item clone(Vec2f position) {
		Item clone = new Item(position, this.genName(), weight, pathSprite, handPosL, handPosL, soundPickup) {};
		return clone;
	}
}