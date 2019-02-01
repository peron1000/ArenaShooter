package arenashooter.engine.input;

/**
 * Input axes
 */
public enum Axis {
	MOVE_X(0), MOVE_Y(1), AIM_X(2), AIM_Y(3);

	public final int id;

	private Axis( int id ) {
		this.id = id;
	}
}