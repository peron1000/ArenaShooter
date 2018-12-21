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
	public static Quat fromAngle( float angle ) { //TODO: Test
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
	
	/**
	 * Rotate a vector by this quaternion
	 * @param source vector to rotate
	 * @return rotated vector
	 */
	public Vec3f rotate( Vec3f source ) { //TODO: Test
		float[][] matrix = Mat4f.mul(Mat4f.rotation(this), Mat4f.translation(source)).val;
		return new Vec3f( matrix[3][0] , matrix[3][1], matrix[3][2] );
	}
	
	/**
	 * Get a unit vector pointing in the direction of this quaternion
	 * @return
	 */
	public Vec3f forward() { //TODO: Test
		return rotate( new Vec3f(0, 0, 1) );
	}
	
	/**
	 * Get a unit vector pointing upwards of this quaternion
	 * @return
	 */
	public Vec3f up() { //TODO: Test
		return rotate( new Vec3f(0, 1, 0) );
	}
	
	/**
	 * Conjugate this
	 */
    public void conjugate() {
        x = -x;
        y = -y;
        z = -z;
    }
    
    /**
     * Conjugate a quaternion
     * @param q quaternion to conjugate
     * @return conjugate of q
     */
    public static Quat conjugate(Quat q) {
    	return new Quat(-q.x, -q.y, -q.z, q.w);
    }
	
	public double length() {
		return Math.sqrt( (x*x)+(y*y)+(z*z)+(w*w) );
	}
	
	public static Quat normalize(Quat q) {
		Quat res = new Quat();
		
		double len = q.length();
		res.x = (float) (q.x/len);
		res.y = (float) (q.y/len);
		res.z = (float) (q.z/len);
		res.w = (float) (q.w/len);
		
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
	
	public String toString() {
		return "Quat("+w+", "+x+", "+y+", "+z+")";
	}
}
