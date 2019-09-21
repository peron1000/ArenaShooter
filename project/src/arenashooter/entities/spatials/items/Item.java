package arenashooter.entities.spatials.items;

import java.util.Set;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.DamageInfo;
import arenashooter.engine.DamageType;
import arenashooter.engine.json.StrongJsonKey;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.entities.spatials.Spatial;
import arenashooter.entities.spatials.Sprite;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.RigidBodyContainer;

public abstract class Item extends Spatial {

	private Vec2f vel = new Vec2f();
	public Vec2f handPosL = null;
	public Vec2f handPosR = null;
	public Vec2f extent = new Vec2f(1 , .2);
	public String name = "";
	protected double weight = 0;
	protected String pathSprite = "";
	public String soundPickup = "";

	private RigidBodyContainer rigidBody;
	public boolean canDealDamage;

	private boolean isEquipped = false;
	private Sprite sprite;
	
	public Item(Vec2f localPosition, String name, double weight, String pathSprite, Vec2f handPosL, Vec2f handPosR,
			Vec2f extent, String soundPickup) {
		super(localPosition);

		attachRot = false;
		canDealDamage = false;

		// Set sprite
		sprite = new Sprite(new Vec2f(), pathSprite);
		sprite.attachToParent(this, "Item_Sprite");

		setSizeOfSprite();

		this.name = name;
		this.setWeight(weight);
		this.pathSprite = pathSprite;
		this.handPosL = handPosL;
		this.handPosR = handPosR;
		this.soundPickup = soundPickup;
		this.extent = extent;
	}

	/**
	 * Constructor for the Editor to avoid a new Item creation for each change state
	 * 
	 * @author Nathan
	 * @param sprite
	 */
	public Item(String sprite) {
		this(new Vec2f(), "", 1, sprite, new Vec2f(), new Vec2f(), new Vec2f(1, .2), "GunCock_01");
	}

	public void setSprite(String sprite) {
		this.sprite = new Sprite(new Vec2f(), sprite);
		this.sprite.attachToParent(this, "Item_Sprite");
		pathSprite = sprite;
		setSizeOfSprite();
	}

	/**
	 * Attach this item to a new parent and update its equipped status
	 * 
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
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	/**
	 * @return the pathSprite
	 */
	public String getPathSprite() {
		return pathSprite;
	}

	/**
	 * @return <b>true</b> if this item is currently equipped on a Character
	 */
	public boolean isEquipped() {
		return isEquipped;
	}

	/**
	 * Apply an impulse depending on damage received. <br/>
	 * Detach if out of bounds
	 */
	@Override
	public float takeDamage(DamageInfo info) { // TODO: Get impact location
		if (rigidBody != null)
			rigidBody.applyImpulse(Vec2f.multiply(info.direction, info.impulse));

		// Destroy when out of bounds
		if (info.dmgType == DamageType.OUT_OF_BOUNDS)
			detach();

		return 0;
	}

	/**
	 * Create or destroy the rigid body for this item depending on its equipped
	 * state
	 */
	private void updateRigidBodyState() {
		if (!isEquipped() && rigidBody == null) {
			RigidBody body = new RigidBody(new ShapeBox(extent), getWorldPos(), 0, CollisionFlags.ITEM, 1, 1);
			body.setRotation((float) getWorldRot());
			rigidBody = new RigidBodyContainer(body);
			rigidBody.attachToParent(this, "rigid_body");
		} else if (isEquipped() && rigidBody != null) {
			rigidBody.detach();
			rigidBody = null;
		}
	}

	@Override
	public void step(double d) {
		// Lock transform to rigid body when simulating
		if (rigidBody != null) {
			localPosition.set(Vec2f.subtract(rigidBody.getWorldPos(), parentPosition));
			localRotation = rigidBody.getWorldRot();
		}

		// Destroy when out of bounds
		if (getArena() != null && (getWorldPos().x < getArena().killBound.x || getWorldPos().x > getArena().killBound.z
				|| getWorldPos().y < getArena().killBound.y || getWorldPos().y > getArena().killBound.w))
			takeDamage(new DamageInfo(0, DamageType.OUT_OF_BOUNDS, new Vec2f(), 0, null));

		// SpriteFlip
		if (isEquipped()) {
			setSpriteFlip();
			setLocalPositionOfSprite();
		}

		super.step(d);

		// if(rigidBody != null) {
		// getSprite().localRotation = rigidBody.getBody().getRotation();
		// }
	}

	public Vec2f getVel() {
		return vel;
	}

	public void setVel(Vec2f vel) {
		this.vel.set(vel);
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
		sprite.size = new Vec2f(sprite.getTexture().getWidth() * .035, sprite.getTexture().getHeight() * .035);
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
	 * Set the local Position of the item Sprite when equipped
	 */
	protected void setLocalPositionOfSprite() {
		if (getCharacter() != null) {
			if (getCharacter().lookRight) {
				localPosition = new Vec2f(1, 0);
			} else {
				localPosition = new Vec2f(-1, 0);
			}
		}
	}

	/**
	 * @return Character if parent is a Character (equipped) or NULL if parent is
	 *         not a Character (not equipped)
	 */
	protected Character getCharacter() {
		if (isEquipped())
			return (Character) getParent();
		else
			return null;
	}

	/**
	 * @param position
	 * @return a copy of this item at <i>position</i>
	 */
	@Override
	public Item clone() {
		Item clone = new Item(localPosition, this.genName(), weight, pathSprite, handPosL, handPosR, extent, soundPickup) {
		};
		return clone;
	}

	
	/*
	 * JSON
	 */
	
	@Override
	public Set<StrongJsonKey> getJsonKey() {
		Set<StrongJsonKey> set = super.getJsonKey();

		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				return name;
			}
			@Override
			public String getKey() {
				return "name";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				name = json.getString(this);
			}
		});
		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				return weight;
			}
			@Override
			public String getKey() {
				return "weight";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				weight = json.getFloat(this);
			}
		});
		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				return pathSprite;
			}
			@Override
			public String getKey() {
				return "pathSprite";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				pathSprite = json.getString(this);
			}
		});
		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				return soundPickup;
			}
			@Override
			public String getKey() {
				return "sound pickup";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				soundPickup = json.getString(this);
			}
		});
		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				if(handPosL == null)
					return new int[0];
				else
					return handPosL;
			}
			@Override
			public String getKey() {
				return "handPosL";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				JsonArray a = json.getCollection(this);
				if( a.size() == 2 )
					handPosL = Vec2f.jsonImport(a);
				else
					handPosL = null;
			}
		});
		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				if(handPosR == null)
					return new int[0];
				else
					return handPosR;
			}
			@Override
			public String getKey() {
				return "handPosR";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				JsonArray a = json.getCollection(this);
				if( a.size() == 2 )
					handPosR = Vec2f.jsonImport(a);
				else
					handPosR = null;
			}
		});
		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				return extent;
			}
			@Override
			public String getKey() {
				return "extent";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				JsonArray a = json.getCollection(this);
				if (a != null)
					extent.set(Vec2f.jsonImport(a));
			}
		});
		return set;
	}
	
}