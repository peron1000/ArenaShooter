package arenashooter.entities.spatials;

import arenashooter.engine.audio.SoundSource;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Collider;
import arenashooter.entities.Entity;

public class Bullet extends Projectile {
	
	static SoundSource sndImpact = new SoundSource("data/sound/Ptou.ogg", 10, .8f, 1.2f, true);

	public Bullet(Vec2f position, Vec2f vel, float damage) {
		this.position = position.clone();
		this.vel = vel.clone();

		this.damage = damage;
		rotation = vel.angle();

		collider = new Collider(this.position, new Vec2f(16, 16));
		collider.attachToParent(this, "collider");

		Sprite bul = new Sprite(position, "data/sprites/Bullet.png");
		bul.size = new Vec2f(bul.tex.getWidth(), bul.tex.getHeight());
		bul.rotation = rotation;
		bul.attachToParent(this, "bul_Sprite");

		sndImpact.setVolume(.05f);
	}

	public void step(double d) {
		for (Entity bump : getParent().children.values()) {
			if (bump instanceof Plateform) {
				for (Entity coll : ((Plateform) bump).children.values()) {
					if (coll instanceof Collider) {
						Collider c = (Collider) coll;
						if (c.isColliding(collider)) {
							sndImpact.play(position);
							destroy();
						}
					}
				}
			}
			if (bump instanceof Character) {
				for (Entity coll : ((Character) bump).children.values()) {
					if (coll instanceof Collider) {
						Collider c = (Collider) coll;
						if (c.isColliding(collider)) {
							sndImpact.play(position);
							((Character) bump).takeDamage(damage, vel.x > 0);
							destroy();
						}
					}
				}
			}
		}

		position.add(Vec2f.multiply(vel, (float) d));

		((Spatial) children.get("bul_Sprite")).position = position;
		((Spatial) children.get("collider")).position = position;
		((Spatial) children.get("bul_Sprite")).rotation = rotation;

		super.step(d);
	}
}
