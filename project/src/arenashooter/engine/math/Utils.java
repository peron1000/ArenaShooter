package arenashooter.engine.math;

public final class Utils {
	//This class cannot be instantiated
	private Utils() {}
	
	/** 2*PI */
	public static final double PI2 = 2d*Math.PI;
	
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
	
//	/**
//	 * Smoothstep
//	 * Implemented from <a href="https://en.wikipedia.org/wiki/Smoothstep">Wikipedia</a>
//	 * @param edge0
//	 * @param edge1
//	 * @param x
//	 * @return
//	 */
//	public static double smoothstepD(double edge0, double edge1, double x) {
//		// Scale, bias and saturate x to 0..1 range
//		x = clampD((x - edge0) / (edge1 - edge0), 0.0, 1.0); 
//		// Evaluate polynomial
//		return x * x * (3 - 2 * x);
//	}
	
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
	
	/**
	 * Linear interpolation between a and b
	 * @param a
	 * @param b
	 * @param f
	 * @return
	 */
	public static float lerpF( float a, float b, double f ) {
		return (float)(a + (b-a)*f);
	}
	
	/**
	 * Linear interpolation between two angles (in radians).<br/>
	 * This will normalize the given angles
	 * @param a
	 * @param b
	 * @param f
	 * @return
	 */
	public static double lerpAngle( double a, double b, double f ) { //TODO: Test when a and b are PI/2 radians apart!!!
//		if( Math.abs(a-b) >= Math.PI ) {
//			if(a>b)
//				a = normalizeAngle(a) - PI2;
//			else
//				b = normalizeAngle(b) - PI2;
//		}
//		
//		return lerpD(a, b, f);
		double cos = (1-f)*Math.cos(a) + f*Math.cos(b);
		double sin = (1-f)*Math.sin(a) + f*Math.sin(b);
		return Math.atan2(sin, cos);
	}
	
	
	/**
	 * Normalize an angle (in radians) between -PI and +PI
	 * @param a
	 * @return
	 */
	public static double normalizeAngle(double a) { //TODO: Test
		return a-PI2*Math.floor( (a + Math.PI)/PI2 );
	}
}
