package arenashooter.engine.physic;

public enum CollisionCategory {
	CAT_LANDSCAPE(0b000001),
	CAT_CHARACTER(0b000010),
	CAT_ITEM(0b000100),
	CAT_PROJ(0b001000),
	CAT_RIGIDBODY(0b010000);
	
	public final int bits;
	
	private CollisionCategory(int bits) {
		this.bits = bits;
	}
}
