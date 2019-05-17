package arenashooter.entities.spatials;

import arenashooter.engine.Profiler;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.ImpactOld;
import arenashooter.entities.Controller;
import arenashooter.entities.Entity;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.items.Item;
import arenashooter.entities.spatials.items.Usable;
import arenashooter.game.CharacterInfo;
import arenashooter.game.GameMaster;

public class Character extends Spatial {

	public Controller controller = null;
	private static final float defaultDamage = 10;
	private float health, healthMax;
	private final Vec2f spawn;

	public Vec2f vel = new Vec2f();
	public Collider collider;
	boolean isOnGround = true;
	public float movementInput = 0;
	public boolean lookRight = true;
	public boolean isAiming = false;
	public double aimInput = 0;

	/**
	 * The character is jumping
	 */
	public boolean jumpi;
	private double jumpForce = 30;
	private double parachuteForce = 0.5;
	private Timer jumpTimer = new Timer(0.5);
	private Timer attack = new Timer(0.3);

	public Character(Vec2f position, CharacterInfo charInfo) {
		super(position);
		
//		Sprite colliderVis = new Sprite(new Vec2f(), "data/default_texture.png");
//		colliderVis.size = new Vec2f(1, 2);
//		colliderVis.attachToParent(this, "collider_vis");
//		colliderVis.zIndex = -10;

		healthMax = 40;
		health = healthMax;
		spawn = position;

		rotation = 0;

		collider = new Collider(this.pos(), new Vec2f(.5, 1));
		collider.attachToParent(this, "coll_Body");

		attack.attachToParent(this, "attack timer");
		jumpTimer.attachToParent(this, "jump Timer");

		CharacterSprite skeleton = new CharacterSprite(this.pos(), charInfo);
		skeleton.attachToParent(this, "skeleton");

		SoundEffect jumpSound = new SoundEffect(this.pos(), "data/sound/jump.ogg", 2);
		jumpSound.setVolume(.7f);
		jumpSound.attachToParent(this, "snd_Jump");

		SoundEffect punchHitSound = new SoundEffect(this.pos(), "data/sound/snd_Punch_Hit2.ogg", 2);
		punchHitSound.setVolume(.7f);
		punchHitSound.attachToParent(this, "snd_Punch_Hit");
	}

	public void jump() {
		if (isOnGround) {
			isOnGround = false;
			jumpi = true;
			vel.y = (float) -jumpForce;
			jumpTimer.reset();
			jumpTimer.setProcessing(true);
			((SoundEffect) getChildren().get("snd_Jump")).play();
		}
	}

	public void planer() {
		if (!isOnGround) {
			if (!jumpTimer.isOver() && jumpTimer.inProcess) {
				if (vel.y < 0 && jumpi) {
					vel.y += (float) (-parachuteForce * Math.expm1(1 - (jumpTimer.getValueRatio())));
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
		if (vel.y < 0) {
			vel.y = vel.y / 2;
		}
	}

	public void attackStart() {
		if (getWeapon() != null) {
			getWeapon().attackStart();
		} else if (attack.isOver()) {
			attack.restart();

			CharacterSprite skeleton = ((CharacterSprite) getChildren().get("skeleton"));
			if (skeleton != null)
				skeleton.punch();

			for (Entity entity : GameMaster.gm.getEntities()) {
				if (entity instanceof Character && entity != this) {
					Character c = (Character) entity;

					boolean isInFrontOfMe = false;
					if (skeleton != null) {
						if ((lookRight && collider.getXRight() < (c.collider.getXRight() + 40))
								|| (!lookRight && collider.getXLeft() > (c.collider.getXLeft() - 40))) {
							isInFrontOfMe = true;
						}
					}

					if (isInFrontOfMe) {
						float xDiff = Math.abs(pos().x - c.pos().x);
						float yDiff = Math.abs(pos().y - c.pos().y);
						if (xDiff < 175 && yDiff < 175) {
							c.takeDamage(defaultDamage, lookRight);
							((SoundEffect) getChildren().get("snd_Punch_Hit")).play();
						}
					}

				}
			}
		}

	}

	public void attackStop() {
		if (getWeapon() != null) {
			getWeapon().attackStop();
		}

	}

	public void getItem() {
		Item arme = null;

		boolean hasWeapon = getWeapon() != null;

		if (!hasWeapon) {
			for (Entity e : GameMaster.gm.getEntities()) {
				if (!hasWeapon && e instanceof Usable) {
					Usable usable = (Usable) e;
					float xDiff = Math.abs(pos().x - usable.pos().x);
					float yDiff = Math.abs(pos().y - usable.pos().y);
					if (xDiff < 1.75 && yDiff < 1.75)
						arme = usable;
				}
			}

			if (arme != null) {
				arme.attachToParent(this, "Item_Weapon");
				Entity soundPickup = arme.getChildren().get("sound_pickup");
				if(soundPickup instanceof SoundEffect) ((SoundEffect)soundPickup).play();
			}
		}
	}

	public void dropItem() {
		attackStop();
		if (getChildren().containsKey("Item_Weapon")) {
			Entity arme = getChildren().get("Item_Weapon");

			if (arme instanceof Usable)
				((Usable) arme).setVel(new Vec2f());

			arme.attachToParent(this.getParent(), arme.genName());
		}
	}

	public Usable getWeapon() {
		Entity e = getChildren().get("Item_Weapon");
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

	public float takeDamage(float damage, boolean droite) {// degats orientes

		float res = Math.min(damage, health);// ? Ajouter Commentaire

		float bumpX = (damage >= 1 ? 4 * (1 + ((float) Math.log10(damage))) : 4);
		float bumpY = (damage >= 1 ? 2.5f * (1 + ((float) Math.log10(damage))) : 2.5f);

		if (droite)
			vel.add(new Vec2f(bumpX, -bumpY));
		else
			vel.add(new Vec2f(-bumpX, -bumpY));

		health = Math.max(0, health - damage);

		if (health <= 0)
			death();

		return res;
	}

	public void death() {
		// TODO: Effects
		health = 0;
		dropItem();
		if (controller != null)
			controller.death();
		detach();
	}

	@Override
	public void step(double d) {

		if (Math.random() > 0.6)
			jumpTimer.isOver();

		Profiler.startTimer(Profiler.PHYSIC);

		vel.x = (float) Utils.lerpD(vel.x, movementInput * 15, Utils.clampD(d * (isOnGround ? 10 : 10), 0, 1));
		if (!isOnGround)
			vel.y += Math.min(9.807 * d * 10, 100);

		isOnGround = false;
		for (Entity plat : getParent().getChildren().values()) {
			if (plat instanceof Plateform) {
				for (Entity coll : ((Plateform) plat).getChildren().values()) {
					if (coll instanceof Collider) {
						Collider c = (Collider) coll;
						ImpactOld impact = new ImpactOld(collider, c, Vec2f.multiply(vel, (float) d));
						vel.x = vel.x * impact.getVelMod().x;
						vel.y = vel.y * impact.getVelMod().y;
						if (collider.getYBottom() + (vel.y * d) >= c.getYTop()
								&& collider.getYBottom() + (vel.y * d) < c.getYBottom()
								&& Collider.isX1IncluedInX2(collider, c))
							isOnGround = true;
					}
				}
			}
		}

		if (isOnGround)
			jumpTimer.reset();

		localPosition.add(Vec2f.multiply(vel, (float) d));

		Profiler.endTimer(Profiler.PHYSIC);

		// Animation
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

		CharacterSprite skeleton = ((CharacterSprite) getChildren().get("skeleton"));
		if (skeleton != null) {
			skeleton.setLookRight(lookRight);
		}

		if (Math.abs(pos().x) > 500 || Math.abs(pos().y) > 500) {
			death();
		}

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
}