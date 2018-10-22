package arenashooter.engine.math;

public class Utils {
	public static int clampI( int a, int min, int max ) {
		return Math.min(Math.max( min, a ), max);
	}
	
	public static double clampD( double a, double min, double max ) {
		return Math.min(Math.max( min, a ), max);
	}
	
	public static double lerpD( double a, double b, double f ) {
		return a + (b-a)*f;
	}
}
