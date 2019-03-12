package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Character;

public abstract class Weapon extends Item {

	public Vec2f handPosL = null;
	public Vec2f handPosR = null;
	protected float damage = 0f;
	/** Time in between attacks */
	protected Timer timerCooldown = new Timer(0.15);

	public Weapon(Vec2f position, String itemSprite, float damage) {
		super(position, itemSprite);
		this.damage = damage;
		timerCooldown.attachToParent(this, timerCooldown.genName());
	}

	public void attackStart() {
		timerCooldown.setIncreasing(true);
		timerCooldown.setProcessing(true);
		
	}

	public void attackStop() {

	}

	public void step(double d) {
		Vec2f targetOffSet = Vec2f.rotate(new Vec2f(50, 0), rotation);
		localPosition.x = (float) Utils.lerpD((double) localPosition.x, targetOffSet.x, Math.min(1, d * 55));
		localPosition.y = (float) Utils.lerpD((double) localPosition.y, targetOffSet.y, Math.min(1, d * 55));
		if(isEquipped())
			rotation = Utils.lerpAngle(rotation, ((Character) parent).aimInput, Math.min(1, d * 17));
			
		super.step(d);
	}
}
