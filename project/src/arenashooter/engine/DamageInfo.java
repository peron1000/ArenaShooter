package arenashooter.engine;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec2fi;
import arenashooter.entities.Entity;

public class DamageInfo {
	/** Base damage */
	public final float damage;
	public final DamageType dmgType;
	/** Direction of damages, used to apply impulses */
	public final Vec2fi direction;
	/** Force of the impulse */
	public final float impulse;
	/** Entity causing damage, Character for bullets and other weapon damages */
	public final Entity instigator;
	
	/**
	 * 
	 * @param damage base damage
	 * @param dmgType
	 * @param direction unit vector (or 0,0 if non-oriented) indicating the direction of the damage (for example: normalized bullet velocity)
	 * @param instigator entity responsible for damage
	 */
	public DamageInfo(float damage, DamageType dmgType, Vec2fi direction, float impulse, Entity instigator) {
		this.damage = damage;
		this.dmgType = dmgType;
		this.direction = new Vec2f(direction);
		this.impulse = impulse;
		this.instigator = instigator;
	}
	
	@Override
	public DamageInfo clone() {
		return new DamageInfo(damage, dmgType, direction, impulse, instigator);
	}

}
