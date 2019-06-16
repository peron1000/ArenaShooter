package arenashooter.entities.spatials;

import arenashooter.engine.DamageInfo;
import arenashooter.engine.DamageType;
import arenashooter.engine.audio.Audio;
import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.entities.Entity;
import arenashooter.entities.Timer;

public class Barrel extends RigidBodyContainer {
	private float damage = 75;
	private double health = 5;
	private Mesh barrelMesh;
	private Timer detonTimer = new Timer(4.25);
	private Entity instigator;
	private double timeSinceLastSpark = 0;
	double timeUntilNewSpark = 0.2;

	public Barrel(Vec2f worldPosition) {
		super(new RigidBody(new ShapeBox(new Vec2f(0.75, 1)), worldPosition, 0, CollisionFlags.RIGIDBODY, 3, 1));

		barrelMesh = new Mesh(new Vec3f(), "data/meshes/barrels/barrel_01.obj");
		barrelMesh.attachToParent(this, "Barrel_Mesh");
		barrelMesh.scale.set(.95, .95, .95);
		Quat.fromEuler(Math.random()*2*Math.PI, 0, 0, barrelMesh.localRotation);

		detonTimer.attachToParent(this, "Detonation_Timer");
		detonTimer.reset();
	}

	public float takeDamage(DamageInfo info) {
		applyImpulse(Vec2f.multiply(info.direction, info.damage));
		Audio.playSound2D("data/sound/nid.ogg", AudioChannel.SFX, 1, (float) (0.95 + Math.random() * 0.1),
				localPosition);

		health -= info.damage;

		if (!detonTimer.isProcessing() && health <= 0) {
			this.instigator = info.instigator;
			detonTimer.setProcessing(true);
		} else if(detonTimer.isProcessing()) {
			detonTimer.setValue(detonTimer.getValue() + 0.15);
		}

		// Destroy when out of bounds
		if (info.dmgType == DamageType.OUT_OF_BOUNDS) {
			if (ignoreKillBounds)
				return 0;
			else
				detach();
		}
		return 0;
	}

	public void detonate() {
		Explosion explosion = new Explosion(getWorldPos(),
				new DamageInfo(damage, DamageType.EXPLOSION, new Vec2f(), 5f, instigator), 10);
		explosion.attachToParent(getArena(), explosion.genName());
		Particles particles = new Particles(getWorldPos(), "data/particles/fire_sparkles.xml");
		particles.attachToParent(getArena(), "fire_particles" + genName());
		detach();
	}

	public void step(double d) {
		if (detonTimer.isOver()) {
			detonate();
		} else if(detonTimer.isProcessing()) {
			timeSinceLastSpark += d;
			for(int i = 0 ; timeSinceLastSpark >= timeUntilNewSpark && i < timeSinceLastSpark/timeUntilNewSpark ; i++) {//new Particle every 'timeUntilNewSpark' s
				Particles particles = new Particles(getWorldPos(), "data/particles/fire_sparkles.xml");
				particles.attachToParent(getArena(), "fire_particles" + genName());
				Audio.playSound2D("data/sound/Tic.ogg", AudioChannel.SFX, 1, (float)(.1 + detonTimer.getValueRatio()), localPosition);
				timeSinceLastSpark -= timeUntilNewSpark;
				timeUntilNewSpark = 0.05+Math.random()*0.20;
			}
		}
		super.step(d);
	}

}
