package arenashooter.engine.math;

import org.jbox2d.common.Vec2;

/**
 * Interface for accessing a Vector 2 of floats in read-only
 */
public interface Vec2fi {
	/**
	 * @return X component of this vector
	 */
	public float x();
	
	/**
	 * @return Y component of this vector
	 */
	public float y();
	
	/**
	 * @return vector length
	 */
	public double length();
	
	/**
	 * This is cheaper than length() because it avoids using a square root
	 * @return vector length squared
	 */
	public double lengthSquared();
	
	/** 
	 * 
	 * @return angle de <i>this</i> in radians
	 */
	public double angle();
	
	public float[] toArray(float[] target);
	
	/**
	 * Set <i>b2Vec</i> to <i>this</i>
	 * @param b2Vec
	 * @return <i>b2Vec</i> (modified)
	 */
	public Vec2 toB2Vec(Vec2 position);
	
	/**
	 * @return creates a Box-2d vector from <i>this</i>
	 */
	public Vec2 toB2Vec();

	public boolean equals(Vec2fi other, float errorMargin);
}
