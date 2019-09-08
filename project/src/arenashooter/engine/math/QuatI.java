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
}
