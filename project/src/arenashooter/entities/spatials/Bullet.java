package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Collider;
import arenashooter.entities.Entity;
import arenashooter.entities.SoundEffect;

public class Bullet extends Projectile {

	public Bullet(Vec2f position, Vec2f vel) {
		this.position = position.clone();
		this.vel = vel.clone();

		damage = 10;
		rotation = 0;

		collider = new Collider(this.position, new Vec2f(16, 16));
		collider.attachToParent(this, "collider");

		Sprite bul = new Sprite(position, "data/sprites/Boulette.png");
		bul.size = new Vec2f(bul.tex.getWidth() * 3, bul.tex.getHeight() * 3);
		bul.attachToParent(this, "bul_Sprite");

		SoundEffect touche = new SoundEffect(this.position, "data/sound/Ptou.ogg");
		touche.setVolume(.7f);
		touche.attachToParent(this, "snd_touche");
	}

	public void step(double d) {
		for (Entity bump : getParent().children.values()) {
			if (bump instanceof Plateform) {
				for (Entity coll : ((Plateform) bump).children.values()) {
					if (coll instanceof Collider) {
						Collider c = (Collider) coll;
						if (c.isColliding(collider)) {
							((SoundEffect) children.get("snd_touche")).play();
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
							((SoundEffect) children.get("snd_touche")).play();
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

		super.step(d);
	}
}
