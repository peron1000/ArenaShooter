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
	
	public static Quat fromAxis( float x, float y, float z, double angle ) { //TODO: Test
		Quat res = new Quat();

		double angleSin = Math.sin(angle/2);
		res.w = (float)Math.cos(angle/2);
		res.x = (float)(x*angleSin);
		res.y = (float)(y*angleSin);
		res.z = (float)(z*angleSin);
		
		return res;
	}
	
	/**
	 * Create a quaternion from a 2d rotation
	 * @param angle
	 * @return new Quat containing the result
	 */
	public static Quat fromAngle( double angle ) {
		Quat res = new Quat();
		
		res.w = (float)Math.cos(angle/2);
		res.z = (float)Math.sin(angle/2);
		
		return res;
	}
	
	/**
	 * Target becomes a new Quat corresponding to an angle
	 * <br/> Avoids object creation
	 * @param angle 
	 * @param target
	 * @return target (modified)
	 */
    public static Quat fromAngle( double angle, Quat target ) {
    	target.w = (float)Math.cos(angle/2);
    	target.x = 0;
    	target.y = 0;
    	target.z = (float)Math.sin(angle/2);
		
		return target;
	}
    
    /**
     * Copies the values from <i>other</i> into <i>this</i> and return it
     * @param other Quat to copy
     * @return <i>this</i> (modified)
     */
    public Quat set(Quat other) {
    	this.w = other.w;
    	this.x = other.x;
    	this.y = other.y;
    	this.z = other.z;
    	return this;
    }
	
	/**
	 * Rotate a vector by this quaternion
	 * @param source vector to rotate
	 * @return rotated vector
	 */
	public Vec3f rotate( Vec3f source ) { //TODO: Test
		float[][] r = Mat4f.rotation(this).val;
		
		float x = r[0][0]*source.x + r[1][0]*source.y + r[2][0]*source.z;
		float y = r[0][1]*source.x + r[1][1]*source.y + r[2][1]*source.z;
		float z = r[0][2]*source.x + r[1][2]*source.y + r[2][2]*source.z;
		
		return new Vec3f(x, y, z);
	}
	
	/**
	 * Rotate a vector by this quaternion and stores the result in <i>target</i>.
	 * <br/> Avoids object creation
	 * @param source vector to rotate
	 * @param target 
	 * @return <i>target</i> (modified)
	 */
	public Vec3f rotate( Vec3f source, Vec3f target ) { //TODO: Test
		float[][] r = Mat4f.rotation(this).val;
		
		target.x = r[0][0]*source.x + r[1][0]*source.y + r[2][0]*source.z;
		target.y = r[0][1]*source.x + r[1][1]*source.y + r[2][1]*source.z;
		target.z = r[0][2]*source.x + r[1][2]*source.y + r[2][2]*source.z;
		
		return target;
	}
	
	/**
	 * Get a unit vector pointing in the direction of this quaternion
	 * @return (0, 0, 1) rotated by this quaternion
	 */
	public Vec3f forward() { //TODO: Test
		double ww = w * w;
        double xx = x * x;
        double yy = y * y;
        double zz = z * z;
        double xz = x * z;
        double yw = y * w;
        double yz = y * z;
        double xw = x * w;
		
        double x = yw + xz + xz + yw;
        double y = yz + yz - xw - xw;
        double z = zz - yy - xx + ww;
		
		return new Vec3f(x, y, z);
	}
	
	/**
	 * Get a unit vector pointing upwards of this quaternion
	 * @return (0, 1, 0) rotated by this quaternion
	 */
	public Vec3f up() { //TODO: Test
		double ww = w * w;
        double xx = x * x;
        double yy = y * y;
        double zz = z * z;
        double zw = z * w;
        double xy = x * y;
        double yz = y * z;
        double xw = x * w;
        
        double x = -zw + xy - zw + xy;
        double y =  yy - zz + ww - xx;
        double z =  yz + yz + xw + xw;
		
		return new Vec3f(x, y, z);
	}
	
	/**
	 * Get a unit vector pointing to the right of this quaternion
	 * @return (1, 0, 0) rotated by this quaternion
	 */
	public Vec3f right() { //TODO: Test
		double ww = w * w;
        double xx = x * x;
        double yy = y * y;
        double zz = z * z;
        double zw = z * w;
        double xy = x * y;
        double xz = x * z;
        double yw = y * w;
		
		double x = ww + xx - zz - yy;
		double y = xy + zw + zw + xy;
		double z = xz - yw + xz - yw;
		
		return new Vec3f(x, y, z);
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
	
	/**
	 * Multiply two Quats
	 * @param q1
	 * @param q2
	 * @return result as new object
	 */
	public static Quat multiply(Quat q1, Quat q2) {
		Quat res = new Quat();
		
		res.x = q1.w*q2.x + q1.x*q2.w + q1.y*q2.z - q1.z*q2.y;
		res.y = q1.w*q2.y - q1.x*q2.z + q1.y*q2.w + q1.z*q2.x;
		res.z = q1.w*q2.z + q1.x*q2.y - q1.y*q2.x + q1.z*q2.w;
		res.w = q1.w*q2.w - q1.x*q2.x - q1.y*q2.y - q1.z*q2.z;
		
		return res;
	}
	
	/**
	 * Multiply two Quats and stores the result in <i>target</i>.
	 * <br/> Avoids object creation
	 * @param q1
	 * @param q2
	 * @return <i>target</i> (modified)
	 */
	public static Quat multiply(Quat q1, Quat q2, Quat target) {
		float x = q1.w*q2.x + q1.x*q2.w + q1.y*q2.z - q1.z*q2.y;
		float y = q1.w*q2.y - q1.x*q2.z + q1.y*q2.w + q1.z*q2.x;
		float z = q1.w*q2.z + q1.x*q2.y - q1.y*q2.x + q1.z*q2.w;
		float w = q1.w*q2.w - q1.x*q2.x - q1.y*q2.y - q1.z*q2.z;
		
		target.x = x;
		target.y = y;
		target.z = z;
		target.w = w;
		
		return target;
	}
	
	public String toString() {
		return "Quat("+w+", "+x+", "+y+", "+z+")";
	}
}
