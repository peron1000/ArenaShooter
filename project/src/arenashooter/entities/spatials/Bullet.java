package arenashooter.entities.spatials;

import arenashooter.engine.audio.SoundSourceMulti;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.ShapeDisk;

public class Bullet extends Projectile {

	static SoundSourceMulti sndImpact = new SoundSourceMulti("data/sound/Ptou.ogg", 10, .8f, 1.2f, true);

	public Bullet(Vec2f position, Vec2f vel, float damage) {
		super(position, new RigidBody(new ShapeDisk(.25), position, 0, CollisionFlags.PROJ, 1, 1));
		
		getBody().setBullet(true);
		
//		this.parentPosition = Vec2f.add(position.clone(), this.vel);
		this.vel = vel.clone();

		this.damage = damage;
		rotation = vel.angle();

//		collider = new Collider(this.getWorldPos(), new Vec2f(.5, .5));
//		collider.attachToParent(this, "collider");

		Sprite bul = new Sprite(getWorldPos(), "data/sprites/Bullet.png");
		bul.size = new Vec2f(bul.getTexture().getWidth()*.018, bul.getTexture().getHeight()*.018);
		bul.rotation = rotation;
		bul.getTexture().setFilter(false);
		bul.attachToParent(this, "bul_Sprite");

		sndImpact.setVolume(.15f);
	}

	@Override
	public void impact(Spatial other) {
		detach();
	}

	public void step(double d) {
		if(getBody().getBody() != null) 
			getBody().getBody().setGravityScale(0);
		
		getBody().setLinearVelocity(vel);
		
		if (Math.abs(getWorldPos().x) > 1000 || Math.abs(getWorldPos().y) > 1000) {
			detach();
		}
		
//		LinkedList<Entity> siblings = new LinkedList<>();
//		siblings.addAll(siblings().values());
//		boolean destroyed = false;
//		for (Entity bump : siblings) {
//			if (bump instanceof Plateform) {
//				for (Entity coll : ((Plateform) bump).getChildren().values()) {
//					if (coll instanceof Collider) {
//						Collider c = (Collider) coll;
//						if (c.isColliding(collider)) {
//							sndImpact.play(getWorldPos());
//							detach();
//							destroyed = true;
//							break;
//						}
//					}
//				}
//			}
//			if (bump instanceof Character && !isShooter(((Character) bump))) {
//				for (Entity coll : ((Character) bump).getChildren().values()) {
//					if (coll instanceof Collider) {
//						Collider c = (Collider) coll;
//						if (c.isColliding(collider)) {
//							sndImpact.play(getWorldPos());
//							((Character) bump).takeDamage(damage, vel.x > 0);
//							detach();
//							destroyed = true;
//							break;
//						}
//					}
//				}
//			}
//
//			if (destroyed)
//				break;
//		}

//		parentPosition.add(Vec2f.multiply(vel, (float) d));

//		((Spatial) getChildren().get("bul_Sprite")).parentPosition = getWorldPos();
//		((Spatial) getChildren().get("collider")).parentPosition = getWorldPos();
		((Spatial) getChildren().get("bul_Sprite")).rotation = vel.angle();

		super.step(d);
	}
}
