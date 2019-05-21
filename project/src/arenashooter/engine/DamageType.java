package arenashooter.engine;

public enum DamageType {
	/** Used to insta-kill players by non-gameworld causes, should always result in death */
	MISC_ONE_SHOT, 
	/** Caused by the damaged entity falling out of bounds, should always result in death */
	OUT_OF_BOUNDS, 
	/** Caused by crushing or violent impact */
	PHYSICS, 
	/** Punch or melee weapon */
	MELEE, 
	/** Bullet-based weapon */
	BULLET, 
	FIRE, 
	EXPLOSION
}
