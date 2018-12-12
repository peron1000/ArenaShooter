package arenashooter.engine.math;

public final class Utils {
	//This class cannot be instantiated
	private Utils() {}
	
	/**
	 * Clamps an int in a range
	 * @param a
	 * @param min
	 * @param max
	 * @return a clamped between min and max
	 */
	public static int clampI( int a, int min, int max ) {
		return Math.min(Math.max( min, a ), max);
	}
	
	/**
	 * Clamps a double in a range
	 * @param a
	 * @param min
	 * @param max
	 * @return a clamped between min and max
	 */
	public static double clampD( double a, double min, double max ) {
		return Math.min(Math.max( min, a ), max);
	}
	
	/**
	 * Clamps a double in a range
	 * @param a
	 * @param min
	 * @param max
	 * @return a clamped between min and max
	 */
	public static float clampF( float a, float min, float max ) {
		return Math.min(Math.max( min, a ), max);
	}
	
	/**
	 * Linear interpolation between a and b
	 * @param a
	 * @param b
	 * @param f
	 * @return
	 */
	public static double lerpD( double a, double b, double f ) {
		return a + (b-a)*f;
	}
	
	/**
	 * Linear interpolation between a and b
	 * @param a
	 * @param b
	 * @param f
	 * @return
	 */
	public static float lerpF( float a, float b, float f ) {
		return a + (b-a)*f;
	}
}
