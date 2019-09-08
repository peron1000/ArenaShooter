package arenashooter.engine.math;

import arenashooter.game.Main;

/**
 * Mutable rotation quaternion
 */

public class Quat implements QuatI{
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
	
	public Quat( double x, double y, double z, double w ) {
		this.w = (float)w;
		this.x = (float)x;
		this.y = (float)y;
		this.z = (float)z;
	}
	
	@Override
	public float w() { return w; }
	
	@Override
	public float x() { return x; }
	
	@Override
	public float y() { return y; }
	
	@Override
	public float z() { return z; }
	
	public static Quat fromAxis( Vec3fi axis, float angle ) {
		return fromAxis( axis.x(), axis.y(), axis.z(), angle );
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
     * Converts a rotation in euler angles to a quaternion, stores the result in <i>target</i>
	 * <br/> Avoids object creation
     * @param euler (yaw, pitch, roll)
     * @param target
     * @return <i>target</i> (modified)
     */
    public static Quat fromEuler(Vec3fi euler, Quat target) {
    	return fromEuler(euler.x(), euler.y(), euler.z(), target);
    }
    
    /**
     * Converts a rotation in euler angles to a quaternion, stores the result in <i>target</i>
	 * <br/> Avoids object creation
     * <br/>Uses <a href="http://www.euclideanspace.com/maths/geometry/rotations/euler/index.htm">this page</a>
     * @param yaw
     * @param pitch
     * @param roll
     * @param target
     * @return <i>target</i> (modified)
     */
    public static Quat fromEuler(double yaw, double pitch, double roll, Quat target) { //TODO: fix this
    	double c1 = Math.cos(yaw/2);
    	double c3 = Math.cos(pitch/2);
    	double c2 = Math.cos(roll/2);
    	double s1 = Math.sin(yaw/2);
    	double s3 = Math.sin(pitch/2);
    	double s2 = Math.sin(roll/2);
    	target.w = (float)(c1*c2*c3 - s1*s2*s3);
    	target.x = (float)(s1*s2*c3 + c1*c2*s3);
    	target.y = (float)(s1*c2*c3 + c1*s2*s3);
    	target.z = (float)(c1*s2*c3 - s1*c2*s3);
    	
    	return target;
    }

    /**
     * Extract euler angles (yaw, pitch, roll) from <i>this</i> and stores the result in <i>target</i>
	 * <br/> Avoids object creation
     * <br/>Uses <a href="http://www.euclideanspace.com/maths/geometry/rotations/euler/index.htm">this page</a>
     * @param target
     * @return <i>target</i> (modified)
     */
    public Vec3f toEuler(Vec3f target) { //TODO: Test
    	double test = x*y + z*w;
    	if (test > 0.499) { //Singularity at north pole
    		target.x = (float)(2 * Math.atan2(x ,w));
    		target.y = 0;
    		target.y = (float)(Math.PI/2);
    		return target;
    	}
    	if (test < -0.499) { //Singularity at south pole
    		target.x = (float)(-2 * Math.atan2(x, w));
    		target.y = 0;
    		target.z = (float)(-Math.PI/2);
    		return target;
    	}

    	target.x = (float) Math.atan2(2*y*w-2*x*z , 1 - 2*y*y - 2*z*z);
    	target.y = (float) Math.atan2(2*x*w-2*y*z , 1 - 2*x*x - 2*z*z);
    	target.z = (float) Math.asin(2*x*y + 2*z*w);
    	return target;
    }
    
    @Override
    public Quat set(QuatI other) {
    	this.w = other.w();
    	this.x = other.x();
    	this.y = other.y();
    	this.z = other.z();
    	return this;
    }
	
	/**
	 * Rotate a vector by this quaternion. Original vector is not modified
	 * @param source vector to rotate
	 * @return rotated vector as new object
	 */
	public Vec3f rotate( Vec3fi source ) {
		return rotate(source, new Vec3f());
	}
	
	/**
	 * Rotate a vector by this quaternion and stores the result in <i>target</i>.
	 * <br/> Avoids object creation
	 * @param source vector to rotate
	 * @param target 
	 * @return <i>target</i> (modified)
	 */
	public Vec3f rotate( Vec3fi source, Vec3f target ) { //TODO: Test
		float[][] r = Mat4f.rotation(this).val;
		
		float sx = source.x(), sy = source.y(), sz = source.z();
		
		target.x = r[0][0]*sx + r[1][0]*sy + r[2][0]*sz;
		target.y = r[0][1]*sx + r[1][1]*sy + r[2][1]*sz;
		target.z = r[0][2]*sx + r[1][2]*sy + r[2][2]*sz;
		
		return target;
	}
	
	/**
	 * Get a unit vector pointing in the direction of this quaternion
	 * @return (0, 0, 1) rotated by this quaternion
	 */
	public Vec3f forward() {
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
	 * Conjugate this (negate x, y, and z)
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
    public static Quat conjugate(QuatI q) {
    	return new Quat(-q.x(), -q.y(), -q.z(), q.w());
    }
    
    @Override
    public double lengthSquared() {
    	return (x*x)+(y*y)+(z*z)+(w*w);
    }
	
    @Override
	public double length() {
		return Math.sqrt( (x*x)+(y*y)+(z*z)+(w*w) );
	}
	
    public Quat normalize() {
    	return normalize(this, this);
    }
    
	public static Quat normalize(QuatI q, Quat target) {
		double len = q.lengthSquared();
		
		if( Math.abs(len-1) <= 0.001 )
			return target.set(q);
		
		if(Math.abs(len) <= 0.001) {
			Main.log.warn("Zero-length quaternion, setting it to default value");
			target.w = 1;
			target.x = 0;
			target.y = 0;
			target.z = 0;
			return target;
		}

		len = Math.sqrt(len);
		
		target.x = (float) (q.x()/len);
		target.y = (float) (q.y()/len);
		target.z = (float) (q.z()/len);
		target.w = (float) (q.w()/len);
		
		return target;
	}
	
	/**
	 * Multiply two Quats and stores the result in <i>target</i>.
	 * <br/> Avoids object creation
	 * @param q1
	 * @param q2
	 * @return <i>target</i> (modified)
	 */
	public static Quat multiply(QuatI q1, QuatI q2, Quat target) {
		float x = q1.w()*q2.x() + q1.x()*q2.w() + q1.y()*q2.z() - q1.z()*q2.y();
		float y = q1.w()*q2.y() - q1.x()*q2.z() + q1.y()*q2.w() + q1.z()*q2.x();
		float z = q1.w()*q2.z() + q1.x()*q2.y() - q1.y()*q2.x() + q1.z()*q2.w();
		float w = q1.w()*q2.w() - q1.x()*q2.x() - q1.y()*q2.y() - q1.z()*q2.z();
		
		target.x = x;
		target.y = y;
		target.z = z;
		target.w = w;
		
		return target;
	}
	
	/**
	 * Create a (x, y, z, w) vector from this Quaternion's values
	 * @param target
	 * @return <i>target</i>
	 */
	public Vec4f toVec4f(Vec4f target) {
		return target.set(x, y, z, w);
	}
	
	public String toString() {
		return "Quat("+w+", "+x+", "+y+", "+z+")";
	}
}
