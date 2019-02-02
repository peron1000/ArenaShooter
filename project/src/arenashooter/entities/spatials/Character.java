package arenashooter.entities.spatials;

import arenashooter.engine.Profiler;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.Impact;
import arenashooter.entities.Collider;
import arenashooter.entities.Entity;
import arenashooter.entities.SoundEffect;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.items.Item;
import arenashooter.entities.spatials.items.Weapon;
import arenashooter.entities.spatials.items.Equipement;
import arenashooter.game.GameMaster;

public class Character extends Spatial {
	private static final float defaultDamage = 5;
	private float health, healthMax;
	private final Vec2f spawn;

	public Vec2f vel = new Vec2f();
	public Collider collider;
	boolean isOnGround = true;
	public float movementInput = 0;
	public boolean lookRight = true;
	public boolean isAiming = false;
	public double aimInput = 0;

	private Timer attack = new Timer(0.3);

	public Character(Vec2f position, CharacterInfo charInfo) {
		super(position);

		healthMax = 40;
		health = healthMax;
		spawn = position;

		rotation = 0;

		collider = new Collider(this.position, new Vec2f(25, 60));
		collider.attachToParent(this, "coll_Body");

		attack.attachToParent(this, "attack timer");

		CharacterSprite skeleton = new CharacterSprite(this.position, charInfo);
		skeleton.attachToParent(this, "skeleton");

		SoundEffect jumpSound = new SoundEffect(this.position, "data/sound/jump.ogg", 2);
		jumpSound.setVolume(.7f);
		jumpSound.attachToParent(this, "snd_Jump");

		// SoundEffect punchHitSound = new SoundEffect(this.position,
		// "data/sound/punch_01.ogg");
		// punchHitSound.setVolume(.7f);
		// punchHitSound.attachToParent(this, "snd_Punch_Hit");

		SoundEffect punchHitSound = new SoundEffect(this.position, "data/sound/snd_Punch_Hit2.ogg", 2);
		punchHitSound.setVolume(.7f);
		punchHitSound.attachToParent(this, "snd_Punch_Hit");
	}

	public void jump(int saut) {
		if (!isOnGround)
			return;
		vel.y = -saut;
		((SoundEffect) children.get("snd_Jump")).play();
	}

	public void attackStart() {
		if (children.get("Item_Weapon") != null) {
			((Weapon) children.get("Item_Weapon")).attackStart();
		} else if (attack.isOver()) {
			attack.restart();

			CharacterSprite skeleton = ((CharacterSprite) children.get("skeleton"));
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
						float xDiff = Math.abs(position.x - c.position.x);
						float yDiff = Math.abs(position.y - c.position.y);
						if (xDiff < 175 && yDiff < 175) {
							c.takeDamage(defaultDamage, lookRight);
							((SoundEffect) children.get("snd_Punch_Hit")).play();
						}
					}

				}
			}
		}

	}
	
	public void attackStop() {
		if (children.get("Item_Weapon") != null) {
			((Weapon) children.get("Item_Weapon")).attackStop();
		}

	}

	public void getItem() {
		Item arme = null;
		Item armure = null;

		boolean hasWeapon = children.containsKey("Item_Weapon");
		boolean hasArmor = children.containsKey("Item_Armor");
		
		if (!hasWeapon || !hasArmor) {
			for (Entity e : GameMaster.gm.getEntities()) {
				if (!hasWeapon && e instanceof Weapon) {
					Item weapon = (Weapon) e;
					float xDiff = Math.abs(position.x - weapon.position.x);
					float yDiff = Math.abs(position.y - weapon.position.y);
					if (xDiff < 175 && yDiff < 175)
						arme = weapon;
				} else if (!hasArmor && e instanceof Equipement) {
					Item item = (Equipement) e;
					float xDiff = Math.abs(position.x - item.position.x);
					float yDiff = Math.abs(position.y - item.position.y);
					if (xDiff < 175 && yDiff < 175)
						armure = item;
				}
			}

			if (arme != null) {
				arme.attachToParent(this, "Item_Weapon");
				((SoundEffect) arme.children.get("snd_Pickup")).play();
			}
			if (armure != null)
				armure.attachToParent(this, "Item_Armor");
		}
	}

	public void dropItem() {
		if (children.containsKey("Item_Weapon")) {
			Entity arme = children.get("Item_Weapon");
			((Weapon)arme).setVel(new Vec2f());
			arme.attachToParent(this.getParent(), arme.genName());
		}
	}

	public float takeDamage(float damage, boolean droite) {// degats orientes

		float res = Math.min(damage, health);// ? Ajouter Commentaire

		float bumpX = (damage >= 1 ? 400 * (1 + ((float) Math.log10(damage))) : 400);
		float bumpY = (damage >= 1 ? 250 * (1 + ((float) Math.log10(damage))) : 250);

		if (droite)
			vel.add(new Vec2f(bumpX, -bumpY));
		else
			vel.add(new Vec2f(-bumpX, -bumpY));

		health = Math.max(0, health - damage);

		if (health <= 0)
			death();

		return res;
	}

	private void death() {
		health = 0;
		// TODO: Effects
		health = healthMax;
		position = new Vec2f(spawn.x, spawn.y);
		vel = new Vec2f();
	}

	@Override
	public void step(double d) {

		Profiler.startTimer(Profiler.PHYSIC);

		vel.x = (float) Utils.lerpD(vel.x, movementInput * 1500, d * (isOnGround ? 10 : 10));
		if (!isOnGround)
			vel.y += Math.min(9.807 * 800 * d, 2000);

		isOnGround = false;
		for (Entity plat : getParent().children.values()) {
			if (plat instanceof Plateform) {
				for (Entity coll : ((Plateform) plat).children.values()) {
					if (coll instanceof Collider) {
						Collider c = (Collider) coll;
						Impact impact = new Impact(collider, c, Vec2f.multiply(vel, (float) d));
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

		position.add(Vec2f.multiply(vel, (float) d));

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

		CharacterSprite skeleton = ((CharacterSprite) children.get("skeleton"));
		if (skeleton != null) {
			skeleton.setLookRight(lookRight);
		}

		if (Math.abs(position.x) > 10000 || Math.abs(position.y) > 10000) {
			death();
		}

		// Updates Children, but Lerp for the Weapon instead of just giving the
		// position.
		if (children.get("Item_Weapon") instanceof Weapon) {
			Weapon arme = (Weapon) children.get("Item_Weapon");
//			Vec2f weaponPosition = Vec2f.add((Vec2f.rotate(new Vec2f(40, 0), aimInput)), position);
			Vec2f weaponPosition = Vec2f.add((Vec2f.rotate(new Vec2f(50, 0), arme.rotation)), position);//rotation indépendante
			arme.position.x = (float) Utils.lerpD((double) arme.position.x, weaponPosition.x, Math.min(1, d * 55));
			arme.position.y = (float) Utils.lerpD((double) arme.position.y, weaponPosition.y, Math.min(1, d * 55));
			arme.rotation = Utils.lerpAngle(arme.rotation, aimInput, Math.min(1, d * 20));//rotation indépendante
//			arme.rotation = Utils.lerpAngle(arme.rotation, new Vec2f(arme.position.x-position.x, arme.position.y-position.y).angle(), Math.min(1, d * 10));
		}
		for (Entity e : children.values()) {
			if (e instanceof Spatial && !(e instanceof Weapon))
				((Spatial) e).position.set(position);
			e.step(d);
		}
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