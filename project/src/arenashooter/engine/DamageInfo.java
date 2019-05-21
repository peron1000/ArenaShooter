package arenashooter.engine;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Entity;

public class DamageInfo {
	/** Base damage */
	public final float damage;
	public final DamageType dmgType;
	/** Direction of damages, used to apply impulses */
	public final Vec2f direction;
	/** Entity causing damage, Character for bullets and other weapon damages */
	public final Entity instigator;
	
	public DamageInfo(float damage, DamageType dmgType, Vec2f direction, Entity instigator) {
		this.damage = damage;
		this.dmgType = dmgType;
		this.direction = direction.clone();
		this.instigator = instigator;
	}

}
