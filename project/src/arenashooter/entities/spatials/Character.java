package arenashooter.entities.spatials;

import java.util.HashMap;

import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

import arenashooter.engine.DamageInfo;
import arenashooter.engine.DamageType;
import arenashooter.engine.audio.Audio;
import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.ShapeCharacter;
import arenashooter.entities.Entity;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.items.Item;
import arenashooter.entities.spatials.items.Usable;
import arenashooter.game.CharacterInfo;
import arenashooter.game.Controller;

public class Character extends RigidBodyContainer {

	public Controller controller = null;
	private static final float defaultDamage = 10;
	private float health, healthMax;
	private final Vec2f spawn;

	// public Vec2f vel = new Vec2f();
	boolean isOnGround = true;
	public float movementInput = 0;
	public boolean lookRight = true;
	public boolean isAiming = false;
	public double aimInput = 0;

	// Movement stats
	public double maxSpeed = 18;
	/**
	 * The Character is jumping
	 */
	public boolean jumpi;
	private double jumpForce = 16;
	private double parachuteForce = 8.5;
	private Timer jumpTimer = new Timer(0.5);

	// Combat stats
	/** Melee attack cooldown */
	private boolean stunned = false;
	private Timer stun = null;
	private Timer attackCooldown = new Timer(0.2);
	private int attackCombo = 0;
	private Timer parry = new Timer(1.5);
	private boolean parryCooldown = false;
	private Timer chargePunch = new Timer(0.5);
	/**
	 * For now, this boolean is used for visuals and Sounds only.
	 */
	boolean charged = false;
	private Timer holdCombo = new Timer(1);
	private float range = 2.5f;
	private float hitWidth = 120;
	/**
	 * 
	 * The Character has already punched mid-air
	 */
	boolean punchi = false;

	public Character(Vec2f position, CharacterInfo charInfo) {
		super(new RigidBody(new ShapeCharacter(), position, 0, CollisionFlags.CHARACTER, .5f, 1.2f));

		getBody().setBullet(true);
		getBody().setRotationLocked(true);

		healthMax = 50;
		health = healthMax;
		spawn = position;

		localRotation = 0;

		attackCooldown.attachToParent(this, "attack timer");
		chargePunch.attachToParent(this, "powerpunch charging");
		chargePunch.setProcessing(false);
		jumpTimer.attachToParent(this, "jump Timer");
		holdCombo.attachToParent(this, "attack Combo Hold");
		parry.attachToParent(this, "parry timer");
		parry.setIncreasing(false);

		CharacterSprite skeleton = new CharacterSprite(charInfo);
		skeleton.attachToParent(this, "skeleton");
	}

	public void jump() {
		if (isOnGround) {
			isOnGround = false;
			jumpi = true;
			// vel.y = (float) -jumpForce;
			Vec2f newVel = getLinearVelocity();
			newVel.y = 0;
			setLinearVelocity(newVel);
			getBody().applyImpulse(new Vec2f(0, -jumpForce));
			jumpTimer.reset();
			jumpTimer.setProcessing(true);
			Audio.playSound2D("data/sound/jump.ogg", AudioChannel.SFX, .7f, Utils.lerpF(.9f, 1.2f, Math.random()),
					getWorldPos());
		}
	}

	public void planer() {
		if (!isOnGround) {
			if (!jumpTimer.isOver() && jumpTimer.inProcess) {
				if (getLinearVelocity().y < 0 && jumpi) {
					// vel.y += (float) (-parachuteForce * Math.expm1(1 -
					// (jumpTimer.getValueRatio())));
					getBody().applyForce(new Vec2f(0, -parachuteForce * Math.expm1(1 - (jumpTimer.getValueRatio()))));
					isOnGround = false;
				}
			} else {
				jumpi = false;
			}
		} else {
			jump();
		}
	}

	public void jumpStop() {
		jumpi = false;
		if (getLinearVelocity().y < 0) {
			getBody().setLinearVelocity(new Vec2f(getLinearVelocity().x, getLinearVelocity().y / 2));
			// vel.y = vel.y / 2;
		}
	}

	public void parryStart() {
		if (getWeapon() == null && !stunned) {
			((CharacterSprite) getChild("skeleton")).parryStart(parryCooldown);
			parry.setIncreasing(true);
		}
	}

	public void parryStop() {
		((CharacterSprite) getChild("skeleton")).parryStop();
		parry.setIncreasing(false);
	}

	/**
	 * 
	 * @param attackAngle
	 *            is the angle the attack is directed. For projectiles, relative
	 *            speed is not taken into account, only absolute speed.
	 * @return true if the character is blocking in the direction of the attack.
	 */
	public boolean canParryThis(double attackAngle) {
		return parry.isIncreasing()&&!parryCooldown;// L'angle n'est pas encore géré
	}

	public boolean isParrying() {
		return parry.isIncreasing() && !parry.isOver();
	}

	public void stun(double stunTime) {
		if (stunned) {
			stun.setMax(stun.getMax() + stunTime);
		} else {
			stunned = true;
			stun = new Timer(stunTime);
			charged = false;
			chargePunch.reset();
		}
		if (getWeapon() != null)
			getWeapon().attackStop();
		CharacterSprite skeleton = ((CharacterSprite) getChild("skeleton"));
		skeleton.stunStart(stunTime);
	}

	public void attackStart() {
		if (stunned)
			return;
		if (getWeapon() != null) {
			getWeapon().attackStart();
		} else if (attackCooldown.isOver()) {
			chargePunch.setProcessing(true);
		}
	}

	public void attackStop() {
		if (stunned)
			return;
		if (getWeapon() != null) {
			getWeapon().attackStop();
		} else if (chargePunch.isProcessing()) {

			boolean superPoing = chargePunch.isOver();
			chargePunch.reset();
			Vec2f impulse;
			CharacterSprite skeleton = ((CharacterSprite) getChild("skeleton"));
			DamageInfo punchDmgInfo;

			if (superPoing) {
				impulse = Vec2f.rotate(new Vec2f((!punchi ? 16 : 8), 0), aimInput);
				punchDmgInfo = new DamageInfo(defaultDamage * 2, DamageType.MELEE, Vec2f.fromAngle(aimInput), this);
				skeleton.punch(-1, aimInput);
			} else {
				impulse = Vec2f.rotate(new Vec2f((!punchi ? 25 : 12), 0), aimInput);
				punchDmgInfo = new DamageInfo(defaultDamage, DamageType.MELEE, Vec2f.fromAngle(aimInput), this);
				attackCombo++;
				if (skeleton != null)
					switch (attackCombo) {
					case 1:
						skeleton.punch(1, aimInput);
						break;
					case 2:
						skeleton.punch(2, aimInput);
						break;
					case 3:
						skeleton.punch(3, aimInput);
						break;
					default:
						break;
					}
			}

			if (impulse.y < 0)
				impulse.y /= 4;
			getBody().applyImpulse(impulse);
			punchi = true;
			attackCooldown.restart();
			holdCombo.restart();
			skeleton.stopCharge();

			// Vec2f punchEnd = Vec2f.fromAngle(aimInput);
			// punchEnd.multiply(range);
			// punchEnd.add(getWorldPos());

			Punch ponch = new Punch(punchDmgInfo, hitWidth, range, superPoing);
			ponch.attachToParent(this, "punch_" + genName());

			// punchHit.clear();
			// punchRayFraction = 0;
			//
			// getArena().physic.getB2World().raycast(PunchRaycastCallback,
			// getWorldPos().toB2Vec(), punchEnd.toB2Vec());
			// for (Entry<Spatial, Float> entry : punchHit.entrySet()) {
			// if (entry.getValue() <= punchRayFraction) {
			// entry.getKey().takeDamage(punchDmgInfo);
			// }
			// }

			if (attackCombo >= 3) {
				attackCombo = 0;
			}
		}
	}

	public void getItem() {
		chargePunch.reset();
		holdCombo.restart();
		if (getChild("skeleton") != null) {
			((CharacterSprite) getChild("skeleton")).stopCharge();
		}

		Item arme = null;

		boolean hasWeapon = getWeapon() != null;

		if (!hasWeapon) {
			for (Entity e : getArena().getChildren().values()) { // TODO: Remove this
				if (!hasWeapon && e instanceof Usable) {
					Usable usable = (Usable) e;
					float xDiff = Math.abs(getWorldPos().x - usable.getWorldPos().x);
					float yDiff = Math.abs(getWorldPos().y - usable.getWorldPos().y);
					if (xDiff < 1.75 && yDiff < 1.75)
						arme = usable;
				}
			}

			if (arme != null) {
				arme.attachToParent(this, "Item_Weapon");
				Audio.playSound2D(arme.soundPickup, AudioChannel.SFX, 1, Utils.lerpF(.8f, 1.2f, Math.random()),
						getWorldPos());
			}
		}
	}

	public void throwItem() {
		attackStop();

		Item item = (Item) getChild("Item_Weapon");
		if (item != null) {
			if (getArena() != null) {
				if (item instanceof Spatial)
					((Spatial) item).localPosition.set(((Spatial) item).getWorldPos());
				item.attachToParent(getArena(), item.genName());
				RigidBodyContainer rigBod = ((RigidBodyContainer) item.getChild("rigid_body"));
				rigBod.setLinearVelocity(Vec2f.multiply(Vec2f.fromAngle(aimInput), 20));
				rigBod.setAngularVelocity((float) (Math.PI * 2 * Math.random() + 12 * Math.PI));
				item.canDealDamage = true;
				Audio.playSound2D("data/sound/throw.ogg", AudioChannel.SFX, 1, (float) (0.95 + Math.random() * 0.1),
						getWorldPos());
			} else
				item.detach();
		}
	}

	public Usable getWeapon() {
		Entity e = getChild("Item_Weapon");
		if (e instanceof Usable)
			return (Usable) e;
		else
			return null;
	}

	public void heal(float healed) {
		// TODO Effects
		if (health + healed > healthMax) {
			health = healthMax;
		} else {
			health = health + healed;
		}
	}

	@Override
	public float takeDamage(DamageInfo info) {
		// Force death if character fell out of bounds or was killed for a non-gameplay
		// reason
		if (info.dmgType == DamageType.OUT_OF_BOUNDS) {
			if (ignoreKillBounds)
				return 0;
			else {
				death(info);
				return health;
			}
		}
		if (info.dmgType == DamageType.MISC_ONE_SHOT) {
			death(info);
			return health;
		}

		if (info.dmgType == DamageType.MELEE) {
			charged = false;
			chargePunch.reset();
		}

		((CharacterSprite) getChild("skeleton")).damageEffects(info);

		float res = Math.min(info.damage, health);

		applyImpulse(Vec2f.multiply(info.direction, info.damage));
		// float bumpX = (info.damage >= 1 ? 4 * (1 + ((float) Math.log10(info.damage)))
		// : 4);
		// float bumpY = (info.damage >= 1 ? 2.5f * (1 + ((float)
		// Math.log10(info.damage))) : 2.5f);

		// if (droite) //TODO: impulsex
		// vel.add(new Vec2f(bumpX, -bumpY));
		// else
		// vel.add(new Vec2f(-bumpX, -bumpY));

		health = Math.max(0, health - info.damage);

		if (health <= 0)
			death(info);

		return res;
	}

	private void death(DamageInfo deathCause) {
		// TODO: Effects
		// if(deathCause.dmgType == DamageType.EXPLOSION)
		((CharacterSprite) getChild("skeleton")).explode(Vec2f.multiply(deathCause.direction, deathCause.damage));

		if (health > 0 && deathCause.dmgType == DamageType.OUT_OF_BOUNDS) {
			Window.getCamera().setCameraShake(1);
			// TODO: Improve random sound
			Audio.playSound2D("data/sound/crush_0" + ((int) (Math.random() * 5) + 1) + ".ogg", AudioChannel.SFX, 1, 1,
					getWorldPos());
		}

		health = 0;
		throwItem();
		if (controller != null)
			controller.death(deathCause);
		detach();
	}

	@Override
	public void step(double d) {
		if (getArena() == null)
			return;
		
		CharacterSprite skeleton = ((CharacterSprite) getChild("skeleton"));

		if (Math.random() > 0.6)// ???
			jumpTimer.isOver();// ??? What is the fuque ?

		if (parry.isOver()) {
			parryCooldown = true;
			skeleton.breakParry();
		}
		if (parryCooldown && parry.getValue() < 0.75) {
			parryCooldown = false;
			skeleton.canParry = true;
		}

		if (!stunned) {
			// Aim
			if (!isAiming) {
				if (movementInput > 0)
					lookRight = true;
				else if (movementInput < 0)
					lookRight = false;
			} else {
				aimInput = Utils.normalizeAngle(aimInput);
				if (aimInput < Math.PI / 2 && aimInput > -Math.PI / 2)
					lookRight = true;
				else
					lookRight = false;
			}
			if (!isAiming && !lookRight) {
				aimInput = Math.PI;
			}
			double velX = Utils.lerpD(getLinearVelocity().x, movementInput * maxSpeed,
					Utils.clampD(d * (isOnGround ? 10 : 7), 0, 1));
			getBody().setLinearVelocity(new Vec2f(velX, getLinearVelocity().y));
		} else {
			stun.step(d);
			if (stun.isOver()) {
				stunned = false;
				skeleton.stunStop();
			}
		}

		if (getBody().getBody() != null)
			getBody().getBody().setGravityScale(6);

		isOnGround = false;

		if (!jumpTimer.isProcessing() || (jumpTimer.inProcess && jumpTimer.getValue() > 0.2)) {
			for (int i = 0; i < 4; i++) {
				Vec2f start = getWorldPos().clone();
				if (i == 0 || i == 3)
					start.y += .7;
				else
					start.y += 1;
				start.x -= .4;
				start.x += i * .2;

				Vec2f end = start.clone();
				end.y += .1;

				getArena().physic.getB2World().raycast(GroundRaycastCallback, start.toB2Vec(), end.toB2Vec());
			}
		}

		if (isOnGround)
			jumpTimer.reset();

		if (skeleton != null) {
			skeleton.setLookRight(lookRight);
			skeleton.charging = chargePunch.isProcessing();
			if (!skeleton.charged && chargePunch.isOver()) {
				skeleton.charged = true;
				Audio.playSound2D("data/sound/Souing.ogg", AudioChannel.SFX, 0.7f, 1.5f, getWorldPos());
			}
		}

		// Kill character when out of bounds
		if (Math.abs(getWorldPos().x) > 500 || Math.abs(getWorldPos().y) > 500)
			takeDamage(new DamageInfo(0, DamageType.OUT_OF_BOUNDS, new Vec2f(), null));

		super.step(d);
	}

	public float getHealth() {
		return health;
	}

	public float getHealthMax() {
		return healthMax;
	}

	public boolean isDead() {
		return health <= 0;
	}

	public Vec2f getSpawn() {
		return spawn;
	}

	RayCastCallback GroundRaycastCallback = new RayCastCallback() {
		@Override
		public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
			// Ignore sensors
			if (fixture.isSensor())
				return -1;

			// Ignore anything the character doesn't collide with
			if ((fixture.getFilterData().categoryBits & CollisionFlags.CHARACTER.maskBits) == 0)
				return -1;

			isOnGround = true;
			punchi = false;
			return fraction;
		}
	};

	private HashMap<Spatial, Float> punchHit = new HashMap<>();
	/** Farthest blocking entity hit by the punch */
	private float punchRayFraction = 0;
	RayCastCallback PunchRaycastCallback = new RayCastCallback() {
		@Override
		public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
			// Ignore sensors
			if (fixture.isSensor())
				return -1;

			// We hit something that isn't self
			if (fixture.getUserData() instanceof Spatial && fixture.getUserData() != this) {
				if (punchHit.containsKey(fixture.getUserData())) {
					if (punchHit.get(fixture.getUserData()) > fraction)
						punchHit.put((Spatial) fixture.getUserData(), fraction);
				} else {
					punchHit.put((Spatial) fixture.getUserData(), fraction);
				}
			}

			// Ignore anything the character doesn't collide with
			if ((fixture.getFilterData().categoryBits) == 0)
				return -1;

			punchRayFraction = Math.max(punchRayFraction, fraction);
			return fraction;
		}
	};
}