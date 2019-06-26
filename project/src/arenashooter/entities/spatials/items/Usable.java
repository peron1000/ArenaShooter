package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Character;

public class Usable extends Item {

	/** Time in between attacks */
	protected double fireRate = .1;
	protected Timer timerCooldown = new Timer(fireRate);
	protected int uses = 10;
	protected String animPath = "";
	protected double warmup = .1;
	protected String soundWarmup = "";
	protected String soundFire = "";
	protected boolean demo = false;

	public Usable(Vec2f localPosition, String name, double weight, String pathSprite, Vec2f handPosL, Vec2f handPosR,
			Vec2f size, String soundPickup, double fireRate, int uses, String animPath, double warmup,
			String soundWarmup, String soundFire) {
		super(localPosition, name, weight, pathSprite, handPosL, handPosR, size, soundPickup);

		// Cooldown
		timerCooldown = new Timer(fireRate);
		timerCooldown.attachToParent(this, timerCooldown.genName());
		this.timerCooldown.setIncreasing(true);
		this.timerCooldown.setProcessing(true);

		this.fireRate = fireRate;
		this.uses = uses;
		this.animPath = animPath;
		this.warmup = warmup;
		this.soundWarmup = soundWarmup;
		this.setSoundFire(soundFire);
	}

	/**
	 * Constructor for the Editor to avoid a new Item creation for each change state
	 * 
	 * @author Nathan
	 * @param sprite
	 */
	public Usable(String sprite) {
		super(sprite);

		// Cooldown
		timerCooldown.attachToParent(this, timerCooldown.genName());
		this.timerCooldown.setIncreasing(true);
		this.timerCooldown.setProcessing(true);
	}

	/**
	 * @return the fireRate
	 */
	public double getFireRate() {
		return fireRate;
	}

	/**
	 * @return the uses
	 */
	public int getUses() {
		return uses;
	}

	/**
	 * @return the animPath
	 */
	public String getAnimPath() {
		return animPath;
	}

	/**
	 * @return the warmup
	 */
	public double getWarmup() {
		return warmup;
	}

	/**
	 * @return the soundWarmup
	 */
	public String getSoundWarmup() {
		return soundWarmup;
	}

	/**
	 * @return the soundFire
	 */
	public String getSoundFire() {
		return soundFire;
	}

	/**
	 * @param soundFire the soundFire to set
	 */
	public void setSoundFire(String soundFire) {
		this.soundFire = soundFire;
	}

	public void attackStart(boolean demo) {
		this.demo = demo;
		timerCooldown.setIncreasing(true);
		timerCooldown.setProcessing(true);
	}

	public void attackStop() {
		timerCooldown.setIncreasing(false);
	}

	public void step(double d) {
		Vec2f targetOffSet = Vec2f.rotate(new Vec2f(0, 0), getWorldRot());
		localPosition.x = (float) Utils.lerpD((double) localPosition.x, targetOffSet.x, Math.min(1, d * 55));
		localPosition.y = (float) Utils.lerpD((double) localPosition.y, targetOffSet.y, Math.min(1, d * 55));
		if (isEquipped()) {
			localRotation = Utils.lerpAngle(localRotation, ((Character) getParent()).aimInput, Math.min(1, d * 17));
//			getSprite().localRotation = getWorldRot();
		}
		super.step(d);
	}

	@Override
	public Usable clone() {
		Usable clone = new Usable(localPosition, this.genName(), weight, pathSprite, handPosL, handPosR, extent,
				soundPickup, fireRate, uses, animPath, fireRate, animPath, animPath) {
		};
		return clone;
	}
}
