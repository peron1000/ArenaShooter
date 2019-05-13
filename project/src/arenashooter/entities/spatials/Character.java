package arenashooter.entities.spatials;

import arenashooter.engine.Profiler;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.ImpactOld;
import arenashooter.entities.Controller;
import arenashooter.entities.Entity;
//import arenashooter.entities.Map;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.items.Item;
import arenashooter.entities.spatials.items.Usable;
import arenashooter.game.CharacterInfo;
import arenashooter.game.GameMaster;

public class Character extends Spatial {

	public Controller controller = null;
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

		collider = new Collider(this.pos(), new Vec2f(25, 60));
		collider.attachToParent(this, "coll_Body");

		attack.attachToParent(this, "attack timer");

		CharacterSprite skeleton = new CharacterSprite(this.pos(), charInfo);
		skeleton.attachToParent(this, "skeleton");

		SoundEffect jumpSound = new SoundEffect(this.pos(), "data/sound/jump.ogg", 2);
		jumpSound.setVolume(.7f);
		jumpSound.attachToParent(this, "snd_Jump");

		SoundEffect punchHitSound = new SoundEffect(this.pos(), "data/sound/snd_Punch_Hit2.ogg", 2);
		punchHitSound.setVolume(.7f);
		punchHitSound.attachToParent(this, "snd_Punch_Hit");
	}

	public void jump(int saut) {
		if (!isOnGround)
			return;
		vel.y = -saut;
		((SoundEffect) getChildren().get("snd_Jump")).play();
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
		boolean hasArmor = getChildren().containsKey("Item_Armor");

		if (!hasWeapon || !hasArmor) {
			for (Entity e : GameMaster.gm.getEntities()) {
				if (!hasWeapon && e instanceof Usable) {
					Usable usable = (Usable) e;
					float xDiff = Math.abs(pos().x - usable.pos().x);
					float yDiff = Math.abs(pos().y - usable.pos().y);
					if (xDiff < 175 && yDiff < 175)
						arme = usable;
				}
			}

			if (arme != null) {
				arme.attachToParent(this, "Item_Weapon");
				SoundEffect soundPickup = arme.pickup;
				soundPickup.play();
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

	public void death() {
		// TODO: Effects
		health = 0;
		dropItem();
		if (controller != null)
			controller.death();
		detach();
//		health = healthMax;
//		position = new Vec2f(spawn.x, spawn.y);
//		vel = new Vec2f();
	}

	@Override
	public void step(double d) {

		Profiler.startTimer(Profiler.PHYSIC);

		vel.x = (float) Utils.lerpD( vel.x, movementInput * 1500, Utils.clampD(d * (isOnGround ? 10 : 10), 0, 1) );
		if (!isOnGround)
			vel.y += Math.min(9.807 * 800 * d, 2000);

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

		CharacterSprite skeleton = ((CharacterSprite) getChildren().get("skeleton"));
		if (skeleton != null) {
			skeleton.setLookRight(lookRight);
		}

		if (Math.abs(pos().x) > 10000 || Math.abs(pos().y) > 10000) {
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