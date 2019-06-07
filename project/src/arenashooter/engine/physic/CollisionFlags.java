package arenashooter.engine.physic;

import static arenashooter.engine.physic.CollisionCategory.*;

public enum CollisionFlags {
	NONE     (CAT_NONE),
	LANDSCAPE(CAT_LANDSCAPE, CAT_CHARACTER, CAT_ITEM, CAT_PROJ, CAT_RIGIDBODY, CAT_CORPSE), 
	CHARACTER(CAT_CHARACTER, CAT_LANDSCAPE, CAT_PROJ, CAT_RIGIDBODY, CAT_ARENA_KINEMATIC, CAT_CHARACTER), 
	ITEM     (CAT_ITEM, CAT_LANDSCAPE, CAT_RIGIDBODY, CAT_RIGIDBODY, CAT_ARENA_KINEMATIC), 
	PROJ     (CAT_PROJ, CAT_LANDSCAPE, CAT_CHARACTER, CAT_RIGIDBODY, CAT_ARENA_KINEMATIC), 
	RIGIDBODY(CAT_RIGIDBODY, CAT_LANDSCAPE, CAT_CHARACTER, CAT_ITEM, CAT_PROJ, CAT_RIGIDBODY, CAT_CORPSE, CAT_ARENA_KINEMATIC),
	CORPSE   (CAT_CORPSE, CAT_LANDSCAPE, CAT_RIGIDBODY, CAT_ARENA_KINEMATIC),
	EXPLOSION(CAT_EXPLOSION, CAT_LANDSCAPE, CAT_CHARACTER, CAT_ITEM, CAT_PROJ, CAT_RIGIDBODY, CAT_CORPSE),
	ARENA_KINEMATIC(CAT_ARENA_KINEMATIC, CAT_CHARACTER, CAT_ITEM, CAT_PROJ, CAT_RIGIDBODY, CAT_CORPSE);

	
	/** What this is */
	public final CollisionCategory category;
	/** What this interacts with */
	public final int maskBits;
	
	/**
	 * @param category
	 * @param masks categories this will react to
	 */
	private CollisionFlags(CollisionCategory category, CollisionCategory... masks) {
		this.category = category;
		int maskBitsTemp = 0;
		for(CollisionCategory mask : masks)
			maskBitsTemp = maskBitsTemp | mask.bits;
		maskBits = maskBitsTemp;
	}
}
