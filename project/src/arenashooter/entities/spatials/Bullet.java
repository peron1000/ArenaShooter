package arenashooter.entities.spatials;

import arenashooter.engine.DamageInfo;
import arenashooter.engine.DamageType;
import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec2fi;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.ShapeDisk;
import arenashooter.game.Main;

public class Bullet extends Projectile {

	public Bullet(Vec2fi position, Vec2fi vel, float damage) {
		super(new RigidBody(new ShapeDisk(.25), position, vel.angle(), CollisionFlags.PROJ, 1, 1));
		
		getBody().setBullet(true);
		getBody().setIsSensor(true);
		
		this.vel = new Vec2f(vel);

		this.damage = damage;

		Sprite sprite = new Sprite(new Vec2f(), "data/sprites/Bullet.png");
		sprite.size = new Vec2f(sprite.getTexture().getWidth()*.018, sprite.getTexture().getHeight()*.018);
		sprite.getTexture().setFilter(false);
		sprite.attachToParent(this, "bul_Sprite");
	}

	@Override
	public void impact(Spatial other) {
		if(other == shooter) return; //Ignore instigator
		
		other.takeDamage(new DamageInfo(damage, DamageType.BULLET, Vec2f.normalize(vel), 5, shooter));
		
		Main.getAudioManager().playSound2D("data/sound/Ptou.ogg", AudioChannel.SFX, .15f, Utils.lerpF(.8f, 1.2f, Math.random()), getWorldPos());
		
		detach();
	}

	public void step(double d) {
		if(getBody().getBody() != null)
			getBody().getBody().setGravityScale(0);
		
		getBody().setLinearVelocity(vel);
		
		if (Math.abs(getWorldPos().x()) > 1000 || Math.abs(getWorldPos().y()) > 1000) {
			detach();
		}
		
//		((Spatial) getChild("bul_Sprite")).localRotation = vel.angle();

		super.step(d);
	}
}
