package arenashooter.engine.math;

/**
 * Interface for accessing a Vector 4 of floats in read-only
 */
public interface Vec4fi {
	/**
	 * @return X component of this vector
	 */
	public float x();
	
	/**
	 * @return Y component of this vector
	 */
	public float y();
	
	/**
	 * @return Z component of this vector
	 */
	public float z();
	
	/**
	 * @return W component of this vector
	 */
	public float w();
	
	/**
	 * @return vector length
	 */
	public double length();
	
	public float[] toArray(float[] target);
}
