package arenashooter.entities;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;

public class Character extends Spatial {
	private static final float defaultDamage = 5;
	private float health, healthMax;
	private final Vec2f spawn;

	Vec2f vel = new Vec2f();
	Collider collider;
	boolean isOnGround = true;
	public float movementInput = 0;
	private boolean lookRight = true;

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

		SoundEffect jumpSound = new SoundEffect(this.position, "data/sound/jump.ogg");
		jumpSound.setVolume(.7f);
		jumpSound.attachToParent(this, "snd_Jump");
		
//		SoundEffect punchHitSound = new SoundEffect(this.position, "data/sound/punch_01.ogg");
//		punchHitSound.setVolume(.7f);
//		punchHitSound.attachToParent(this, "snd_Punch_Hit");
		
		SoundEffect punchHitSound = new SoundEffect(this.position, "data/sound/snd_Punch_Hit2.ogg");
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
		if (attack.isOver()) {
			attack.restart();
			
			CharacterSprite skeleton = ((CharacterSprite) children.get("skeleton"));
			if (skeleton != null) skeleton.punch();
			
			//TODO: attac
			for (Entity entity : Game.game.getMap().children.values()) {
				if (entity instanceof Character && entity != this) {
					Character c = (Character) entity;

					boolean isInFrontOfMe = false;
					if (skeleton != null) {
						if ((lookRight && collider.getXRight() < (c.collider.getXRight()+40))
								|| (!lookRight && collider.getXLeft() > (c.collider.getXLeft()-40))) {
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

	public float takeDamage(float damage, boolean droite) {// degats orientes
		float res = Math.min(damage, health);
		if (droite)
			vel.add(new Vec2f(500 * ((float) Math.log10(damage)), -800 * ((float) Math.log10(damage))));
		else
			vel.add(new Vec2f(-500 * ((float) Math.log10(damage)), -800 * ((float) Math.log10(damage))));

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

		vel.x = (float) Utils.lerpD(vel.x, movementInput * 1500, d * (isOnGround ? 10 : 2));
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

		//Animation
		if( movementInput > 0 )
			lookRight = true;
		else if( movementInput < 0 )
			lookRight = false;
		CharacterSprite skeleton = ((CharacterSprite) children.get("skeleton"));
		if (skeleton != null) {
			skeleton.setLookRight(lookRight);
		}

		if (Math.abs(position.x) > 10000 || Math.abs(position.y) > 10000) {
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
