package arenashooter.engine.math;

/**
 * Mutable 4*4 matrix of floats
 */
public class Mat4f {

//	public float 	m00, m10, m20, m30,
//					m01, m11, m21, m31,
//					m02, m12, m22, m32,
//					m03, m13, m23, m33;
	
	/*
	 * float[i][j]:
	 * i0j0, i1j0, i2j0, i3j0
	 * i0j1, i1j1, i2j1, i3j1
	 * i0j2, i1j2, i2j2, j3j2
	 * i0j3, i1j3, i2j3, i3j3
	 */
	public float[][] val = new float[4][4];
	
	public Mat4f() {}
	
	public Mat4f( Mat4f m ) {
		this.val = m.val.clone();
	}
	
	public Mat4f clone() {
		return new Mat4f(this);
	}
	
	/**
	 * @return This matrix as a 1 dimension array
	 */
	public float[] toArray() {
		float[] res = new float[16];
		
		for(int j=0; j<4; j++)
			for(int i=0; i<4; i++)
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
	 * Create a rotation matrix
	 * @param q unit quaternion
	 * @return
	 */
	public static Mat4f rotation(Quat q) {
		Mat4f res = new Mat4f();
		
		double ww = q.w * q.w;
        double xx = q.x * q.x;
        double yy = q.y * q.y;
        double zz = q.z * q.z;
        double zw = q.z * q.w;
        double xy = q.x * q.y;
        double xz = q.x * q.z;
        double yw = q.y * q.w;
        double yz = q.y * q.z;
        double xw = q.x * q.w;
		
		//First line
        res.val[0][0] = (float) (ww + xx - zz - yy);
        res.val[0][1] = (float) (xy + zw + zw + xy);
        res.val[0][2] = (float) (xz - yw + xz - yw);
		
		//Second line
        res.val[1][0] = (float) (-zw + xy - zw + xy);
        res.val[1][1] = (float) (yy - zz + ww - xx);
        res.val[1][2] = (float) (yz + yz + xw + xw);

		//Third line
        res.val[2][0] = (float) (yw + xz + xz + yw);
        res.val[2][1] = (float) (yz + yz - xw - xw);
        res.val[2][2] = (float) (zz - yy - xx + ww);
		
		res.val[3][3] = 1;
		return res;
	}
	
	/**
	 * Create a translation matrix
	 * @param v
	 * @return
	 */
	public static Mat4f translation(Vec3f v) {
		Mat4f res = identity();
		
		res.val[3][0] = v.x;
		res.val[3][1] = v.y;
		res.val[3][2] = v.z;
		
		return res;
	}
	
	/**
	 * Create a scaling matrix
	 * @param v
	 * @return
	 */
	public static Mat4f scale(Vec3f v) {
		Mat4f res = new Mat4f();
		
		res.val[0][0] = v.x;
		res.val[1][1] = v.y;
		res.val[2][2] = v.z;
		res.val[3][3] = 1;
		
		return res;
	}
	
	public static Mat4f transform( Vec3f loc, Quat rot, Vec3f scale ) { //TODO: add rotation
		Mat4f res = new Mat4f();
		res = mul(mul(translation(loc), rotation(rot)), scale(scale));
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
	 * @param yFOV vertical field of view
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
		res.val[3][2] = -(2*far*near)/(far-near);
		res.val[2][3] = -1;
		
		return res;
	}
	
	/**
	 * Creates an orthographic projection matrix
	 * 
	 * @param near clip plane distance, should be > 0
	 * @param far clip plane distance, should be > near
	 * @param left 
	 * @param bottom 
	 * @param right 
	 * @param top 
	 * @return the projection matrix
	 */
	public static Mat4f ortho( float near, float far, float left, float bottom, float right, float top ) {
		Mat4f res = new Mat4f();
		
		res.val[0][0] = 2f/(right-left);
		res.val[1][1] = 2f/(top-bottom);
		res.val[2][2] = -2f/(far-near);
		res.val[3][3] = 1f;
		
		res.val[3][0] = -(right+left)/(right-left);
		res.val[3][1] = -(top+bottom)/(top-bottom);
		res.val[3][2] = -(far+near)/(far-near);
		
		return res;
	}
	
	/**
	 * Multiplies 2 matrices
	 * @param m1
	 * @param m2
	 * @return m1*m2
	 */
	public static Mat4f mul( Mat4f m1, Mat4f m2 ) { 
		Mat4f res = new Mat4f();
		
		for( int j=0; j<4; j++ )
			for( int i=0; i<4; i++ )
				res.val[i][j] = (m1.val[0][j]*m2.val[i][0])+
								(m1.val[1][j]*m2.val[i][1])+
								(m1.val[2][j]*m2.val[i][2])+
								(m1.val[3][j]*m2.val[i][3]);
		
		return res;
	}
	
	public String toString() {
		String res = "Mat4f:\n";
		
		for( int j=0; j<4; j++ ) {
			for( int i=0; i<4; i++ ) {
				res+=val[i][j];
				if(i<3) res+=", ";
			}
			if(j<3) res+="\n";
		}
		
		return res;
	}
}
