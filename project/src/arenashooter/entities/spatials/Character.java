package arenashooter.entities.spatials;

import java.util.HashMap;
import java.util.Map;

import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

import arenashooter.engine.DamageInfo;
import arenashooter.engine.DamageType;
import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec2fi;
import arenashooter.engine.physic.CollisionCategory;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.ShapeCharacter;
import arenashooter.entities.Entity;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.items.Item;
import arenashooter.entities.spatials.items.Usable;
import arenashooter.game.CharacterClass;
import arenashooter.game.CharacterInfo;
import arenashooter.game.Controller;
import arenashooter.game.Main;

public class Character extends RigidBodyContainer {

	public Controller controller = null;
	private float punchDamage = 10;
	private float health, healthMax;

	public boolean lookRight = true;
	public boolean isAiming = false;
	public double aimInput = 0;

	// Movement stats
	public double maxSpeed = 15;
	boolean isOnGround = true;
	private boolean canJump = true;
	private int canWallJump = 0;// -1 if the wall is on the left, 1 if on the right, 0 else
	public float movementInputX = 0;
	public float movementInputY = 0;
	/**
	 * The Character is jumping
	 */
	public boolean jumpi;
	public int bonusJumpsMax = 0;
	public int bonusJumpsUsed = 0;
	public double weight = 1;
	private double jumpForce = 23;
	private double punchDashForce = 1;
	private double parachuteForce = 8.5;
	private Timer jumpTimer = new Timer(0.6);
	private Timer justInTime = new Timer(0.2);
	private int lastJumpCouldMake = 0;

	// Combat stats
	private boolean bushido = false;// still has control and explodes 3s after dying;
	private Timer afterDeath;
	private DamageInfo upcomingDeath;// used for bushido Death
	/** Melee attack cooldown */
	private boolean stunned = false;
	private Timer stun = null;
	private Timer attackCooldown = new Timer(0.35);
	private int attackCombo = 0;
	private Timer parry = new Timer(1.5);
	private boolean parryCooldown = false;
	private Timer chargePunch = new Timer(1.2);
	/**
	 * For now, this boolean is used for visuals and Sounds only.
	 */
	boolean charged = false;
	private Timer holdCombo = new Timer(1);
	public int punching;
	private float range = 2.5f;
	private float hitWidth = 2.0944f;
	/**
	 * 
	 * The Character has already punched mid-air
	 */
	private boolean punchedMidAir = false;

	public Character(Vec2fi position, CharacterInfo charInfo , boolean demo) {
		super(new RigidBody(new ShapeCharacter(), position, 0, (demo? CollisionFlags.NONE: CollisionFlags.CHARACTER),
				(charInfo.getCharClass() == CharacterClass.Heavy ? 1.5f : .5f), 1));

		getBody().setBullet(true);
		getBody().setRotationLocked(true);

		healthMax = 50;
		health = healthMax;

		localRotation = 0;

		switch (charInfo.getCharClass()) {
		case Heavy:
			weight = 3;
			punchDashForce = 11;
			jumpForce = 23 * 1;
			break;
		case Agile:
			maxSpeed = 18.5;
			weight = 0.8;
			jumpForce = 26;
			break;
		case Aqua:
			bushido = true;
			afterDeath = new Timer(3);
			afterDeath.attachToParent(this, "bushido_Timer");
			afterDeath.reset();
			break;
		case Bird:
			bonusJumpsMax = 1;
			parachuteForce = 12;
			jumpTimer.setMax(2);
			break;
		default:
			break;
		}

		attackCooldown.attachToParent(this, "attack timer");
		chargePunch.attachToParent(this, "powerpunch charging");
		chargePunch.setProcessing(false);
		jumpTimer.attachToParent(this, "jump Timer");
		justInTime.attachToParent(this, "just_In_Time jump");
		holdCombo.attachToParent(this, "attack Combo Hold");
		parry.attachToParent(this, "parry timer");
		parry.setIncreasing(false);

		CharacterSprite skeleton = new CharacterSprite(charInfo);
		skeleton.attachToParent(this, "skeleton");
	}

	public void jump() {
		float y = getLinearVelocity().y();
		float x = getLinearVelocity().x();
		if (jumpTimer.getValue() > .4 || jumpTimer.getValue() == 0) {
			if (canJump || (!justInTime.isOver() && lastJumpCouldMake == 0)) {
				jumpTimer.reset();
				jumpTimer.setProcessing(true);
				Main.getAudioManager().playSound2D("data/sound/jump.ogg", AudioChannel.SFX, .7f, Utils.lerpF(.9f, 1.2f, Math.random()),
						getWorldPos());

				isOnGround = false;
				jumpi = true;
				// getLinearVelocity().y = 0;
				// setLinearVelocity(Vec2f.add(getLinearVelocity(), new Vec2f(0,
				// (float)-jumpForce)));
				getBody().setLinearVelocity(new Vec2f(x, -jumpForce + (y < 0 ? y / 2 : 0)));
			} else if (canWallJump != 0 || !justInTime.isOver()) {
				jumpTimer.reset();
				jumpTimer.setProcessing(true);
				Main.getAudioManager().playSound2D("data/sound/jump.ogg", AudioChannel.SFX, .7f, Utils.lerpF(.9f, 1.2f, Math.random()),
						getWorldPos());
				jumpi = true;
				if (canWallJump != 0)
					getBody().setLinearVelocity(Vec2f.rotate(new Vec2f(x, -jumpForce * 1.525 + (y < 0 ? y / 2 : 0)),
							(canWallJump > 0 ? -1 : 1)));
				else
					getBody().setLinearVelocity(Vec2f.rotate(new Vec2f(x, -jumpForce * 1.525 + (y < 0 ? y / 2 : 0)),
							(lastJumpCouldMake > 0 ? -.9 : .9)));
			} else if (bonusJumpsUsed++ < bonusJumpsMax) {
				jumpTimer.reset();
				jumpTimer.setProcessing(true);
				Main.getAudioManager().playSound2D("data/sound/jump.ogg", AudioChannel.SFX, .7f, Utils.lerpF(.9f, 1.2f, Math.random()),
						getWorldPos());
				// TODO: Skeleton double jump feather Effects
				((CharacterSprite) getChild("skeleton")).plum();
				isOnGround = false;
				jumpi = true;
				// getLinearVelocity().y = 0;
				// setLinearVelocity(Vec2f.add(getLinearVelocity(), new Vec2f(0,
				// (float)-jumpForce)));
				getBody().setLinearVelocity(new Vec2f(x, -jumpForce * 0.8 + (y < 0 ? y / 2 : 0)));
			}
		} else if (bonusJumpsUsed++ < bonusJumpsMax) {
			jumpTimer.reset();
			jumpTimer.setProcessing(true);
			Main.getAudioManager().playSound2D("data/sound/jump.ogg", AudioChannel.SFX, .7f, Utils.lerpF(.9f, 1.2f, Math.random()),
					getWorldPos());
			// TODO: Skeleton double jump feather Effects
			((CharacterSprite) getChild("skeleton")).plum();
			isOnGround = false;
			jumpi = true;
			// getLinearVelocity().y = 0;
			// setLinearVelocity(Vec2f.add(getLinearVelocity(), new Vec2f(0,
			// (float)-jumpForce)));
			getBody().setLinearVelocity(new Vec2f(x, -jumpForce * 0.8 + (y < 0 ? y / 2 : 0)));
		}
		if (!justInTime.isOver())
			justInTime.setValue(justInTime.getMax());
	}

	public void planer() {
		if (!isOnGround) {
			if (!jumpTimer.isOver() && jumpTimer.inProcess) {
				if (getLinearVelocity().y() < 0 && jumpi) {
					// vel.y += (float) (-parachuteForce * Math.expm1(1 -
					// (jumpTimer.getValueRatio())));
					getBody().applyForce(
							new Vec2f(0, -parachuteForce * weight * Math.expm1(1 - (jumpTimer.getValueRatio()))));
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
		if (getLinearVelocity().y() < 0) {
			getBody().setLinearVelocity(new Vec2f(getLinearVelocity().x(), getLinearVelocity().y() / 2));
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
	 *            speed is not taken into account, only absolute speed
	 * @return true if the character is blocking in the direction of the attack.
	 */
	public boolean canParryThis(double attackAngle) {
		return parry.isIncreasing() && !parry.isOver()
				&& Vec2f.areOpposed(Vec2f.fromAngle(attackAngle), Vec2f.fromAngle(aimInput), hitWidth / 2);
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
		parryStop();
		CharacterSprite skeleton = ((CharacterSprite) getChild("skeleton"));
		skeleton.stunStart(stunTime);
		skeleton.stopCharge();
	}

	public void attackStart(boolean justPressed) {
		if (stunned)
			return;

		if (getWeapon() != null) {
				getWeapon().attackStart(justPressed);
		} else if (attackCooldown.isOver()) {
			parryStop();
			chargePunch.setProcessing(true);
		}
	}
	
	public void attackStartDemo(boolean justPressed) {
		if (getWeapon() != null) {
			if (justPressed)
				getWeapon().attackStart(true);
		} else if (attackCooldown.isOver()) {
			chargePunch.setProcessing(true);
		}
	}
	
	public void attackStopDemo() {
		if (getWeapon() != null) {
			getWeapon().attackStop();
		} else if (chargePunch.isProcessing()) {

			boolean superPoing = chargePunch.isOver();
			chargePunch.reset();
			CharacterSprite skeleton = ((CharacterSprite) getChild("skeleton"));
			DamageInfo punchDmgInfo;

			if (superPoing) {
				punchDmgInfo = new DamageInfo((float) (punchDamage * 1.5), DamageType.MELEE, Vec2f.fromAngle(aimInput),
						0, this);
				skeleton.punch(-1, aimInput);
			} else {
				punchDmgInfo = new DamageInfo(punchDamage, DamageType.MELEE, Vec2f.fromAngle(aimInput), 0, this);
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

			punching++;
			attackCooldown.restart();
			skeleton.stopCharge();

			Punch ponch = new Punch(punchDmgInfo, hitWidth, range, superPoing);
			ponch.attachToParent(this, "punch_" + genName());

			attackCombo %= 3;

		}
	}

	public void attackStop() {
		if (stunned)
			return;
		if (getWeapon() != null) {
			getWeapon().attackStop();
		} else if (chargePunch.isProcessing()) {
			parryStop();

			boolean superPoing = chargePunch.isOver();
			chargePunch.reset();
			Vec2f impulse;
			CharacterSprite skeleton = ((CharacterSprite) getChild("skeleton"));
			DamageInfo punchDmgInfo;

			if (superPoing) {
				impulse = Vec2f.rotate(new Vec2f((!punchedMidAir ? 25 : 12) * punchDashForce, 0), aimInput);
				punchDmgInfo = new DamageInfo((float) (punchDamage * 1.5), DamageType.MELEE, Vec2f.fromAngle(aimInput),
						20, this);
				skeleton.punch(-1, aimInput);
			} else {
				impulse = Vec2f.rotate(new Vec2f((!punchedMidAir ? 16 : 8), 0), aimInput);
				punchDmgInfo = new DamageInfo(punchDamage, DamageType.MELEE, Vec2f.fromAngle(aimInput), 10, this);
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

			if (impulse.y < 0) {
				if (getLinearVelocity().y() < 0)
					impulse.y = 0;
				else
					impulse.y = (float) (-Math.log(-impulse.y) * 4);
			}
			getBody().applyImpulse(impulse);
			punching++;
			punchedMidAir = true;
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
					float xDiff = Math.abs(getWorldPos().x() - usable.getWorldPos().x());
					float yDiff = Math.abs(getWorldPos().y() - usable.getWorldPos().y());
					if (xDiff < 1.75 && yDiff < 1.75)
						arme = usable;
				}
			}

			if (arme != null) {
				arme.attachToParent(this, "Item_Weapon");
				Main.getAudioManager().playSound2D(arme.soundPickup, AudioChannel.SFX, 1, Utils.lerpF(.8f, 1.2f, Math.random()),
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
				rigBod.setLinearVelocity(Vec2f.multiply(Vec2f.fromAngle(aimInput), 25));
				rigBod.setAngularVelocity((float) (Math.PI * 2 * Math.random() + 12 * Math.PI * (lookRight ? 1 : -1)));
				item.canDealDamage = true;
				Main.getAudioManager().playSound2D("data/sound/throw.ogg", AudioChannel.SFX, 1, (float) (0.95 + Math.random() * 0.1),
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
			((CharacterSprite)getChild("skeleton")).stopCharge();
		}

		((CharacterSprite) getChild("skeleton")).damageEffects(info);

		float res = Math.min(info.damage, health);

		applyImpulse(Vec2f.multiply(info.direction, info.impulse));

		health = Math.max(0, health - info.damage);

		if (health <= 0)
			death(info);

		return res;
	}

	private void death(DamageInfo deathCause) {
		// TODO: Effects
		// if(deathCause.dmgType == DamageType.EXPLOSION)
		if (!bushido) {
			((CharacterSprite) getChild("skeleton")).explode(Vec2f.multiply(deathCause.direction, deathCause.damage));

			if (health > 0 && deathCause.dmgType == DamageType.OUT_OF_BOUNDS) {
				Window.getCamera().setCameraShake(2);
				// TODO: Improve random sound
				Main.getAudioManager().playSound2D("data/sound/crush_0" + ((int) (Math.random() * 5) + 1) + ".ogg", AudioChannel.SFX, 1,
						1, getWorldPos());
			}

			health = 0;
			throwItem();
			if (controller != null)
				controller.death(deathCause);
			detach();
		} else if (deathCause.dmgType == DamageType.MISC_ONE_SHOT || deathCause.dmgType == DamageType.OUT_OF_BOUNDS) {
			bushidoDeath(deathCause);
		} else {
			if (!afterDeath.isProcessing()) {
				controller.zombieChar();
				upcomingDeath = deathCause;
				afterDeath.inProcess = true;
				maxSpeed += 4;
				((CharacterSprite) getChild("skeleton")).activateBushidoMode();
				getBody().setRotationLocked(false);
			}
		}
	}

	private void bushidoDeath(DamageInfo deathCause) {
		if (deathCause.dmgType != DamageType.MISC_ONE_SHOT) {
			Explosion explosion = new Explosion(getWorldPos(),
					new DamageInfo(90, DamageType.EXPLOSION, new Vec2f(), 30, this), 10);
			explosion.attachToParent(getArena(), explosion.genName());
			CharacterSprite skeleton = ((CharacterSprite) getChild("skeleton"));
			skeleton.explode(Vec2f.multiply(deathCause.direction, deathCause.damage));

			for (int i = 0; i < 5; i++) {
				Particles blood = new Particles(new Vec2f(), "data/particles/blood.xml");
				blood.selfDestruct = true;
				blood.attachToParent(skeleton, blood.genName());
			}
		}

		if (health > 0 && deathCause.dmgType == DamageType.OUT_OF_BOUNDS) {
			Window.getCamera().setCameraShake(2);
			// TODO: Improve random sound
			Main.getAudioManager().playSound2D("data/sound/crush_0" + ((int) (Math.random() * 5) + 1) + ".ogg", AudioChannel.SFX, 1, 1,
					getWorldPos());
		}
		health = 0;
		throwItem();
		if (controller != null)
			controller.death(deathCause);
		detach();
	}

	private int jumpPoints = 0;
	private boolean autoSlideUp = false;
	private boolean wallRayHit = false;

	@Override
	public void step(double d) {
		if (getArena() == null)
			return;

		CharacterSprite skeleton = ((CharacterSprite) getChild("skeleton"));

		if (parry.isOver()) {
			parryCooldown = true;
			skeleton.breakParry();
		}
		if (parryCooldown && parry.getValue() < 0.75) {
			parryCooldown = false;
			skeleton.canParry = true;
		}

		double velX = Utils.lerpD(getLinearVelocity().x(), movementInputX * maxSpeed, Utils.clampD(d * 15, 0, 1));
		double velY = getLinearVelocity().y();
		if (!stunned) {
			// Aim
			if (!isAiming) {
				if (movementInputX > 0)
					lookRight = true;
				else if (movementInputX < 0)
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

			if (movementInputY > 0.4) {
				if (!isOnGround && velY < 40)
					velY = Utils.lerpD(velY, movementInputY * 40, Utils.clampD(d * 15, 0, 1));
			} else if (getLinearVelocity().y() > 75)
				velY = Utils.lerpD(velY, 75, Utils.clampD(d * 15, 0, 1));
			else if (getLinearVelocity().y() < -65)
				velY = Utils.lerpD(velY, -65, Utils.clampD(d * 15, 0, 1));
			getBody().setLinearVelocity(new Vec2f(velX, velY));
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
		jumpPoints = 0;

		if (!jumpTimer.isProcessing() || (jumpTimer.isProcessing() && jumpTimer.getValue() > 0.2)) {
			Vec2f start = new Vec2f(), end = new Vec2f();
			for (int i = 0; i < 4; i++) {
				start.set(getWorldPos());
				if (i == 0 || i == 3)
					start.y += .7;
				else
					start.y += 1;
				start.x -= .4;
				start.x += i * .2;

				end.set(start);
				end.y += .1;

				getArena().physic.raycast(start, end, GroundRaycastCallback);
			}
		}

		canJump = isOnGround;
		if (isOnGround && jumpTimer.getValue() > .2)
			jumpTimer.reset();
		autoSlideUp = false;
		boolean touchingRightWall = false;
		boolean touchingLeftWall = false;

		float x = getWorldPos().x();
		float y = getWorldPos().y();

		if (!canJump) {
			getArena().physic.raycast(new Vec2f(.5 + x, y), new Vec2f(.5 + x, .8 + y), JumpRaycastCallback);

			getArena().physic.raycast(new Vec2f(-.5 + x, y), new Vec2f(-.5 + x, .8 + y), JumpRaycastCallback);
			if (jumpPoints >= 2)
				canJump = true;
		}

		//
		// deciding WallJump
		wallRayHit = false;
		for (int i = 1; i < 6; i++) {
			getArena().physic.raycast(new Vec2f(x, y + .18 * i), new Vec2f(x + (i == 5 ? .4 : .55), y + .18 * i), WallRaycastCallback);
		}
		if (wallRayHit)
			touchingRightWall = true;

		wallRayHit = false;
		for (int i = 0; i < 5 && !wallRayHit; i++) {
			getArena().physic.raycast(new Vec2f(x, y - .2 * i), new Vec2f(x + .55, y - .2 * i), WallRaycastCallback);
		}
		if (!wallRayHit && touchingRightWall)
			autoSlideUp = true;
		if (wallRayHit)
			touchingRightWall = true;

		wallRayHit = false;
		for (int i = 1; i < 6; i++) {
			getArena().physic.raycast(new Vec2f(x, y + .18 * i), new Vec2f(x - (i == 5 ? .4 : .55), y + .18 * i), WallRaycastCallback);
		}
		if (wallRayHit)
			touchingLeftWall = true;

		wallRayHit = false;
		for (int i = 0; i < 5 && !wallRayHit; i++) {
			getArena().physic.raycast(new Vec2f(x, y - .2 * i), new Vec2f(x - .55, y - .2 * i), WallRaycastCallback);
		}
		if (!wallRayHit && touchingLeftWall)
			autoSlideUp = true;
		if (wallRayHit)
			touchingLeftWall = true;

		if (touchingLeftWall) {
			canWallJump = -1;
			if (velX < 0) {
				velX = 0;
				if (autoSlideUp) {
					if (!stunned)
						if (velY > -8)
							// setLinearVelocity(new Vec2f(velX, -8));
							getBody().applyImpulse(new Vec2f(velX, -15 * d));
				} else {
					if (velY > 5)
						velY = Utils.lerpD(velY, 5, d * 15);
				}
			}

		}
		if (touchingRightWall) {
			canWallJump = 1;
			if (velX > 0) {
				velX = 0;
				if (autoSlideUp) {
					if (!stunned)
						if (velY > -8)
							// setLinearVelocity(new Vec2f(velX, -8));
							getBody().applyImpulse(new Vec2f(velX, -15 * d));
				} else {
					if (velY > 5)
						velY = Utils.lerpD(velY, 5, d * 15);
				}
			}

		}
		if (touchingLeftWall || touchingRightWall)
			setLinearVelocity(new Vec2f(velX, velY));
		else {
			canWallJump = 0;
		}

		if (canJump) {
			lastJumpCouldMake = 0;
			justInTime.restart();
		} else if (canWallJump != 0) {
			lastJumpCouldMake = canWallJump;
			justInTime.restart();
		}

		//
		// Skeleton Update
		if (skeleton != null) {
			skeleton.setLookRight(lookRight);
			skeleton.charging = chargePunch.isProcessing();
			if (!skeleton.charged && chargePunch.isOver()) {
				skeleton.charged = true;
				Main.getAudioManager().playSound2D("data/sound/Souing.ogg", AudioChannel.SFX, 0.7f, 1.5f, getWorldPos());
			}
		}

		super.step(d);
		if (bushido) {
			if (afterDeath.isProcessing()) {
				if (afterDeath.isOver()) {
					bushidoDeath(upcomingDeath);
				}
			}
		}
	}

	public float getHealth() {
		return health;
	}

	public float getHealthMax() {
		return healthMax;
	}

	public boolean isDead() {
		return health <= 0 && (!bushido || afterDeath.isOver());
	}

	/**
	 * Used to check if the Character is on the ground
	 */
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
			bonusJumpsUsed = 0;
			punchedMidAir = false;
			return fraction;
		}
	};

	/**
	 * Used to check if the Character can jump
	 */
	RayCastCallback JumpRaycastCallback = new RayCastCallback() {
		@Override
		public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
			// Ignore sensors
			if (fixture.isSensor())
				return -1;

			// Ignore anything the character doesn't collide with
			if ((fixture.getFilterData().categoryBits & CollisionFlags.CHARACTER.maskBits) == 0)
				return -1;

			jumpPoints++;
			return fraction;
		}
	};

	/**
	 * Used to check if the Character is touching a wall
	 */
	RayCastCallback WallRaycastCallback = new RayCastCallback() {
		@Override
		public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
			// Ignore sensors
			if (fixture.isSensor())
				return -1;

			if ((fixture.getFilterData().categoryBits & CollisionCategory.CAT_CHARACTER.bits) != 0 || (fixture.getFilterData().categoryBits & CollisionCategory.CAT_CORPSE.bits) != 0 || (fixture.getFilterData().categoryBits & CollisionCategory.CAT_EXPLOSION.bits) != 0 || (fixture.getFilterData().categoryBits & CollisionCategory.CAT_ITEM.bits) != 0 || (fixture.getFilterData().categoryBits & CollisionCategory.CAT_PROJ.bits) != 0 || (fixture.getFilterData().categoryBits & CollisionCategory.CAT_RIGIDBODY.bits) != 0)
				return -1;

			// Ignore anything the character doesn't collide with
			if ((fixture.getFilterData().categoryBits & CollisionFlags.CHARACTER.maskBits) == 0)
				return -1;

			wallRayHit = true;

			return fraction;
		}
	};

	private Map<Spatial, Float> punchHit = new HashMap<>();
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