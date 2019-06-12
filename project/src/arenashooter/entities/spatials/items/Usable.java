package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Character;

public class Usable extends Item {

	/** Time in between attacks */
	protected Timer timerCooldown = null;
	protected double fireRate = 0;
	protected int uses = 0;
	protected String animPath = "";
	protected double warmup = 0;
	/**
	 * @return the warmup
	 */
	public double getWarmup() {
		return warmup;
	}

	protected String soundWarmup = "";
	protected String soundFire = "";

	public Usable(Vec2f localPosition, String name, double weight , String pathSprite, Vec2f handPosL, Vec2f handPosR, String soundPickup, double fireRate, int uses, String animPath,
			double warmup, String soundWarmup, String soundFire) {
		super(localPosition, name, weight, pathSprite, handPosL, handPosR, soundPickup);
		timerCooldown = new Timer(fireRate);
		timerCooldown.attachToParent(this, timerCooldown.genName());
		this.fireRate = fireRate;
		this.setUses(uses);
		this.setAnimPath(animPath);
		this.warmup = warmup;
		this.setSoundWarmup(soundWarmup);
		this.setSoundFire(soundFire);
	}

	/**
	 * @return the fireRate
	 */
	public double getFireRate() {
		return fireRate;
	}

	public void attackStart() {
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
		Usable clone = new Usable(localPosition, this.genName(), getWeight(), getPathSprite(), handPosL, handPosL, soundPickup, fireRate, getUses(), getAnimPath(), fireRate, getAnimPath(), getAnimPath()) {
		};
		return clone;
	}

	public int getUses() {
		return uses;
	}

	public void setUses(int uses) {
		this.uses = uses;
	}

	public String getAnimPath() {
		return animPath;
	}

	public void setAnimPath(String animPath) {
		this.animPath = animPath;
	}

	public String getSoundWarmup() {
		return soundWarmup;
	}

	public void setSoundWarmup(String soundWarmup) {
		this.soundWarmup = soundWarmup;
	}

	public String getSoundFire() {
		return soundFire;
	}

	public void setSoundFire(String soundFire) {
		this.soundFire = soundFire;
	}
}
