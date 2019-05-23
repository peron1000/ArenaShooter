package arenashooter.engine.physic;

public enum CollisionCategory {
	CAT_LANDSCAPE(0b0000000000000001),
	CAT_CHARACTER(0b0000000000000010),
	CAT_ITEM     (0b0000000000000100),
	CAT_PROJ     (0b0000000000001000),
	CAT_RIGIDBODY(0b0000000000010000),
	CAT_CORPSE   (0b0000000000100000);
	
	public final int bits;
	
	private CollisionCategory(int bits) {
		this.bits = bits;
	}
}
