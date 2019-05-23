package arenashooter.entities.spatials;

import arenashooter.engine.audio.SoundSourceMulti;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.ShapeDisk;
import arenashooter.entities.Timer;

public class Grenade extends Projectile {

	private Timer grenadeTimer;
	
	private boolean launched = false;

	public Grenade(Vec2f position, Vec2f vel, float damage) {
		super(position, new RigidBody(new ShapeDisk(.25), position, 0, CollisionFlags.PROJ, 1, 1));
		
		getBody().setBullet(true);
		
		this.vel = vel.clone();

		this.damage = damage;
		rotation = vel.angle();

		Sprite sprite = new Sprite(getWorldPos(), "data/sprites/grenade_01.png");
		sprite.size = new Vec2f(sprite.getTexture().getWidth()*.018, sprite.getTexture().getHeight()*.018);
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
			System.out.println("Kaboom"); //TODO: Create explosion
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
