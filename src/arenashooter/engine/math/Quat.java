package arenashooter.engine.math;

/**
 * Mutable rotation quaternion
 */

public class Quat {
	protected float w, x, y, z;
	
	public Quat() {
		w = 1;
		x = 0;
		y = 0;
		z = 0;
	}
	
	public Quat( float x, float y, float z, float w ) {
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static Quat fromAxis( Vec3f axis, float angle ) {
		return fromAxis( axis.x, axis.y, axis.z, angle );
	}
	
	/**
	 * Create a quaternion from a 2d rotation
	 * @param angle
	 * @return
	 */
	public static Quat fromAngle( float angle ) { //TODO: Test this
		Quat res = new Quat();
		
		float angleSin = (float)Math.sin(angle/2);
		res.w = (float)Math.cos(angle/2);
		res.z = 1*angleSin;
		
		return res;
	}
	
	public static Quat fromAxis( float x, float y, float z, float angle ) {
		Quat res = new Quat();

		float angleSin = (float)Math.sin(angle/2);
		res.w = (float)Math.cos(angle/2);
		res.x = x*angleSin;
		res.y = y*angleSin;
		res.z = z*angleSin;
		
		return res;
	}
	
	public double magnitude() {
		return Math.sqrt( (x*x)+(y*y)+(z*z)+(w*w) );
	}
	
	public static Quat normalize(Quat q) {
		Quat res = new Quat();
		
		double magnitude = q.magnitude();
		res.x = (float) (q.x/magnitude);
		res.y = (float) (q.y/magnitude);
		res.z = (float) (q.z/magnitude);
		res.w = (float) (q.w/magnitude);
		
		return res;
	}
	
	public static Quat mul(Quat q1, Quat q2) {
		Quat res = new Quat();
		
		res.x = ( q1.w*q2.x + q1.x*q2.w + q1.y*q2.z - q1.z*q2.y );
		res.y = ( q1.w*q2.y - q1.x*q2.z + q1.y*q2.w + q1.z*q2.x );
		res.z = ( q1.w*q2.z + q1.x*q2.y - q1.y*q2.x + q1.z*q2.w );
		res.w = ( q1.w*q2.w - q1.x*q2.x - q1.y*q2.y - q1.z*q2.z );
		
		return res;
	}
}
