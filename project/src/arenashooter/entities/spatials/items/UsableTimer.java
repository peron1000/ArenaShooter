package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Character;

public class UsableTimer extends Item {

	/** Time in between attacks */
	protected Timer timerCooldown = null;

	public UsableTimer(Vec2f position, String name, double weight, String pathSprite, Vec2f handPosL, Vec2f handPosR,
			String soundPickup, double fireRate, int duration, String animPath, double warmup, String soundWarmup,
			String soundFire) {
		super(position, name, weight, pathSprite, handPosL, handPosR, soundPickup);
		timerCooldown = new Timer(fireRate);
		timerCooldown.attachToParent(this, timerCooldown.genName());
	}

	public void attackStart() {
		timerCooldown.setIncreasing(true);
		timerCooldown.setProcessing(true);
	}

	public void attackStop() {
		timerCooldown.setIncreasing(false);
	}

	public void step(double d) {
		if (timerCooldown.isOver()) {
			timerCooldown.restart();

		}
		Vec2f targetOffSet = Vec2f.rotate(new Vec2f(50, 0), rotation);
		localPosition.x = (float) Utils.lerpD((double) localPosition.x, targetOffSet.x, Math.min(1, d * 55));
		localPosition.y = (float) Utils.lerpD((double) localPosition.y, targetOffSet.y, Math.min(1, d * 55));
		if (isEquipped()) {
			rotation = Utils.lerpAngle(rotation, ((Character) parent).aimInput, Math.min(1, d * 17));
			getSprite().rotation = rotation;
		}

		super.step(d);
	}
}
