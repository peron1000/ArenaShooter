package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Collider;
import arenashooter.entities.spatials.SoundEffect;

public class Melee extends Usable {
	protected Timer fireRate = null;
	Collider collider;
	protected float damage = 10f;
	/** Time before the first bullet is fired */
	protected Timer timerWarmup = null;

	public Melee(Vec2f position, String name, double weight, String pathSprite, Vec2f handPosL, Vec2f handPosR,
			String soundPickup, double fireRate, int uses, String animPath, double warmupDuration, String soundWarmup,
			String attackSound, float damage, double size) {
		super(position, name, weight, pathSprite, handPosL, handPosR, soundPickup, fireRate, uses, animPath,
				warmupDuration, soundWarmup, attackSound);
		this.fireRate = new Timer(fireRate);

		SoundEffect pickup = new SoundEffect(position, "data/sound/" + soundPickup + ".ogg", 2);
		pickup.attachToParent(this, "snd_Pickup");

		SoundEffect attack = new SoundEffect(position, "data/sound/" + attackSound + ".ogg", 2);
		attack.attachToParent(this, "snd_attack");

		SoundEffect warmup = new SoundEffect(position, "data/sound/" + soundWarmup + ".ogg", 2);
		warmup.attachToParent(this, "snd_Warmup");
		this.timerWarmup = new Timer(warmupDuration);
		this.timerWarmup.attachToParent(this, this.timerWarmup.genName());
	}

	@Override
	public void step(double d) {
		if (timerWarmup.isOver()) {
			timerCooldown.restart();
		}
		super.step(d);
	}

	@Override
	protected void setLocalPositionOfSprite() {
		localPosition = Vec2f.rotate(new Vec2f(20, 0), rotation);
	}
	
	@Override
	public Melee clone(Vec2f position) {
		Melee clone = new Melee(position, this.genName(), weight, pathSprite, handPosL, handPosL, soundPickup, warmup, uses, animPath, warmup, animPath, animPath, damage, warmup) {
		};
		return clone;
	}
}
