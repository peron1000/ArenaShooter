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
			String soundPickup, double cooldown, int uses, String animPath, double warmupDuration, String soundWarmup,
			String attackSound, float damage, double size) {
		super(position, name, weight, pathSprite, handPosL, handPosR, soundPickup, cooldown, uses, animPath,
				warmupDuration, soundWarmup, attackSound);

		SoundEffect pickup = new SoundEffect(position, "data/sound/" + soundPickup + ".ogg", 2);
		pickup.attachToParent(this, "snd_Pickup");

		SoundEffect attack = new SoundEffect(position, "data/sound/" + attackSound + ".ogg", 2);
		attack.attachToParent(this, "snd_attack");

		SoundEffect warmup = new SoundEffect(position, "data/sound/" + soundWarmup + ".ogg", 2);
		warmup.attachToParent(this, "snd_Warmup");
		
		// Warmup
		this.timerWarmup = new Timer(warmupDuration);
		this.timerWarmup.setIncreasing(false);
		this.timerWarmup.setProcessing(true);
		this.timerWarmup.attachToParent(this, "timer_warmup");

		// Cooldown
		this.timerCooldown = new Timer(cooldown);
		this.timerCooldown.setIncreasing(true);
		this.timerCooldown.setProcessing(true);
		this.timerCooldown.setValue(cooldown);
		this.timerCooldown.attachToParent(this, "timer_cooldown");
	}

	@Override
	public void attackStart() {
		timerWarmup.setIncreasing(true);
	}

	@Override
	public void attackStop() {
		timerWarmup.setIncreasing(false);
	}

	@Override
	public void step(double d) {
		if (timerWarmup.isIncreasing() && timerWarmup.isOver() && timerCooldown.isOver()) {
			timerCooldown.restart();
			Vec2f aim = Vec2f.fromAngle(rotation);
			this.position = aim;
		}
		super.step(d);
	}

	@Override
	protected void setLocalPositionOfSprite() {
		localPosition = Vec2f.rotate(new Vec2f(20, 0), rotation);
	}

	@Override
	public Melee clone(Vec2f position) {
		Melee clone = new Melee(position, this.genName(), weight, pathSprite, handPosL, handPosL, soundPickup, warmup,
				uses, animPath, warmup, animPath, animPath, damage, warmup) {
		};
		return clone;
	}
}
