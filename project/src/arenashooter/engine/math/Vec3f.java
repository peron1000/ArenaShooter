package arenashooter.engine.math;

/**
 * Mutable 3 dimensional vector of floats
 */
public class Vec3f {
	
	public float x, y, z;
	
	/**
	 * Creates a (0, 0, 0) vector
	 */
	public Vec3f() {}
	
	/**
	 * Creates a (a, a, a) vector
	 * @param a
	 */
	public Vec3f(float a) {
		x=a;
		y=a;
		z=a;
	}
	
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
	 * Creates a (x, y, z) vector from doubles (values will be casted to float)
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vec3f(double x, double y, double z) {
		this.x = (float)x;
		this.y = (float)y;
		this.z = (float)z;
	}
	
	/**
	 * This becomes Other
	 * @param other vector to copy
	 */
	public void set(Vec3f other) {
		x = other.x;
		y = other.y;
		z = other.z;
	}
	
	/**
	 * <i>This</i> becomes (x, y, z) and is returned
	 * @param x
	 * @param y
	 * @param z
	 * @return <i>this</i> (modified)
	 */
	public Vec3f set(double x, double y, double z) {
		this.x = (float)x;
		this.y = (float)y;
		this.z = (float)z;
		return this;
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
	 * @return <i>this</i> (modified)
	 */
	public Vec3f multiply( float a ) {
		x *= a;
		y *= a;
		z *= a;
		return this;
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
		
		return new Vec3f( v.x/len, v.y/len, v.z/len );
	}
	
	public Vec3f clone() {
		return new Vec3f(x, y, z);
	}
	
	public float[] toArray() { return new float[] {x, y, z}; }
	
	public String toString() { return "( "+x+", "+y+", "+z+" )"; }
	
	//
	//Static functions
	//
	
	/**
	 * Add two vectors together
	 * @param a
	 * @param b
	 * @return a+b (original vectors are unchanged)
	 */
	public static Vec3f add(Vec3f a, Vec3f b) {
		return new Vec3f(a.x+b.x, a.y+b.y, a.z+b.z);
	}
	
	/**
	 * Add two vectors together, store the result in target and return it
	 * <br/> Avoids object creation
	 * 
	 * @param a
	 * @param b
	 * @param target
	 * @return target (a+b)
	 */
	public static Vec3f add(Vec3f a, Vec3f b, Vec3f target) {
		target.x = a.x+b.x;
		target.y = a.y+b.y;
		target.z = a.z+b.z;
		return target;
	}
	
	/**
	 * Subtract two vectors
	 * @param a
	 * @param b
	 * @return a-b (original vectors are unchanged)
	 */
	public static Vec3f subtract(Vec3f a, Vec3f b) {
		return new Vec3f( a.x-b.x, a.y-b.y, a.z-b.z );
	}
	
	/**
	 * Multiplies a vector by a double
	 * @param v the vector
	 * @param a the double
	 * @return v*a (original vector is unchanged)
	 */
	public static Vec3f multiply( Vec3f v, double a ) {
		return new Vec3f( v.x*a, v.y*a, v.z*a );
	}
	
	/**
	 * Cross product of two vectors
	 * @param a
	 * @param b
	 * @return a x b (original vectors are unchanged)
	 */
	public static Vec3f cross( Vec3f a, Vec3f b ) {
		Vec3f res = new Vec3f();
		
		res.x = a.y*b.z - a.z*b.y;
		res.y = a.z*b.x - a.x*b.z;
		res.z = a.x*b.y - a.y*b.x;
		
		return res;
	}
	
	/**
	 * Converts a hue-saturation-value color to rgb. Input values are all normalized (0-1 range).
	 * <br/> Result is stored in <i>target</i>
	 * @param h hue
	 * @param s saturation
	 * @param v value
	 * @param target
	 * @return (r, g, b) stored in <i>target</i>
	 */
	public static Vec3f hsvToRgb(float h, float s, float v, Vec3f target) { //TODO: test
		if(s == 0) return target.set(v, v, v); //No saturation, return value
		int i = (int)(h*6);
		float f = (h*6f)-i;
		float p = v*(1f-s);
		float q = v*(1f-s*f);
		float t = v*(1f-s*(1f-f));
		i %= 6;
		switch(i) {
		case 0:
			return target.set(v, t, p);
		case 1:
			return target.set(q, v, p);
		case 2:
			return target.set(p, v, t);
		case 3:
			return target.set(p, q, v);
		case 4:
			return target.set(t, p, v);
		default:
			return target.set(v, p, q);
		}
	}
	
	public static Vec3f lerp( Vec3f a, Vec3f b, double f ) {
		return new Vec3f( Utils.lerpF(a.x, b.x, f),
						  Utils.lerpF(a.y, b.y, f),
						  Utils.lerpF(a.z, b.z, f));
	}
}
