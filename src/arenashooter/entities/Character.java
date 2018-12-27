package arenashooter.entities;

import arenashooter.engine.graphics.Texture;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;

public class Character extends Spatial {
	private static final float defaultDammage = 5;
	private float health, healthMax;
	private final Vec2f spawn;
	
	Vec2f vel = new Vec2f();
	Collider collider;
	boolean isOnGround = true;
	public float movementInput = 0;

	public Character(Vec2f position) {
		super(position);
		
		healthMax = 100;
		health = healthMax;
		spawn = position;
		
		rotation = 0;
		
		collider = new Collider(this.position, new Vec2f(50, 110));
		collider.attachToParent(this, "coll_Body");
		
		Sprite body = new Sprite(position, "data/UnMoineHD.png");
		body.size = new Vec2f(body.tex.getWidth() * 3, body.tex.getHeight() * 3);
		body.attachToParent(this, "body_Sprite");
		
		SoundEffect testSound = new SoundEffect( this.position, "data/sound/jump.ogg" );
		testSound.attachToParent(this, "snd_Jump");
	}

	public void jump(int saut) {
		if( !isOnGround ) return;
		isOnGround = false;
		vel.y = -saut;
		
		((SoundEffect)children.get("snd_Jump")).play();
		// TODO: collider
	}

	public void attack() {
		Texture chevre = Texture.loadTexture("data/Chevre2.png");
		chevre.setFilter(false);
		Sprite body = ((Sprite) children.get("body_Sprite"));
		((Sprite) children.get("body_Sprite")).tex = chevre;
		body.size = new Vec2f(body.tex.getWidth() * 3, body.tex.getHeight() * 3);
		collider.extent.y = body.size.y / 2;
		collider.extent.x = (body.size.x / 2) - 20;
		// TODO: attac
		for (Entity entity : Game.game.getMap().children.values()) {
			if(entity instanceof Character && entity != this) {
				Character c = (Character) entity;
				float xDiff = Math.abs(position.x - c.position.x);
				float yDiff = Math.abs(position.y - c.position.y);
				if(xDiff < 300 && yDiff < 300) {
					c.takeDamage(defaultDammage);
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
						if (collider.getYBottom() >= c.getYTop() && collider.getYBottom() < c.getYBottom()
								&& Collider.isX1IncluedInX2(collider, c))
							isOnGround = true;
					}
				}
			}
		}
		
		if (movementInput > 0)
			((Sprite) children.get("body_Sprite")).flipX = false;
		else if (movementInput < 0)
			((Sprite) children.get("body_Sprite")).flipX = true;

		position.add(Vec2f.multiply(vel, (float) d));
		
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
