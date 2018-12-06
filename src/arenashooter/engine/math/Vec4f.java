package arenashooter.engine.math;

/**
 * Mutable 3 dimensionnal vector of floats
 */
public class Vec4f {
	
	public float w, x, y, z;
	
	/**
	 * Creates a (0, 0, 0, 0) vector
	 */
	public Vec4f() {}
	
	/**
	 * Creates a (w, x, y, z) vector
	 * @param w
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vec4f(float w, float x, float y, float z) {
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Add two vectors together.
	 * This becomes this+v
	 * @param v
	 */
	public void add(Vec4f v) {
		w += v.w;
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
		w *= a;
		x *= a;
		y *= a;
		z *= a;
	}
	
	/**
	 * @return vector length
	 */
	public double length() {
		return Math.sqrt( (w*w)+(x*x)+(y*y)+(z*z) );
	}
	
	/**
	 * Normalizes a vector (sets its length to 1)
	 * @param v
	 * @return normalized vector, or (0,0, 0) if length is 0
	 */
	public static Vec4f normalize( Vec4f v ) {
		double len = v.length();
		
		if( len == 0 ) return new Vec4f();
		
		return new Vec4f( (float)(v.w/len), (float)(v.x/len), (float)(v.y/len), (float)(v.z/len) );
	}
	
	public static Vec4f lerp( Vec4f a, Vec4f b, float f ) {
		return new Vec4f( Utils.lerpF(a.w, b.w, f),
						  Utils.lerpF(a.x, b.x, f),
						  Utils.lerpF(a.y, b.y, f),
						  Utils.lerpF(a.z, b.z, f));
	}
	
	public Vec4f clone() {
		return new Vec4f(w, x, y, z);
	}
	
	public float[] toArray() { return new float[] {w, x, y, z}; }
	
	public String toString() { return "( "+w+", "+x+", "+y+", "+z+" )"; }
	
	//Static functions
	
	/**
	 * Add two vectors together
	 * @param a
	 * @param b
	 * @return a+b
	 */
	public static Vec4f add(Vec4f a, Vec4f b) {
		return new Vec4f(a.w+b.w, a.x+b.x, a.y+b.y, a.z+b.z);
	}
	
	/**
	 * Multiplies a vector by a float
	 * @param v the vector
	 * @param a the float
	 * @return v*a
	 */
	public static Vec4f multiply( Vec4f v, float a ) {
		return new Vec4f( v.w*a, v.x*a, v.y*a, v.z*a );
	}
}
