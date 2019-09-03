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
	 * @return quaternion length
	 */
	public double length();
	
	/**
     * Copies the values from <i>other</i> into <i>this</i> and return it
     * @param other Quat to copy
     * @return <i>this</i> (modified)
     */
    public Quat set(QuatI other);
}
