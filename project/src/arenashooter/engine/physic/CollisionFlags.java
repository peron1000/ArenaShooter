package arenashooter.engine.physic;

import static arenashooter.engine.physic.CollisionCategory.*;

public enum CollisionFlags {
	LANDSCAPE(CAT_LANDSCAPE, CAT_CHARACTER, CAT_ITEM, CAT_PROJ, CAT_RIGIDBODY, CAT_CORPSE), 
	CHARACTER(CAT_CHARACTER, CAT_LANDSCAPE, CAT_ITEM, CAT_PROJ, CAT_RIGIDBODY), 
	ITEM     (CAT_ITEM, CAT_LANDSCAPE, CAT_RIGIDBODY, CAT_RIGIDBODY), 
	PROJ     (CAT_PROJ, CAT_LANDSCAPE, CAT_CHARACTER, CAT_RIGIDBODY), 
	RIGIDBODY(CAT_RIGIDBODY, CAT_LANDSCAPE, CAT_CHARACTER, CAT_ITEM, CAT_PROJ, CAT_RIGIDBODY, CAT_CORPSE),
	CORSPE   (CAT_CORPSE, CAT_LANDSCAPE, CAT_RIGIDBODY);

	
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
