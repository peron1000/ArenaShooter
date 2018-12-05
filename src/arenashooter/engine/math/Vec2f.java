package arenashooter.engine.math;

/**
 * Mutable 2 dimensionnal vector of floats
 */
public class Vec2f {
	
	public float x, y;
	
	/**
	 * Creates a (0, 0) vector
	 */
	public Vec2f() {}
	
	/**
	 * Creates a (x, y) vector
	 * @param x
	 * @param y
	 */
	public Vec2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * This becomes Other
	 * @param other vector to copy
	 */
	public void set(Vec2f other) {
		x = other.x;
		y = other.y;
	}

	/**
	 * Add two vectors together.
	 * This becomes this+v
	 * @param v
	 */
	public void add(Vec2f v) {
		x += v.x;
		y += v.y;
	}
	
	/**
	 * Multiplies the vector.
	 * This becomes this*a
	 * @param a
	 */
	public void multiply( float a ) {
		x *= a;
		y *= a;
	}
	
	/**
	 * @return vector length
	 */
	public double length() {
		return Math.sqrt( (x*x)+(y*y) );
	}
	
	/**
	 * @return vector length squared
	 * this is cheaper than length() because it avoids using a square root
	 */
	public double lengthSquared() {
		return (x*x)+(y*y);
	}
	
	/**
	 * Normalizes a vector (sets its length to 1)
	 * @param v
	 * @return normalized vector, or (0,0) if length is 0
	 */
	public static Vec2f normalize( Vec2f v ) {
		double len = v.length();
		
		if( len == 0 ) return new Vec2f();
		
		return new Vec2f( (float)(v.x/len), (float)(v.y/len) );
	}
	
	public Vec2f clone() {
		return new Vec2f(x, y);
	}
	
	public String toString() { return "( "+x+", "+y+" )"; }
	
	//Static functions
	
	public static Vec2f fromAngle(float angle) {
		return new Vec2f( (float)Math.cos(angle), (float)Math.sin(angle) );
	}
	
	/**
	 * Add two vectors together
	 * @param a
	 * @param b
	 * @return a+b
	 */
	public static Vec2f add(Vec2f a, Vec2f b) {
		return new Vec2f(a.x+b.x, a.y+b.y);
	}
	
	/**
	 * Multiplies a vector by a float
	 * @param v the vector
	 * @param a the float
	 * @return v*a
	 */
	public static Vec2f multiply( Vec2f v, float a ) {
		return new Vec2f( v.x*a, v.y*a );
	}
}
