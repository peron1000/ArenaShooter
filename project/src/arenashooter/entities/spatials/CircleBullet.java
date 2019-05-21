package arenashooter.entities.spatials;

import arenashooter.engine.DamageInfo;
import arenashooter.engine.DamageType;
import arenashooter.engine.audio.SoundSourceMulti;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.ShapeDisk;

public class CircleBullet extends Projectile {
	
	private boolean sens = true;
	
	private double movementTime = 0;
	
	private Vec2f direction;
	private Vec2f finalVel = new Vec2f();
	private static float frequency = 45, amplitude = 10;
	
	static SoundSourceMulti sndImpact = new SoundSourceMulti("data/sound/slap.ogg", 10, .8f, 1.2f, true);

	public CircleBullet(Vec2f position, Vec2f vel, float damage, boolean sens) {
		super(position, new RigidBody(new ShapeDisk(.25), position, 0, CollisionFlags.PROJ, 1, 1));
		
		getBody().setBullet(true);
		
		this.sens = sens;
		movementTime += (Math.random()-0.5)/2;
		
		this.vel = vel.clone();
		direction = Vec2f.fromAngle(vel.angle());

		this.damage = damage;

		Sprite sprite = new Sprite(getWorldPos(), "data/sprites/Ion_Bullet.png");
		sprite.size = new Vec2f(sprite.getTexture().getWidth()*.018, sprite.getTexture().getHeight()*.018);
		sprite.rotation = rotation;
		sprite.getTexture().setFilter(false);
		sprite.useTransparency = true;
		sprite.rotationFromParent = false;
		sprite.attachToParent(this, "bul_Sprite");

		sndImpact.setVolume(.15f);
	}

	@Override
	public void impact(Spatial other) {
		other.takeDamage(new DamageInfo(damage, DamageType.BULLET, vel, shooter));
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
		
		if (Math.abs(getWorldPos().x) > 1000 || Math.abs(getWorldPos().y) > 1000) {
			detach();
		}

		super.step(d);
	}
}
