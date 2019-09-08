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

public class CircleBullet extends Projectile {
	
	private boolean sens = true;
	
	private double movementTime = 0;
	
	private Vec2f direction;
	private Vec2f finalVel = new Vec2f();
	private static float frequency = 45, amplitude = 10;
	
	public CircleBullet(Vec2fi position, Vec2fi vel, float damage, boolean sens) {
		super(new RigidBody(new ShapeDisk(.25), position, 0, CollisionFlags.PROJ, 1, 1));
		
		getBody().setBullet(true);
		getBody().setIsSensor(true);
		
		this.sens = sens;
		movementTime += (Math.random()-0.5)/2;
		
		this.vel = new Vec2f(vel);
		direction = Vec2f.fromAngle(vel.angle());

		this.damage = damage;

		Sprite sprite = new Sprite(new Vec2f(), "data/sprites/Ion_Bullet.png");
		sprite.size = new Vec2f(sprite.getTexture().getWidth()*.018, sprite.getTexture().getHeight()*.018);
		sprite.localRotation = getWorldRot();
		sprite.getTexture().setFilter(false);
		sprite.material.transparency = true;
		sprite.attachRot = false;
		sprite.attachToParent(this, "bul_Sprite");
	}

	@Override
	public void impact(Spatial other) {
		if(other == shooter) return; //Ignore instigator
		
		other.takeDamage(new DamageInfo(damage, DamageType.BULLET, direction, 5, shooter));
		
		Main.getAudioManager().playSound2D("data/sound/slap.ogg", AudioChannel.SFX, .15f, Utils.lerpF(.8f, 1.2f, Math.random()), getWorldPos());
		
		detach();
	}

	public void step(double d) {
		if(getBody().getBody() != null) 
			getBody().getBody().setGravityScale(0);
		
		movementTime += (sens ? d : -d);

		Vec2f.rotate(direction, movementTime * frequency, finalVel);
		finalVel.multiply(amplitude);
		finalVel.add(vel);
		
		getBody().setLinearVelocity(finalVel);
		
		if (Math.abs(getWorldPos().x()) > 1000 || Math.abs(getWorldPos().y()) > 1000) {
			detach();
		}

		super.step(d);
	}
}
