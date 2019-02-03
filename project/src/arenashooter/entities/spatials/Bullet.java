package arenashooter.entities.spatials;

import java.util.LinkedList;

import arenashooter.engine.audio.SoundSourceMulti;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Collider;
import arenashooter.entities.Entity;

public class Bullet extends Projectile {

	static SoundSourceMulti sndImpact = new SoundSourceMulti("data/sound/Ptou.ogg", 10, .8f, 1.2f, true);

	public Bullet(Vec2f position, Vec2f vel, float damage) {
		this.position = Vec2f.add(position.clone(), this.vel);
		this.vel = vel.clone();

		this.damage = damage;
		rotation = vel.angle();

		collider = new Collider(this.pos(), new Vec2f(16, 16));
		collider.attachToParent(this, "collider");

		Sprite bul = new Sprite(pos(), "data/sprites/Bullet.png");
		bul.size = new Vec2f(bul.tex.getWidth(), bul.tex.getHeight());
		bul.rotation = rotation;
		bul.attachToParent(this, "bul_Sprite");

		sndImpact.setVolume(.15f);
	}

	public void step(double d) {
		if (Math.abs(pos().x) > 10000 || Math.abs(pos().y) > 10000) {
			detach();
		}
		LinkedList<Entity> siblings = new LinkedList<>();
		siblings.addAll(siblings().values());
		boolean destroyed = false;
		for (Entity bump : siblings) {
			if (bump instanceof Plateform) {
				for (Entity coll : ((Plateform) bump).children.values()) {
					if (coll instanceof Collider) {
						Collider c = (Collider) coll;
						if (c.isColliding(collider)) {
							sndImpact.play(pos());
							destroy();
							destroyed = true;
							break;
						}
					}
				}
			}
			if (bump instanceof Character && !isShooter(((Character) bump))) {
				for (Entity coll : ((Character) bump).children.values()) {
					if (coll instanceof Collider) {
						Collider c = (Collider) coll;
						if (c.isColliding(collider)) {
							sndImpact.play(pos());
							((Character) bump).takeDamage(damage, vel.x > 0);
							destroy();
							destroyed = true;
							break;
						}
					}
				}
			}

			if (destroyed)
				break;
		}

		position.add(Vec2f.multiply(vel, (float) d));

		((Spatial) children.get("bul_Sprite")).position = pos();
		((Spatial) children.get("collider")).position = pos();
		((Spatial) children.get("bul_Sprite")).rotation = rotation;

		super.step(d);
	}
}
