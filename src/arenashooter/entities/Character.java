package arenashooter.entities;

import arenashooter.engine.graphics.Texture;
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
	
	//TODO: Temp sprite selector
	private static boolean chevre_chat = false;

	public Character(Vec2f position) {
		super(position);
		
		healthMax = 100;
		health = healthMax;
		spawn = position;
		
		rotation = 0;
		
		collider = new Collider(this.position, new Vec2f(25, 70));
		collider.attachToParent(this, "coll_Body");
		
		Sprite body = new Sprite(position, "data/sprites/UnMoineHD.png");
		body.size = new Vec2f(body.tex.getWidth() * 3, body.tex.getHeight() * 3);
//		body.attachToParent(this, "body_Sprite");
		
		String spriteFolder = chevre_chat ? "chevre_01" : "chat_01";
		chevre_chat = !chevre_chat;
		CharacterSprite skeleton = new CharacterSprite(this.position, "data/sprites/characters/"+spriteFolder);
		skeleton.attachToParent(this, "skeleton");
		
		SoundEffect jumpSound = new SoundEffect( this.position, "data/sound/jump.ogg" );
		jumpSound.setVolume(.7f);
		jumpSound.attachToParent(this, "snd_Jump");
	}

	public void jump(int saut) {
		if(!isOnGround) return;
		vel.y = -saut;
		((SoundEffect)children.get("snd_Jump")).play();
	}

	public void attack() {
		Sprite body = ((Sprite) children.get("body_Sprite"));
		if(body != null) {
			Texture chevre = Texture.loadTexture("data/sprites/Chevre2.png");
			chevre.setFilter(false);
			body.tex = chevre;
			body.size = new Vec2f(body.tex.getWidth() * 3, body.tex.getHeight() * 3);
			collider.extent.x = 25;
			collider.extent.y = 60;
		}
		// TODO: attac
		for (Entity entity : Game.game.getMap().children.values()) {
			if(entity instanceof Character && entity != this) {
				Character c = (Character) entity;
				float xDiff = Math.abs(position.x - c.position.x);
				float yDiff = Math.abs(position.y - c.position.y);
				if(xDiff < 300 && yDiff < 300) {
					c.takeDamage(defaultDamage);
					c.position.add(new Vec2f(0, -30));
				}
			}
		}
	}
	
	public float takeDamage( float damage ) {
		float res = Math.min(damage, health);
		
		health = Math.max(0, health-damage);
		
		if( health <= 0 ) death();
		
		return res;
	}
	
	private void death() {
		health = 0;
		//TODO: Effects
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
						if (collider.getYBottom()+(vel.y*d) >= c.getYTop() && collider.getYBottom()+(vel.y*d) < c.getYBottom()
								&& Collider.isX1IncluedInX2(collider, c))
							isOnGround = true;
					}
				}
			}
		}

		position.add(Vec2f.multiply(vel, (float) d));

		if(((Sprite) children.get("body_Sprite")) != null) {
			if (movementInput > 0)
				((Sprite) children.get("body_Sprite")).flipX = false;
			else if (movementInput < 0)
				((Sprite) children.get("body_Sprite")).flipX = true;
		}
		
		//Animation
		CharacterSprite skeleton = ((CharacterSprite)children.get("skeleton"));
		if(skeleton != null) {
			skeleton.onGround = isOnGround;
			skeleton.moveSpeed = vel.x;
			if(movementInput > 0)
				skeleton.lookRight = true;
			else if(movementInput < 0)
				skeleton.lookRight = false;
		}
		
		if(Math.abs(position.x) > 10000 || Math.abs(position.y) > 10000) {
			death();
		}
		
		super.step(d);
	}
	
	public float getHealth() { return health; }
	public float getHealthMax() { return healthMax; }
	public boolean isDead() { return health <= 0; }

	public Vec2f getSpawn() {
		return spawn;
	}
}
