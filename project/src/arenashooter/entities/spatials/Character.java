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
import arenashooter.entities.spatials.items.Equipement;
import arenashooter.entities.spatials.items.Gun;
import arenashooter.game.Game;

public class Character extends Spatial {
	private static final float defaultDamage = 5;
	private float health, healthMax;
	private final Vec2f spawn;

	public Vec2f vel = new Vec2f();
	Collider collider;
	boolean isOnGround = true;
	public float movementInput = 0;
	public boolean lookRight = true;
	public Vec2f aimInput = new Vec2f();

	private Timer attack = new Timer(0.3);

	// TODO: Temp sprite selector
	private static boolean chevre_chat = false;

	public Character(Vec2f position) {
		super(position);

		healthMax = 100;
		health = healthMax;
		spawn = position;

		rotation = 0;

		collider = new Collider(this.position, new Vec2f(25, 60));
		collider.attachToParent(this, "coll_Body");

		attack.attachToParent(this, "attack timer");

		String spriteFolder = chevre_chat ? "chevre_01" : "chat_01";
		chevre_chat = !chevre_chat;
		CharacterSprite skeleton = new CharacterSprite(this.position, "data/sprites/characters/" + spriteFolder);
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

	public void attack() {
		if (children.get("Item_Weapon") != null) {
			((Gun) children.get("Item_Weapon")).fire(lookRight);
		} else if (attack.isOver()) {
			attack.restart();

			CharacterSprite skeleton = ((CharacterSprite) children.get("skeleton"));
			if (skeleton != null)
				skeleton.punch();

			for (Entity entity : Game.game.getMap().children.values()) {
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

	public void getItem() {
		Item arme = null;
		Item armure = null;

		if (!children.containsKey("Item_Weapon") || !children.containsKey("Item_Armor")) {
			for(Entity e : Game.game.getMap().children.values()) {
				if(e instanceof Gun) {
					Item weapon = (Gun)e;
					float xDiff = Math.abs(position.x - weapon.position.x);
					float yDiff = Math.abs(position.y - weapon.position.y);
					if (xDiff < 175 && yDiff < 175)
						arme = weapon;
				} else if(e instanceof Equipement) {
					Item item = (Equipement)e;
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
			vel.y += 9.807 * 800 * d;

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
		if (movementInput > 0)
			lookRight = true;
		else if (movementInput < 0)
			lookRight = false;
		CharacterSprite skeleton = ((CharacterSprite) children.get("skeleton"));
		if (skeleton != null) {
			skeleton.setLookRight(lookRight);
		}

		if (Math.abs(position.x) > 10000 || Math.abs(position.y) > 5000) {
			death();
		}

		// Updates Children, but Lerp for the Weapon instead of just giving the
		// position.
		if (children.get("Item_Weapon") instanceof Gun) {
			Gun arme = (Gun) children.get("Item_Weapon");
			boolean loin = arme.position.x > position.x + (lookRight ? 70 : -10)
					|| arme.position.x < position.x - (lookRight ? -10 : 70) || arme.position.y > position.y + 40
					|| arme.position.y < position.y - 20;
			if (lookRight) {
				arme.position.x = (float) Utils.lerpD(arme.position.x, position.x + 40, Math.min(1, d * (loin ? 60 : 20)));
				arme.position.y = (float) Utils.lerpD(arme.position.y, position.y + 10, Math.min(1, d * (loin ? 60 : 20)));
			} else {
				arme.position.x = (float) Utils.lerpD(arme.position.x, position.x - 40, Math.min(1 ,d * (loin ? 60 : 20)));
				arme.position.y = (float) Utils.lerpD(arme.position.y, position.y + 10, Math.min(1, d * (loin ? 60 : 20)));
			}
		}
		for (Entity e : children.values()) {
			if (e instanceof Spatial && !(e instanceof Gun))
				((Spatial) e).position.set(position);
			aimInput.print();
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
