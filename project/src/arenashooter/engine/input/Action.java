package arenashooter.engine.input;

/**
 * Input actions
 */
public enum Action {
	JUMP(0), ATTACK(1), GET_ITEM(2), DROP_ITEM(3), UI_LEFT(4), UI_RIGHT(5), UI_UP(6), UI_DOWN(7), UI_OK(8), UI_BACK(9) , UI_NathanTest(10) , UI_PAUSE(11);

	public final int id;

	private Action( int id ) {
		this.id = id;
	}
}