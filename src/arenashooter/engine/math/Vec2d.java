package arenashooter.engine.math;

/**
 * Mutable 2 dimensionnal vector of doubles
 */
public class Vec2d {
	
	public double x, y;
	
	/**
	 * Creates a (0, 0) vector
	 */
	public Vec2d() {
		x = 0;
		y = 0;
	}
	
	/**
	 * Creates a (x, y) vector
	 * @param x
	 * @param y
	 */
	public Vec2d(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Add two vectors together.
	 * This becomes this+v
	 * @param v
	 */
	public void add(Vec2d v) {
		x += v.x;
		y += v.y;
	}
	
	/**
	 * Multiplies the vector.
	 * This becomes this*a
	 * @param a
	 */
	public void multiply( double a ) {
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
	 * Normalizes a vector (sets its length to 1)
	 * @param v
	 * @return normalized vector, or (0,0) if length is 0
	 */
	public static Vec2d normalize( Vec2d v ) {
		double len = v.length();
		
		if( len == 0 ) return new Vec2d();
		
		return new Vec2d( v.x/len, v.y/len );
	}
	
	public Vec2d clone() {
		return new Vec2d(x, y);
	}
	
	//Static functions
	
	/**
	 * Add two vectors together
	 * @param a
	 * @param b
	 * @return a+b
	 */
	public static Vec2d add(Vec2d a, Vec2d b) {
		return new Vec2d(a.x+b.x, a.y+b.y);
	}
	
	/**
	 * Multiplies a vector by a double
	 * @param v the vector
	 * @param a the double
	 * @return v*a
	 */
	public static Vec2d multiply( Vec2d v, double a ) {
		return new Vec2d( v.x*a, v.y*a );
	}
}
