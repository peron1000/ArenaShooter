package arenashooter.entities.spatials;

import arenashooter.engine.DamageInfo;
import arenashooter.engine.DamageType;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.engine.physic.shapes.ShapeDisk;
import arenashooter.entities.Timer;

public class Grenade extends Projectile {

	private Timer grenadeTimer;
	
	private boolean launched = false;

	public Grenade(Vec2f position, Vec2f vel, float damage) {
		super(position, new RigidBody(new ShapeBox(new Vec2f(.25, .15)), position, vel.angle(), CollisionFlags.PROJ, 1, 1));
		
		getBody().setBullet(true);
		
		this.vel = vel.clone();

		this.damage = damage;
		rotation = vel.angle();

		Sprite sprite = new Sprite(getWorldPos(), "data/sprites/grenade_01.png");
		sprite.size = new Vec2f(sprite.getTexture().getWidth()*.06, sprite.getTexture().getHeight()*.06);
		sprite.rotation = rotation;
		sprite.getTexture().setFilter(false);
		sprite.rotationFromParent = true;
		sprite.attachToParent(this, "bul_Sprite");

		grenadeTimer = new Timer(2);
		grenadeTimer.setIncreasing(true);
		grenadeTimer.setProcessing(true);
		grenadeTimer.attachToParent(this, "timer");
	}

	@Override
	public void impact(Spatial other) { } //Don't do anything special on impact, just bounce around

	public void step(double d) {
		if(grenadeTimer.isOver()) {
			Explosion explosion = new Explosion(getWorldPos(), new DamageInfo(150, DamageType.EXPLOSION, new Vec2f(), shooter), 40);
			explosion.attachToParent(getArena(), explosion.genName());
			detach();
		}
		
		if(!launched && getBody().getBody() != null) {
			launched = true;
			getBody().setLinearVelocity(vel);
		}
		
		if (Math.abs(getWorldPos().x) > 1000 || Math.abs(getWorldPos().y) > 1000)
			detach();

		super.step(d);
	}
}
