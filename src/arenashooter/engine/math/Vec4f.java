package arenashooter.engine.math;

/**
 * Mutable 3 dimensionnal vector of floats
 */
public class Vec4f {
	
	public float x, y, z, w;
	
	/**
	 * Creates a (0, 0, 0, 0) vector
	 */
	public Vec4f() {}
	
	/**
	 * Creates a (x, y, z, w) vector
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public Vec4f(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	/**
	 * Creates a (x, y, z, w) vector from doubles (values will be casted to float)
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public Vec4f(double x, double y, double z, double w) {
		this.x = (float)x;
		this.y = (float)y;
		this.z = (float)z;
		this.w = (float)w;
	}

	/**
	 * Add two vectors together.
	 * This becomes this+v
	 * @param v
	 */
	public void add(Vec4f v) {
		x += v.x;
		y += v.y;
		z += v.z;
		w += v.w;
	}
	
	/**
	 * Multiplies the vector.
	 * This becomes this*a
	 * @param a
	 */
	public void multiply( float a ) {
		x *= a;
		y *= a;
		z *= a;
		w *= a;
	}
	
	/**
	 * @return vector length
	 */
	public double length() {
		return Math.sqrt( (x*x)+(y*y)+(z*z)+(w*w) );
	}
	
	/**
	 * Normalizes a vector (sets its length to 1)
	 * @param v
	 * @return normalized vector, or (0, 0, 0, 0) if length is 0
	 */
	public static Vec4f normalize( Vec4f v ) {
		double len = v.length();
		
		if( len == 0 ) return new Vec4f();
		
		return new Vec4f( v.x/len, v.y/len, v.z/len, v.w/len );
	}
	
	public static Vec4f lerp( Vec4f a, Vec4f b, double f ) {
		return new Vec4f( Utils.lerpF(a.x, b.x, f),
						  Utils.lerpF(a.y, b.y, f),
						  Utils.lerpF(a.z, b.z, f),
						  Utils.lerpF(a.w, b.w, f));
	}
	
	public Vec4f clone() {
		return new Vec4f(x, y, z, w);
	}
	
	public float[] toArray() { return new float[] {x, y, z, w}; }
	
	public String toString() { return "( "+x+", "+y+", "+z+", "+w+" )"; }
	
	//
	//Static functions
	//
	
	/**
	 * Add two vectors together
	 * @param a
	 * @param b
	 * @return a+b (original vectors are unchanged)
	 */
	public static Vec4f add(Vec4f a, Vec4f b) {
		return new Vec4f(a.x+b.x, a.y+b.y, a.z+b.z, a.w+b.w);
	}
	
	/**
	 * Multiplies a vector by a double
	 * @param v the vector
	 * @param a the float
	 * @return v*a (original vector is unchanged)
	 */
	public static Vec4f multiply( Vec4f v, double a ) {
		return new Vec4f( v.x*a, v.y*a, v.z*a, v.w*a );
	}
}
