package arenashooter.engine.math;

/**
 * Interface for accessing a Quaternion in read-only
 */
public interface QuatI {
	/**
	 * @return W component of this quaternion
	 */
	public float w();
	/**
	 * @return X component of this quaternion
	 */
	public float x();
	
	/**
	 * @return Y component of this quaternion
	 */
	public float y();
	
	/**
	 * @return Z component of this quaternion
	 */
	public float z();
	
	/**
	 * @return quaternion length squared, this is cheaper than length()
	 */
	public double lengthSquared();
	
	/**
	 * @return quaternion length
	 */
	public double length();
	
	/**
	 * Get a unit vector pointing in the direction of this quaternion
	 * @return (0, 0, 1) rotated by this quaternion
	 */
	public Vec3f forward();
	
	/**
	 * Get a unit vector pointing upwards of this quaternion
	 * @return (0, 1, 0) rotated by this quaternion
	 */
	public Vec3f up();
	
	/**
	 * Get a unit vector pointing to the right of this quaternion
	 * @return (1, 0, 0) rotated by this quaternion
	 */
	public Vec3f right();
}
