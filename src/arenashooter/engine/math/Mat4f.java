package arenashooter.engine.math;

/**
 * Mutable 4*4 matrix of floats
 */
public class Mat4f {

//	public float 	m00, m10, m20, m30,
//					m01, m11, m21, m31,
//					m02, m12, m22, m32,
//					m03, m13, m23, m33;
	
	public float[][] val = new float[4][4];
	
	public Mat4f() {}
	
	public Mat4f( Mat4f m ) {
		this.val = m.val.clone();
	}
	
//	public void identity() {
//		val = 	new float[][] {{ 1, 0, 0, 0 },
//									{ 0, 1, 0, 0 },
//									{ 0, 0, 1, 0 },
//									{ 0, 0, 0, 1 }};
//		}
//	}

	public Mat4f clone() {
		return new Mat4f(this);
	}
	
	/**
	 * @return This matrix as a 1 dimension array
	 */
	public float[] toArray() {
		float[] res = new float[16];
		
		for(int i=0; i<4; i++)
			for(int j=0; j<4; j++)
				res[ (i*4)+j ] = val[i][j];
		
		return res;
	}
	
	public static Mat4f identity() {
		Mat4f res = new Mat4f();
//		res.m00 = res.m11 = res.m22 = res.m33 = 1;
		res.val[0][0] = 1;
		res.val[1][1] = 1;
		res.val[2][2] = 1;
		res.val[3][3] = 1;
		return res;
	}
	
	/**
	 * Transpose a matrix
	 * @param m
	 * @return m transposed
	 */
	public static Mat4f transpose( Mat4f m ) {
		Mat4f res = new Mat4f();
		
		for( int i=0; i<4; i++ )
			for( int j=0; j<4; j++ )
				res.val[i][j] = m.val[j][i];
		
		return res;
	}
	
	/**
	 * Creates a symmetric perspective projection matrix
	 * 
	 * @param near clip plane distance, should be > 0
	 * @param far clip plane distance, should be > near
	 * @param yFOV vertival field of view
	 * @param ratio aspect ratio (width/height)
	 * @return the projection matrix
	 */
	public static Mat4f perspective( float near, float far, float yFOV, float ratio ) {
		Mat4f res = new Mat4f();
		
		float top = (float) (Math.tan(yFOV/2)*near);
		float right = top*ratio;
		
		res.val[0][0] = near/right;
		res.val[1][1] = near/top;
		res.val[2][2] = -(far+near)/(far-near);
		res.val[3][2] = -(2*far*near)/(far-near); //TODO: tester si besoin de transposer ou non
		res.val[2][3] = -1; //TODO: tester si besoin de transposer ou non
		
		return res;
	}

	/**
	 * Multiplies 2 matrices
	 * @param m1
	 * @param m2
	 * @return m1*m2
	 */
	public static Mat4f mul( Mat4f m1, Mat4f m2 ) { 
		/*TODO: Tester ceci, ca depend de si on stock les matrices par colonnes/lignes ou lignes/colonnes, 
		 * a adapter plus tard quand on s'atttaquera au rendu
		 */
		Mat4f res = new Mat4f();
		
		for( int i=0; i<4; i++ )
			for( int j=0; j<4; j++ )
				res.val[i][j] = (m1.val[0][j]*m2.val[i][0])+
								(m1.val[1][j]*m2.val[i][1])+
								(m1.val[2][j]*m2.val[i][2])+
								(m1.val[3][j]*m2.val[i][3]);
		
		return res;
	}
}
