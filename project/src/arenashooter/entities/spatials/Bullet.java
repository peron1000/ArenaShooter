package arenashooter.entities.spatials;

import arenashooter.engine.DamageInfo;
import arenashooter.engine.DamageType;
import arenashooter.engine.audio.SoundSourceMulti;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.ShapeDisk;

public class Bullet extends Projectile {

	static SoundSourceMulti sndImpact = new SoundSourceMulti("data/sound/Ptou.ogg", 8, .8f, 1.2f, true);

	public Bullet(Vec2f position, Vec2f vel, float damage) {
		super(position, new RigidBody(new ShapeDisk(.25), position, 0, CollisionFlags.PROJ, 1, 1));
		
		getBody().setBullet(true);
		getBody().setIsSensor(true);
		
		this.vel = vel.clone();

		this.damage = damage;
		rotation = vel.angle();

		Sprite sprite = new Sprite(getWorldPos(), "data/sprites/Bullet.png");
		sprite.size = new Vec2f(sprite.getTexture().getWidth()*.018, sprite.getTexture().getHeight()*.018);
		sprite.rotation = rotation;
		sprite.getTexture().setFilter(false);
		sprite.rotationFromParent = false;
		sprite.attachToParent(this, "bul_Sprite");

		sndImpact.setVolume(.15f);
	}

	@Override
	public void impact(Spatial other) {
		if(other == shooter) return; //Ignore instigator
		
		other.takeDamage(new DamageInfo(damage, DamageType.BULLET, Vec2f.normalize(vel), shooter));
		
		sndImpact.play(getWorldPos());
		
		detach();
	}

	public void step(double d) {
		if(getBody().getBody() != null) 
			getBody().getBody().setGravityScale(0);
		
		getBody().setLinearVelocity(vel);
		
		if (Math.abs(getWorldPos().x) > 1000 || Math.abs(getWorldPos().y) > 1000) {
			detach();
		}
		
		((Spatial) getChildren().get("bul_Sprite")).rotation = vel.angle();

		super.step(d);
	}
}
