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
	public static double lerpAngle( double a, double b, double f ) {
//		if( Math.abs(a-b) >= Math.PI ) {
//			if(a>b)
//				a = normalizeAngle(a) - PI2;
//			else
//				b = normalizeAngle(b) - PI2;
//		}
//		
//		return lerpD(a, b, f);
		
		//Special case for a PI/2 difference
		if( Math.abs(normalizeAngle(a)+PI2 - normalizeAngle(b)) < .01) a+=.01;
		
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
	
	/**
	 * Based on <a href="https://github.com/javagl/Geom">this implementation</a>.
	 * @param p0
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param f
	 * @return
	 */
	public static Vec2f catmullRomSpline2f(Vec2f p0, Vec2f p1, Vec2f p2, Vec2f p3, double f) {
		double dx = p1.x-p0.x;
        double dy = p1.y-p0.y;
        double d = dx*dx + dy*dy;
        double t1 = Math.pow(d, 0.25);
		if(t1 <= 0) t1 = 0.1;

        dx = p2.x-p1.x;
        dy = p2.y-p1.y;
        d = dx*dx + dy*dy;
        double t2 = t1 + Math.pow(d, 0.25);
		if(t2 <= t1 ) t2 = t1+0.1;

        dx = p3.x-p2.x;
        dy = p3.y-p2.y;
        d = dx*dx + dy*dy;
        double t3 = t2 + Math.pow(d, 0.25);
		if(t3 <= t2 ) t3 = t2+0.1;
		
		double t = lerpD(t1, t2, f);

        double invDt01 = 1.0 / (t1);
        double invDt12 = 1.0 / (t2 - t1);
        double invDt23 = 1.0 / (t3 - t2);
        double f01a = (t1 - t) * invDt01;
        double f01b = (t) * invDt01;
        double f12a = (t2 - t) * invDt12;
        double f12b = (t - t1) * invDt12;
        double f23a = (t3 - t) * invDt23;
        double f23b = (t - t2) * invDt23;
        double x01 = f01a * p0.x + f01b * p1.x;
        double y01 = f01a * p0.y + f01b * p1.y;
        double x12 = f12a * p1.x + f12b * p2.x;
        double y12 = f12a * p1.y + f12b * p2.y;
        double x23 = f23a * p2.x + f23b * p3.x;
        double y23 = f23a * p2.y + f23b * p3.y;
        double invDt02 = 1.0 / (t2);
        double invDt13 = 1.0 / (t3 - t1);
        double f012a = (t2 - t) * invDt02;
        double f012b = (t) * invDt02;
        double f123a = (t3 - t) * invDt13;
        double f123b = (t - t1) * invDt13;
        double x012 = f012a * x01 + f012b * x12;
        double y012 = f012a * y01 + f012b * y12;
        double x123 = f123a * x12 + f123b * x23;
        double y123 = f123a * y12 + f123b * y23;
        double resultX = f12a * x012 + f12b * x123;
        double resultY = f12a * y012 + f12b * y123;

        return new Vec2f(resultX, resultY);
	}
	
	/**
	 * Based on <a href="https://github.com/javagl/Geom">this implementation</a>.
	 * @param p0
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param f
	 * @return
	 */
	public static Vec3f catmullRomSpline3f(Vec3f p0, Vec3f p1, Vec3f p2, Vec3f p3, double f) {
		double dx = p1.x-p0.x;
        double dy = p1.y-p0.y;
        double dz = p1.z-p0.z;
        double d = dx*dx + dy*dy + dz*dz;
        double t1 = Math.pow(d, 0.25);
		if(t1 <= 0) t1 = 0.1;

        dx = p2.x-p1.x;
        dy = p2.y-p1.y;
        dz = p2.z-p1.z;
        d = dx*dx + dy*dy + dz*dz;
        double t2 = t1 + Math.pow(d, 0.25);
		if(t2 <= t1 ) t2 = t1+0.1;

        dx = p3.x-p2.x;
        dy = p3.y-p2.y;
        dz = p3.z-p2.z;
        d = dx*dx + dy*dy + dz*dz;
        double t3 = t2 + Math.pow(d, 0.25);
		if(t3 <= t2 ) t3 = t2+0.1;
		
		double t = lerpD(t1, t2, f);

        double invDt01 = 1.0 / (t1);
        double invDt12 = 1.0 / (t2 - t1);
        double invDt23 = 1.0 / (t3 - t2);
        double f01a = (t1 - t) * invDt01;
        double f01b = (t) * invDt01;
        double f12a = (t2 - t) * invDt12;
        double f12b = (t - t1) * invDt12;
        double f23a = (t3 - t) * invDt23;
        double f23b = (t - t2) * invDt23;
        double x01 = f01a * p0.x + f01b * p1.x;
        double y01 = f01a * p0.y + f01b * p1.y;
        double z01 = f01a * p0.z + f01b * p1.z;
        double x12 = f12a * p1.x + f12b * p2.x;
        double y12 = f12a * p1.y + f12b * p2.y;
        double z12 = f12a * p1.z + f12b * p3.z;
        double x23 = f23a * p2.x + f23b * p3.x;
        double y23 = f23a * p2.y + f23b * p3.y;
        double z23 = f23a * p2.z + f23b * p3.z;
        double invDt02 = 1.0 / (t2);
        double invDt13 = 1.0 / (t3 - t1);
        double f012a = (t2 - t) * invDt02;
        double f012b = (t) * invDt02;
        double f123a = (t3 - t) * invDt13;
        double f123b = (t - t1) * invDt13;
        double x012 = f012a * x01 + f012b * x12;
        double y012 = f012a * y01 + f012b * y12;
        double z012 = f012a * z01 + f012b * z12;
        double x123 = f123a * x12 + f123b * x23;
        double y123 = f123a * y12 + f123b * y23;
        double z123 = f123a * z12 + f123b * z23;
        double resultX = f12a * x012 + f12b * x123;
        double resultY = f12a * y012 + f12b * y123;
        double resultZ = f12a * z012 + f12b * z123;

        return new Vec3f(resultX, resultY, resultZ);
	}
}
