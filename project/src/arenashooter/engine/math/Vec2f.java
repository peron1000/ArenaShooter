package arenashooter.engine.math;

import org.jbox2d.common.Vec2;

import arenashooter.engine.graphics.Window;

/**
 * Mutable 2 dimensional vector of floats (x, y)
 */
public class Vec2f {

	public float x, y;

	/**
	 * Creates a (0, 0) vector
	 */
	public Vec2f() { }

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
	 * @return creates a Box-2d vector from <i>this</i>
	 */
	public Vec2 toB2Vec() {
		return new Vec2(x, y);
	}
	
	/**
	 * Set <i>b2Vec</i> to <i>this</i>
	 * @param b2Vec
	 * @return <i>b2Vec</i> (modified)
	 */
	public Vec2 toB2Vec(Vec2 b2Vec) {
		b2Vec.set(x, y);
		return b2Vec;
	}

	/**
	 * <i>This</i> becomes Other
	 * @param other Box-2d vector to copy into <i>this</i>
	 * @return <i>this</i>
	 */
	public Vec2f set(Vec2 other) {
		x = other.x;
		y = other.y;
		return this;
	}

	/**
	 * <i>This</i> becomes Other
	 * 
	 * @param other
	 *            vector to copy
	 */
	public void set(Vec2f other) {
		x = other.x;
		y = other.y;
	}
	
	/**
	 * <i>This</i> becomes (x, y) and return <i>this</i>
	 * @param x
	 * @param y
	 * @return <i>this</i> (modified)
	 */
	public Vec2f set(double x, double y) {
		this.x = (float)x;
		this.y = (float)y;
		return this;
	}

	/**
	 * Add two vectors together. <i>This</i> becomes <i>this</i>+v
	 * 
	 * @param v
	 * @return <i>this</i> (modified)
	 */
	public Vec2f add(Vec2f v) {
		x += v.x;
		y += v.y;
		return this;
	}

	/**
	 * Multiplies the vector. <i>This</i> becomes <i>this</i>*a
	 * 
	 * @param a
	 * @return <i>this</i> (modified)
	 */
	public Vec2f multiply(float a) {
		x *= a;
		y *= a;
		return this;
	}

	/**
	 * @return vector length
	 */
	public double length() {
		return Math.sqrt((x * x) + (y * y));
	}

	/**
	 * This is cheaper than length() because it avoids using a square root
	 * @return vector length squared
	 */
	public double lengthSquared() {
		return (x * x) + (y * y);
	}

	/**
	 * Normalizes a vector (sets its length to 1)
	 * 
	 * @param v
	 * @return new normalized vector, or (0,0) if length is 0
	 */
	public static Vec2f normalize(Vec2f v) {
		double len = v.length();

		if (len == 0)
			return new Vec2f();

		return new Vec2f(v.x / len, v.y / len);
	}

	/** 
	 * 
	 * @return angle de <i>this</i> in radians
	 */
	public double angle() {
		return Math.atan2(y, x);
	}

	/**
	 * Creates a new vector with the same x and y as <i>this</i>
	 */
	@Override
	public Vec2f clone() {
		return new Vec2f(x, y);
	}
	
	public float[] toArray(float[] target) {
		target[0] = x;
		target[1] = y;
		return target;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof Vec2f)
			return ((Vec2f)other).x == x && ((Vec2f)other).y == y;
		return false;
	}
	
	public boolean equals(Vec2f other, float errorMargin) {
		return Math.abs(other.x-x) < errorMargin && Math.abs(other.y-y) < errorMargin;
	}

	@Override
	public String toString() {
		return "Vec2f( " + x + ", " + y + " )";
	}

	//
	// Static functions
	//

	public static Vec2f fromAngle(double angle) {
		return new Vec2f(Math.cos(angle), Math.sin(angle));
	}
	
	public static Vec2f fromAngle(double angle, Vec2f target) {
		return target.set(Math.cos(angle), Math.sin(angle));
	}

	/**
	 * Add two vectors together
	 * 
	 * @param a
	 * @param b
	 * @return a+b (original vectors are unchanged)
	 */
	public static Vec2f add(Vec2f a, Vec2f b) {
		return new Vec2f(a.x + b.x, a.y + b.y);
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
	public static Vec2f add(Vec2f a, Vec2f b, Vec2f target) {
		target.x = a.x+b.x;
		target.y = a.y+b.y;
		return target;
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
	 * Multiplies a vector by a double, creates a new object
	 * 
	 * @param v the vector
	 * @param a the double
	 * @return v*a (original vector is unchanged)
	 */
	public static Vec2f multiply(Vec2f v, double a) {
		return new Vec2f(v.x * a, v.y * a);
	}
	
	/**
	 * Multiplies a vector by a double and stores the result in target
	 * <br/> Avoids object creation
	 * @param v
	 * @param a
	 * @param target
	 * @return target (modified)
	 */
	public static Vec2f multiply(Vec2f v, double a, Vec2f target) {
		target.x = (float) (v.x*a);
		target.y = (float) (v.y*a);
		return target;
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
	
	/**
	 * Get direction from a vector to another
	 * @param from
	 * @param to
	 * @return
	 */
	public static double direction(Vec2f from, Vec2f to) {
		double x = to.x - from.x;
		double y = to.y - from.y;
		return Math.atan2(y, x);
	}
	
	/**
	 * Get distance between two vectors
	 * @param a
	 * @param b
	 * @return
	 */
	public static double distance(Vec2f a, Vec2f b) {
		float x = b.x-a.x;
		float y = b.y-a.y;
		
		return Math.sqrt( (x*x) + (y*y) );
	}
	
	/**
	 * Get squared distance between two vectors. This is cheaper than distance() because it avoids a square root
	 * @param a
	 * @param b
	 * @return
	 */
	public static double distanceSquared(Vec2f a, Vec2f b) {
		float x = b.x-a.x;
		float y = b.y-a.y;
		
		return (x*x) + (y*y);
	}

	/**
	 * Rotate a vector by a given angle (in radians)
	 * @param v vector to rotate
	 * @param r angle
	 * @return new rotated vector (original is unchanged)
	 */
	public static Vec2f rotate(Vec2f v, double r) {
		double cos = Math.cos(r);
		double sin = Math.sin(r);
		return new Vec2f(cos * v.x - sin * v.y, sin * v.x + cos * v.y);
	}
	
	/**
	 * Rotate a vector by a given angle (in radians) and store the result in target
	 * <br/> Avoids object creation
	 * @param v vector to rotate
	 * @param r angle
	 * @param target vector storing the result
	 * @return target (modified)
	 */
	public static Vec2f rotate(Vec2f v, double r, Vec2f target) {
		double cos = Math.cos(r);
		double sin = Math.sin(r);
		float vx = v.x, vy = v.y;
		
		target.x = (float)(cos * vx - sin * vy);
		target.y = (float)(sin * vx + cos * vy);
		return target;
	}

	public static Vec2f rotate90(Vec2f v) {
		return new Vec2f(-v.y, v.x);
	}
	
	public static Vec2f lerp( Vec2f a, Vec2f b, double f ) {
		return lerp(a, b, f, new Vec2f());
	}
	
	public static Vec2f lerp( Vec2f a, Vec2f b, double f, Vec2f target ) {
		target.x = Utils.lerpF(a.x, b.x, f);
		target.y = Utils.lerpF(a.y, b.y, f);
		return target;
	}
	
	/**
	 * Project a point in world to screen
	 * @param world point to project
	 * @return screen space projection
	 */
	public static Vec2f worldToScreen(Vec3f world) { //TODO: test
		Mat4f model = Mat4f.translation(world);
		float[] projected = Mat4f.mul(Mat4f.mul(Window.proj, Window.getView()), model).val[3];
				
		return new Vec2f( projected[0], projected[1] );
	}

	/**
	 * Project a point in world to screen
	 * @param world point to project
	 * @return screen space projection
	 */
	public static Vec2f worldToScreen(Vec2f world) { //TODO: fix this
		Mat4f model = Mat4f.translation(world);
		float[] projected = Mat4f.mul( Window.proj, Mat4f.mul( Window.getView(), model) ).val[3];
		
		return new Vec2f( projected[0], projected[1] );
	}
	
	/**
	 * /!\ Carful, this function may not work.
	 * @param v1
	 * @param v2
	 * @param angleTolerated when diffrence is calculated.
	 * @return if v1 and v2 are opposed, tolerating a given angle
	 */
	public static boolean areOpposed(Vec2f v1, Vec2f v2, double angleTolerated) {
		Vec2f vec1 = v1.clone();
		Vec2f vec2 = v2.clone();
		
		if (v1.x <= v2.x)
			vec1.multiply(-1);
		else 
			vec2.multiply(-1);
		
		System.out.println(vec1.angle());
		System.out.println(vec2.angle());
		
		return Math.abs(vec1.angle()-vec2.angle()) <= angleTolerated;
	}
}
