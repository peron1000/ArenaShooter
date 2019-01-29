package arenashooter.engine.math;

import arenashooter.engine.graphics.Window;

/**
 * Mutable 2 dimensionnal vector of floats
 */
public class Vec2f {

	public float x, y;

	/**
	 * Creates a (0, 0) vector
	 */
	public Vec2f() {
	}

	/**
	 * Creates a (a, a) vector
	 * 
	 * @param a
	 */
	public Vec2f(double a) {
		x = (float) a;
		y = (float) a;
	}

	/**
	 * Creates a (x, y) vector
	 * 
	 * @param x
	 * @param y
	 */
	public Vec2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Creates a (x, y) vector from doubles (values will be casted to float)
	 * 
	 * @param x
	 * @param y
	 */
	public Vec2f(double x, double y) {
		this.x = (float) x;
		this.y = (float) y;
	}

	/**
	 * This becomes Other
	 * 
	 * @param other
	 *            vector to copy
	 */
	public void set(Vec2f other) {
		x = other.x;
		y = other.y;
	}

	/**
	 * Add two vectors together. This becomes this+v
	 * 
	 * @param v
	 */
	public void add(Vec2f v) {
		x += v.x;
		y += v.y;
	}

	/**
	 * Multiplies the vector. This becomes this*a
	 * 
	 * @param a
	 */
	public void multiply(float a) {
		x *= a;
		y *= a;
	}

	/**
	 * @return vector length
	 */
	public double length() {
		return Math.sqrt((x * x) + (y * y));
	}

	/**
	 * @return vector length squared this is cheaper than length() because it avoids
	 *         using a square root
	 */
	public double lengthSquared() {
		return (x * x) + (y * y);
	}

	/**
	 * Normalizes a vector (sets its length to 1)
	 * 
	 * @param v
	 * @return normalized vector, or (0,0) if length is 0
	 */
	public static Vec2f normalize(Vec2f v) {
		double len = v.length();

		if (len == 0)
			return new Vec2f();

		return new Vec2f(v.x / len, v.y / len);
	}

	/** 
	 * 
	 * @return angle de this en radians
	 */
	public double angle() {
		return Math.atan2(y, x);
	}

	public Vec2f clone() {
		return new Vec2f(x, y);
	}

	public String toString() {
		return "( " + x + ", " + y + " )";
	}

	//
	// Static functions
	//

	public static Vec2f fromAngle(double angle) {
		return new Vec2f(Math.cos(angle), Math.sin(angle));
	}

	/**
	 * Add two vectors together
	 * 
	 * @param a
	 * @param b
	 * @return a+b (original vector is unchanged)
	 */
	public static Vec2f add(Vec2f a, Vec2f b) {
		return new Vec2f(a.x + b.x, a.y + b.y);
	}

	/**
	 * Subtract two vectors together
	 * 
	 * @param a
	 * @param b
	 * @return a-b (original vector is unchanged)
	 */
	public static Vec2f subtract(Vec2f a, Vec2f b) {
		return new Vec2f(a.x - b.x, a.y - b.y);
	}

	/**
	 * Multiplies a vector by a double
	 * 
	 * @param v
	 *            the vector
	 * @param a
	 *            the double
	 * @return v*a (original vector is unchanged)
	 */
	public static Vec2f multiply(Vec2f v, double a) {
		return new Vec2f(v.x * a, v.y * a);
	}

	/**
	 * Multiplies two vectors
	 * 
	 * @param a
	 * @param b
	 * @return ( a.x*b.x, a.y*b.y ) (original vectors are unchanged)
	 */
	public static Vec2f multiply(Vec2f a, Vec2f b) {
		return new Vec2f(a.x * b.x, a.y * b.y);
	}

	/**
	 * Dot product of two vectors
	 * 
	 * @param a
	 * @param b
	 * @return a.b (original vectors are unchanged)
	 */
	public static double dot(Vec2f a, Vec2f b) {
		return a.x * b.x + a.y * b.y;
	}

	public static Vec2f rotate(Vec2f v, double r) {
		double cos = Math.cos(r);
		double sin = Math.sin(r);
		return new Vec2f(cos * v.x - sin * v.y, sin * v.x + cos * v.y);
	}

	public static Vec2f rotate90(Vec2f v) {
		return new Vec2f(-v.y, v.x);
	}
	
	/**
	 * Project a point in world to screen
	 * @param world point to project
	 * @return screen space projection
	 */
	public static Vec2f worldToScreen(Vec3f world) { //TODO: test
		Mat4f model = Mat4f.translation(world);
		float[] projected = Mat4f.mul(Mat4f.mul(Window.proj, Window.camera.viewMatrix), model).val[3];
				
		return new Vec2f( projected[0], projected[1] );
	}

	/**
	 * Project a point in world to screen
	 * @param world point to project
	 * @return screen space projection
	 */
	public static Vec2f worldToScreen(Vec2f world) { //TODO: test
		Mat4f model = Mat4f.translation(world);
		float[] projected = Mat4f.mul(Mat4f.mul(Window.proj, Window.camera.viewMatrix), model).val[3];
				
		return new Vec2f( projected[0], projected[1] );
	}
	
	public void print() {
		System.out.println("x = " + x + " ; y = " + y);
	}
}
