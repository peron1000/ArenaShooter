package arenashooter.entities.spatials.items;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.Collider;
import arenashooter.entities.spatials.Sprite;

public class Melee extends Usable {
	protected Timer fireRate = null;
	Collider collider;
	protected float damage = 10f;

	protected AnimMelee animmelee = null;

	protected Sprite sprite = null;
	
	protected Timer timerWarmup = null;

	public Melee(Vec2f position, String name, double weight, String pathSprite, Vec2f handPosL, Vec2f handPosR,
			String soundPickup, double cooldown, int uses, String animPath, double warmupDuration, String soundWarmup,
			String attackSound, float damage, double size) {
		super(position, name, weight, pathSprite, handPosL, handPosR, soundPickup, cooldown, uses, animPath,
				warmupDuration, soundWarmup, attackSound);
		
		this.animmelee = new AnimMelee(new Vec2f(), this);
	}

	@Override
	public void detach() {
		animmelee.stopAnim();
		super.detach();
	}
	
	@Override
	public void attackStart() {
		animmelee.attachToParent(this, "anim_attack_01");
		animmelee.playAnim();
	}

	@Override
	public void attackStop() {
		if( getChild("anim_attack_01") instanceof AnimMelee ) {
			((AnimMelee)getChild("anim_attack_01")).stopAnim();
			getChild("anim_attack_01").detach();
		}
		
	} 

	@Override
	protected void setLocalPositionOfSprite() {
		if (getParent() instanceof Character) {
			if (((Character) getParent()).lookRight) {
				localPosition = new Vec2f(0.3, 0.1);
			} else {
				localPosition = new Vec2f(-0.3, 0.1);
			}
		}
	}

	@Override
	public Melee clone(Vec2f position) {
		Melee clone = new Melee(position, this.genName(), weight, pathSprite, handPosL, handPosL, soundPickup, warmup,
				uses, animPath, warmup, animPath, animPath, damage, warmup) {
		};
		return clone;
	}
}
