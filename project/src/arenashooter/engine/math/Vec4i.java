package arenashooter.engine.math;

/**
 * Mutable 4 dimensional vector of integers
 */
public class Vec4i {
	
	public int x, y, z, w;
	
	/**
	 * Creates a (0, 0, 0, 0) vector
	 */
	public Vec4i() {}
	
	/**
	 * Creates a (x, y, z, w) vector
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public Vec4i(int x, int y, int z, int w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/**
	 * Add two vectors together.
	 * This becomes this+v
	 * @param v
	 */
	public void add(Vec4i v) {
		x += v.x;
		y += v.y;
		z += v.z;
		w += v.w;
	}
	
	public Vec4i clone() {
		return new Vec4i(x, y, z, w);
	}
	
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
	public static Vec4i add(Vec4i a, Vec4i b) {
		return new Vec4i(a.x+b.x, a.y+b.y, a.z+b.z, a.w+b.w);
	}
}
