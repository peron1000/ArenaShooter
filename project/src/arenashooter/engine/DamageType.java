package arenashooter.engine;

public enum DamageType {
	/** Caused by the damaged entity falling out of bounds, should always result in death */
	OUTOFBOUNDS, 
	/** Caused by crushing or violent impact */
	PHYSICS, 
	/** Punch or melee weapon */
	MELEE, 
	/** Bullet-based weapon */
	BULLET, 
	FIRE, 
	EXPLOSION
}
