package arenashooter.engine.math;

/**
 * Mutable 3 dimensionnal vector of floats
 */
public class Vec3f {
	
	public float x, y, z;
	
	/**
	 * Creates a (0, 0, 0) vector
	 */
	public Vec3f() {}
	
	/**
	 * Creates a (x, y, z) vector
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vec3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Add two vectors together.
	 * This becomes this+v
	 * @param v
	 */
	public void add(Vec3f v) {
		x += v.x;
		y += v.y;
		z += v.z;
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
	}
	
	/**
	 * @return vector length
	 */
	public double length() {
		return Math.sqrt( (x*x)+(y*y)+(z*z) );
	}
	
	/**
	 * Normalizes a vector (sets its length to 1)
	 * @param v
	 * @return normalized vector, or (0,0, 0) if length is 0
	 */
	public static Vec3f normalize( Vec3f v ) {
		double len = v.length();
		
		if( len == 0 ) return new Vec3f();
		
		return new Vec3f( (float)(v.x/len), (float)(v.y/len), (float)(v.z/len) );
	}
	
	public Vec3f clone() {
		return new Vec3f(x, y, z);
	}
	
	public float[] toArray() { return new float[] {x, y, z}; }
	
	public String toString() { return "( "+x+", "+y+", "+z+" )"; }
	
	//Static functions
	
	/**
	 * Add two vectors together
	 * @param a
	 * @param b
	 * @return a+b
	 */
	public static Vec3f add(Vec3f a, Vec3f b) {
		return new Vec3f(a.x+b.x, a.y+b.y, a.z+b.z);
	}
	
	/**
	 * Multiplies a vector by a float
	 * @param v the vector
	 * @param a the float
	 * @return v*a
	 */
	public static Vec3f multiply( Vec3f v, float a ) {
		return new Vec3f( v.x*a, v.y*a, v.z*a );
	}
}
