package arenashooter.engine.math;

/**
 * Interface for accessing a Matrix 4*4 of floats in read-only
 */
public interface Mat4fi {
	public float m00();
	public float m10();
	public float m20();
	public float m30();

	public float m01();
	public float m11();
	public float m21();
	public float m31();

	public float m02();
	public float m12();
	public float m22();
	public float m32();
	
	public float m03();
	public float m13();
	public float m23();
	public float m33();
	
	/**
	 * @param target float[16] array used to store the result
	 * @return This matrix as a 1 dimension array ( m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33 )
	 */
	public float[] toArray(float[] target);
}