package arenashooter.entities.spatials;

import java.util.LinkedList;

import arenashooter.engine.audio.SoundSource;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Collider;
import arenashooter.entities.Entity;

public class CircleBullet extends Projectile {
	
	private boolean sens = true;
	
	private double movementTime = 0;
	
	static SoundSource sndImpact = new SoundSource("data/sound/slap.ogg", 10, .8f, 1.2f, true);

	public CircleBullet(Vec2f position, Vec2f vel, float damage, boolean sens) {
		this.position = Vec2f.add(position.clone(), this.vel);
		this.vel = vel.clone();
		movementTime += (Math.random()-0.5)/2;

		this.damage = damage;
		this.sens = sens;

		collider = new Collider(this.pos(), new Vec2f(16, 16));
		collider.attachToParent(this, "collider");

		sndImpact.setVolume(.15f);
		
		Sprite bul = new Sprite(pos(), "data/sprites/Ion_Bullet.png");
		bul.size = new Vec2f(bul.tex.getWidth(), bul.tex.getHeight());
		bul.rotation = rotation;
		bul.attachToParent(this, "bul_Sprite");
		bul.tex.setFilter(false);
		bul.useTransparency = true;
	}

	@Override
	public void step(double d) {

		movementTime += (sens ? d : -d);

		double sin = Math.sin(movementTime * 40);
		double cos = Math.cos(movementTime * 40);
		
		collider.localPosition.x = (float) (cos * 4);
		collider.localPosition.y = (float) (sin * 4);
		
		Sprite sprite = ((Sprite)children.get("bul_Sprite"));
		sprite.localPosition.x = (float) (-20 + cos * 40);
		sprite.localPosition.y = (float) (-20 + sin * 40);

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
