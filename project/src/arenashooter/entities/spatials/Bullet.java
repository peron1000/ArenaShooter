package arenashooter.entities.spatials;

import java.util.LinkedList;

import arenashooter.engine.audio.SoundSourceMulti;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Entity;

public class Bullet extends Projectile {

	static SoundSourceMulti sndImpact = new SoundSourceMulti("data/sound/Ptou.ogg", 10, .8f, 1.2f, true);

	public Bullet(Vec2f position, Vec2f vel, float damage) {
		this.parentPosition = Vec2f.add(position.clone(), this.vel);
		this.vel = vel.clone();

		this.damage = damage;
		rotation = vel.angle();

		collider = new Collider(this.pos(), new Vec2f(.5, .5));
		collider.attachToParent(this, "collider");

		Sprite bul = new Sprite(pos(), "data/sprites/Bullet.png");
		bul.size = new Vec2f(bul.getTexture().getWidth()*.018, bul.getTexture().getHeight()*.018);
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
				for (Entity coll : ((Plateform) bump).getChildren().values()) {
					if (coll instanceof Collider) {
						Collider c = (Collider) coll;
						if (c.isColliding(collider)) {
							sndImpact.play(pos());
							detach();
							destroyed = true;
							break;
						}
					}
				}
			}
			if (bump instanceof Character && !isShooter(((Character) bump))) {
				for (Entity coll : ((Character) bump).getChildren().values()) {
					if (coll instanceof Collider) {
						Collider c = (Collider) coll;
						if (c.isColliding(collider)) {
							sndImpact.play(pos());
							((Character) bump).takeDamage(damage, vel.x > 0);
							detach();
							destroyed = true;
							break;
						}
					}
				}
			}

			if (destroyed)
				break;
		}

		parentPosition.add(Vec2f.multiply(vel, (float) d));

		((Spatial) getChildren().get("bul_Sprite")).parentPosition = pos();
		((Spatial) getChildren().get("collider")).parentPosition = pos();
		((Spatial) getChildren().get("bul_Sprite")).rotation = rotation;

		super.step(d);
	}
}
