package arenashooter.engine.math;

/**
 * Interface for accessing a Vector 3 of floats in read-only
 */
public interface Vec3fi {
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
	 * @return vector length
	 */
	public double length();
	
	public float[] toArray(float[] target);
}
