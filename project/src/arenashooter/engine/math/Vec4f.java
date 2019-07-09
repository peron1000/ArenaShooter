package arenashooter.engine.math;

import java.io.IOException;
import java.io.Writer;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.Jsonable;

/**
 * Mutable 4 dimensional vector of floats (x, y, z, w), (r, g, b, a) for colors
 */
public class Vec4f implements Jsonable {
	
	public float x, y, z, w;
	
	/**
	 * Creates a (0, 0, 0, 0) vector
	 */
	public Vec4f() {}
	
	/**
	 * Creates a (x, y, z, w) vector (rgba for a color)
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
	 * Create a (x, x, x, x) vector
	 * @param x
	 */
	public Vec4f(float x) {
		this.x = x;
		y = x;
		z = x;
		w = x;
	}
	
	/**
	 * Creates a (x, y, z, w) vector (rgba for a color from doubles (values will be casted to float)
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
	 * Appends w to a Vec3f
	 * @param xyz
	 * @param w
	 */
	public Vec4f(Vec3f xyz, float w) {
		x = xyz.x;
		y = xyz.y;
		z = xyz.z;
		this.w = w;
	}
	

	/**
	 * <i>This</i> becomes (x, y, z, w) and return <i>this</i>
	 * @param x
	 * @param y
	 * @paraw z
	 * @paraw w
	 * @return <i>this</i> (modified)
	 */
	public Vec4f set(double x, double y, double z, double w) {
		this.x = (float)x;
		this.y = (float)y;
		this.z = (float)z;
		this.w = (float)w;
		return this;
	}

	/**
	 * <i>This</i> becomes other and return <i>this</i>
	 * @param other
	 * @return <i>this</i> (modified)
	 */
	public Vec4f set(Vec4f other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
		this.w = other.w;
		return this;
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
		return lerp(a, b, f, new Vec4f());
	}
	
	public static Vec4f lerp( Vec4f a, Vec4f b, double f, Vec4f target ) {
		target.x = Utils.lerpF(a.x, b.x, f);
		target.y = Utils.lerpF(a.y, b.y, f);
		target.z = Utils.lerpF(a.z, b.z, f);
		target.w = Utils.lerpF(a.w, b.w, f);
		
		return target;
	}
	
	public Vec4f clone() {
		return new Vec4f(x, y, z, w);
	}
	
	public float[] toArray(float[] target) {
		target[0] = x;
		target[1] = y;
		target[2] = z;
		target[3] = w;
		return target;
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
	public static Vec4f add(Vec4f a, Vec4f b) {
		return new Vec4f(a.x+b.x, a.y+b.y, a.z+b.z, a.w+b.w);
	}
	
	/**
	 * Subtract a vector from another
	 * @param a
	 * @param b
	 * @return a-b (original vectors are unchanged)
	 */
	public static Vec4f sub(Vec4f a, Vec4f b) {
		return new Vec4f(a.x-b.x, a.y-b.y, a.z-b.z, a.w-b.w);
	}
	
	/**
	 * Multiplies a vector by a double
	 * @param v the vector
	 * @param a the double
	 * @return v*a (original vector is unchanged)
	 */
	public static Vec4f multiply( Vec4f v, double a ) {
		return new Vec4f( v.x*a, v.y*a, v.z*a, v.w*a );
	}
	
	private JsonArray getJson() {
		return new JsonArray().addChain(x).addChain(y).addChain(z).addChain(w);
	}
	
	@Override
	public String toJson() {
		return getJson().toJson();
	}


	@Override
	public void toJson(Writer writable) throws IOException {
		getJson().toJson(writable);
	}
}
