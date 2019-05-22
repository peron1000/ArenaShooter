package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.entities.spatials.Spatial;
import arenashooter.entities.spatials.Sprite;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.Collider;
import arenashooter.entities.spatials.RigidBodyContainer;
import arenashooter.entities.spatials.SoundEffect;

public abstract class Item extends Spatial {

	protected Vec2f vel = new Vec2f();
	Collider collider;
	public Vec2f handPosL = null;
	public Vec2f handPosR = null;
	protected Vec2f position = null;
	public String name = "";
	protected double weight = 0;
	protected String pathSprite = ""; 
	protected String soundPickup = "";
	
	private RigidBodyContainer rigidBody;
	
	private boolean isEquipped = false;

	public boolean isEquipped() {
		return isEquipped;
	}

	public Item(Vec2f position, String name, double weight, String pathSprite, Vec2f handPosL, Vec2f handPosR,
			String soundPickup) {
		super(position);
		
		rotationFromParent = false;
		
		//Set sprite
		Sprite sprite = new Sprite(position, pathSprite);
		sprite.attachToParent(this, "Item_Sprite");
		
		//Collision
//		collider = new Collider(parentPosition, new Vec2f(sprite.getTexture().getWidth(), sprite.getTexture().getHeight()));
//		collider.attachToParent(this, "coll_item");

		SoundEffect pickup = new SoundEffect(parentPosition, "data/sound/"+soundPickup+".ogg", 1);
		pickup.attachToParent(this, "sound_pickup");

		setSizeOfSprite();

		
		this.position = position;
		this.name = name;
		this.weight = weight;
		this.pathSprite = pathSprite;
		this.handPosL = handPosL;
		this.handPosR = handPosR;
		this.soundPickup = soundPickup;
	}
	
	@Override
	public Entity attachToParent(Entity newParent, String name) {
		Entity prev = super.attachToParent(newParent, name);
		
		isEquipped = parent instanceof Character;
		updateRigidBodyState();
		
		return prev;
	}
	
	@Override
	public void detach() {
		isEquipped = false;
		updateRigidBodyState();
		super.detach();
	}
	
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
		if(rigidBody != null) {
			localPosition.set(Vec2f.subtract(rigidBody.getWorldPos(), parentPosition));
			rotation = rigidBody.getWorldRot();
		}
		
		// SpriteFlip
		if (isEquipped()) {
			setSpriteFlip();
			setLocalPositionOfSprite();
			parentPosition.add(localPosition);
		} else {
			parentPosition.add(Vec2f.multiply(getVel(), (float) d));
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
	 * @return Character if parent is a Character
	 * or NULL if parent is different of a Character
	 */
	protected Character getCharacter() {
		if(getParent() instanceof Character) {
			return (Character) getParent();
		} else {
			return null;
		}
	}
	
	public Item clone(Vec2f position) {
		Item clone = new Item(position, this.genName(), weight, pathSprite, handPosL, handPosL, soundPickup) {
		};
		return clone;
	}
}