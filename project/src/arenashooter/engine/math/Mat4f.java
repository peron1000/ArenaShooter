package arenashooter.engine.math;

/**
 * Mutable 4*4 matrix of floats
 */
public class Mat4f implements Mat4fi {

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
	public final float[][] val;
	
	public Mat4f() {
		val = new float[4][4];
	}
	
	public Mat4f( Mat4fi m ) {
		val = new float[][]{
			{m.m00(), m.m10(), m.m20(), m.m30()},
			{m.m01(), m.m11(), m.m21(), m.m31()},
			{m.m02(), m.m12(), m.m22(), m.m32()},
			{m.m03(), m.m13(), m.m23(), m.m33()}
		};
	}
	
	public float m00() { return val[0][0]; }
	public float m10() { return val[1][0]; }
	public float m20() { return val[2][0]; }
	public float m30() { return val[3][0]; }
	
	public float m01() { return val[0][1]; }
	public float m11() { return val[1][1]; }
	public float m21() { return val[2][1]; }
	public float m31() { return val[3][1]; }
	
	public float m02() { return val[0][2]; }
	public float m12() { return val[1][2]; }
	public float m22() { return val[2][2]; }
	public float m32() { return val[3][2]; }
	
	public float m03() { return val[0][3]; }
	public float m13() { return val[1][3]; }
	public float m23() { return val[2][3]; }
	public float m33() { return val[3][3]; }
	
	@Override
	public float[] toArray(float[] target) {
		for(int j=0; j<4; j++)
			for(int i=0; i<4; i++)
				target[ (i*4)+j ] = val[i][j];
		
		return target;
	}
	
	/**
	 * @return new identity matrix
	 */
	public static Mat4f identity() {
		Mat4f res = new Mat4f();
		res.val[0][0] = 1;
		res.val[1][1] = 1;
		res.val[2][2] = 1;
		res.val[3][3] = 1;
		return res;
	}
	
	/**
	 * Set <i>this</i> to identity and return it
	 * @return <i>this</i> (modified)
	 */
	public Mat4f setToIdentity() {
		val[0][0] = 1;
		val[0][1] = 0;
		val[0][2] = 0;
		val[0][3] = 0;

		val[1][0] = 0;
		val[1][1] = 1;
		val[1][2] = 0;
		val[1][3] = 0;

		val[2][0] = 0;
		val[2][1] = 0;
		val[2][2] = 1;
		val[2][3] = 0;

		val[3][0] = 0;
		val[3][1] = 0;
		val[3][2] = 0;
		val[3][3] = 1;
		
		return this;
	}

	/**
	 * Create a rotation matrix
	 * @param q unit quaternion
	 * @return
	 */
	public static Mat4f rotation(QuatI q) {
		Mat4f res = new Mat4f();
		
		double ww = q.w() * q.w();
        double xx = q.x() * q.x();
        double yy = q.y() * q.y();
        double zz = q.z() * q.z();
        double zw = q.z() * q.w();
        double xy = q.x() * q.y();
        double xz = q.x() * q.z();
        double yw = q.y() * q.w();
        double yz = q.y() * q.z();
        double xw = q.x() * q.w();
		
		//First column
        res.val[0][0] = (float) (ww + xx - zz - yy);
        res.val[0][1] = (float) (xy + zw + zw + xy);
        res.val[0][2] = (float) (xz - yw + xz - yw);
		
		//Second column
        res.val[1][0] = (float) (-zw + xy - zw + xy);
        res.val[1][1] = (float) (yy - zz + ww - xx);
        res.val[1][2] = (float) (yz + yz + xw + xw);

		//Third column
        res.val[2][0] = (float) (yw + xz + xz + yw);
        res.val[2][1] = (float) (yz + yz - xw - xw);
        res.val[2][2] = (float) (zz - yy - xx + ww);
		
        //Fourth column
		res.val[3][3] = 1;
		
		return res;
	}
	
	/**
	 * Create a rotation matrix
	 * @param angle
	 * @return
	 */
	public static Mat4f rotation(float angle) {
		Mat4f res = new Mat4f();
		
		float w = (float)Math.cos(-angle/2);
		float z = (float)Math.sin(-angle/2);
		
		double ww = w * w;
        double zz = z * z;
        double zw = z * w;
		
		//First column
        res.val[0][0] = (float) (ww - zz);
        res.val[0][1] = (float) (zw + zw);
		
		//Second column
        res.val[1][0] = (float) (-zw - zw);
        res.val[1][1] = (float) (-zz + ww);

		//Third column
        res.val[2][2] = (float) (zz + ww);
		
        //Fourth column
		res.val[3][3] = 1;
		
		return res;
	}
	
	/**
	 * <i>Target</i> becomes a translation matrix for <i>v</i>
	 * @param v
	 * @param target
	 * @return <i>target</i>
	 */
	public static Mat4f translation(Vec3fi v, Mat4f target) {
		target.setToIdentity();

		target.val[3][0] = v.x();
		target.val[3][1] = v.y();
		target.val[3][2] = v.z();
		
		return target;
	}
	
	/**
	 * Create a translation matrix
	 * @param v
	 * @return
	 */
	public static Mat4f translation(Vec3fi v) {
		return translation(v, new Mat4f());
	}
	
	/**
	 * <i>Target</i> becomes a translation matrix for <i>v</i>
	 * @param v
	 * @param target
	 * @return <i>target</i>
	 */
	public static Mat4f translation(Vec2fi v, Mat4f target) {
		target.setToIdentity();

		target.val[3][0] = v.x();
		target.val[3][1] = v.y();
		
		return target;
	}

	/**
	 * Create a translation matrix
	 * @param v
	 * @return
	 */
	public static Mat4f translation(Vec2fi v) {
		return translation(v, new Mat4f());
	}
	
	/**
	 * Translate this matrix
	 * @param v translation vector
	 * @return <i>this</i> translated
	 */
	public Mat4f translate(Vec2fi v) {
		val[3][0] = val[0][0]*v.x() + val[1][0]*v.y() + val[3][0];
		val[3][1] = val[0][1]*v.x() + val[1][1]*v.y() + val[3][1];
		val[3][2] = val[0][2]*v.x() + val[1][2]*v.y() + val[3][2];
		val[3][3] = val[0][3]*v.x() + val[1][3]*v.y() + val[3][3];

		return this;
	}
	
	/**
	 * Translate this matrix
	 * @param v translation vector
	 * @return <i>this</i> translated
	 */
	public Mat4f translate(Vec3fi v) {
		val[3][0] = val[0][0]*v.x() + val[1][0]*v.y() + val[2][0]*v.z() + val[3][0];
		val[3][1] = val[0][1]*v.x() + val[1][1]*v.y() + val[2][1]*v.z() + val[3][1];
		val[3][2] = val[0][2]*v.x() + val[1][2]*v.y() + val[2][2]*v.z() + val[3][2];
		val[3][3] = val[0][3]*v.x() + val[1][3]*v.y() + val[2][3]*v.z() + val[3][3];

		return this;
	}
	
	/**
	 * Create a scaling matrix
	 * @param v
	 * @return
	 */
	public static Mat4f scaling(Vec3fi v) {
		Mat4f res = new Mat4f();
		
		res.val[0][0] = v.x();
		res.val[1][1] = v.y();
		res.val[2][2] = v.z();
		res.val[3][3] = 1;
		
		return res;
	}
	
	/**
	 * Scale this matrix
	 * @param v scaling vector
	 * @return <i>this</i> scaled
	 */
	public Mat4f scale(Vec2fi v) {
		float m00 = val[0][0];
		float m10 = val[1][0];
		float m01 = val[0][1];
		float m11 = val[1][1];
		float m02 = val[0][2];
		float m12 = val[1][2];
		float m03 = val[0][3];
		float m13 = val[1][3];
		
		val[0][0] = m00*v.x();
		val[1][0] = m10*v.y();
		
		val[0][1] = m01*v.x();
		val[1][1] = m11*v.y();
		
		val[0][2] = m02*v.x();
		val[1][2] = m12*v.y();
		
		val[0][3] = m03*v.x();
		val[1][3] = m13*v.y();
		
		return this;
	}
	
	/**
	 * Create a scaling matrix
	 * @param v
	 * @return
	 */
	public static Mat4f scaling(Vec2fi v) {
		Mat4f res = new Mat4f();
		
		res.val[0][0] = v.x();
		res.val[1][1] = v.y();
		res.val[2][2] = 1;
		res.val[3][3] = 1;
		
		return res;
	}
	
	/**
	 * Scale this matrix
	 * @param v scaling vector
	 * @return <i>this</i> scaled
	 */
	public Mat4f scale(Vec3fi v) {
		val[0][0] = m00()*v.x();
		val[1][0] = m10()*v.y();
		val[2][0] = m20()*v.z();
		
		val[0][1] = m01()*v.x();
		val[1][1] = m11()*v.y();
		val[2][1] = m21()*v.z();
		
		val[0][2] = m02()*v.x();
		val[1][2] = m12()*v.y();
		val[2][2] = m22()*v.z();
		
		val[0][3] = m03()*v.x();
		val[1][3] = m13()*v.y();
		val[2][3] = m23()*v.z();
		
		return this;
	}
	
	/**
	 * Create a transform matrix for a 3D object
	 * @param loc
	 * @param rot
	 * @param scale
	 * @param target
	 * @return <i>target</i>
	 */
	public static Mat4f transform( Vec3fi loc, QuatI rot, Vec3fi scale, Mat4f target ) {
		target.setToIdentity().translate(loc);
		target = mul(target, rotation(rot)); //TODO: Remove matrix creation for rotation
		target.scale(scale);
		return target;
	}
	
	/**
	 * Create a transform matrix for a 2D object and stores the result in <i>target</i>
	 * <br/> Avoids object creation
	 * @param loc
	 * @param rot
	 * @param scale
	 * @param target
	 * @return <i>target</i> (modified)
	 */
	public static Mat4f transform( Vec2fi loc, double rot, Vec2fi scale, Mat4f target ) {
		double w = Math.cos(rot/2);
		double z = Math.sin(rot/2);
		
		double ww = w * w;
		double zz = z * z;
		double zw2 = 2*(z * w);
        
        //First column
        target.val[0][0] = (float)((ww - zz)*scale.x());
        target.val[0][1] = (float)(zw2*scale.x());
        target.val[0][2] = 0;
        target.val[0][3] = 0;
		//Second column
        target.val[1][0] = (float)(-zw2*scale.y());
        target.val[1][1] = (float)((-zz + ww)*scale.y());
        target.val[1][2] = 0;
        target.val[1][3] = 0;
		//Third column
        target.val[2][0] = 0;
        target.val[2][1] = 0;
        target.val[2][2] = (float)(zz + ww);
        target.val[2][3] = 0;
        //Fourth column
        target.val[3][0] = loc.x();
        target.val[3][1] = loc.y();
        target.val[3][2] = 0;
        target.val[3][3] = 1;
        
		return target;
	}
	
	/**
	 * Create a view matrix
	 * @param loc
	 * @param rot
	 * @param target
	 * @return <i>target</i> (modified)
	 */
	public static Mat4f viewMatrix(Vec3fi loc, QuatI rot, Mat4f target) {
		target.setToIdentity();
		target.val[3][0] = -loc.x();
		target.val[3][1] = -loc.y();
		target.val[3][2] = -loc.z();
		return mul(rotation(Quat.conjugate(rot)), target, target); //TODO: Remove new Quat() caused by conjugate
	}
	
	/**
	 * Transpose a matrix
	 * @param m
	 * @return m transposed
	 */
	public static Mat4f transpose( Mat4fi m ) {
		Mat4f res = new Mat4f();
		
		res.val[0][0] = m.m00();
		res.val[1][0] = m.m01();
		res.val[2][0] = m.m02();
		res.val[3][0] = m.m03();
		
		res.val[0][1] = m.m10();
		res.val[1][1] = m.m11();
		res.val[2][1] = m.m12();
		res.val[3][1] = m.m13();
		
		res.val[0][2] = m.m20();
		res.val[1][2] = m.m21();
		res.val[2][2] = m.m22();
		res.val[3][2] = m.m23();
		
		res.val[0][3] = m.m30();
		res.val[1][3] = m.m31();
		res.val[2][3] = m.m32();
		res.val[3][3] = m.m33();
		
		return res;
	}
	
	/**
	 * Set <i>target</i> to a symmetric perspective projection matrix
	 * 
	 * @param near clip plane distance, should be > 0
	 * @param far clip plane distance, should be > near
	 * @param yFOV vertical field of view (degrees)
	 * @param ratio aspect ratio (width/height)
	 * @param target
	 * @return <i>target</i>
	 */
	public static Mat4f perspective( float near, float far, float yFOV, float ratio, Mat4f target ) {
		target.setToIdentity();
		
		float top = (float) (Math.tan(Math.toRadians(yFOV)/2)*near);
		float right = top*ratio;
		target.val[0][0] = near/right;
		target.val[1][1] = near/-top;
		target.val[2][2] = -(far+near)/(far-near);
		target.val[3][2] = -(2*far*near)/(far-near);
		target.val[2][3] = -1;
		
		return target;
	}
	
	/**
	 * Set <i>target</i> to an orthographic projection matrix
	 * 
	 * @param near clip plane distance, should be > 0
	 * @param far clip plane distance, should be > near
	 * @param left 
	 * @param bottom 
	 * @param right 
	 * @param top 
	 * @param target
	 * @return <i>target</i>
	 */
	public static Mat4f ortho( float near, float far, float left, float bottom, float right, float top, Mat4f target ) {
		target.setToIdentity();
		
		target.val[0][0] = 2f/(right-left);
		target.val[1][1] = 2f/(top-bottom);
		target.val[2][2] = -2f/(far-near);
		target.val[3][3] = 1f;
		
		target.val[3][0] = -(right+left)/(right-left);
		target.val[3][1] = -(top+bottom)/(top-bottom);
		target.val[3][2] = -(far+near)/(far-near);
		
		return target;
	}
	
	/**
	 * Multiplies 2 matrices and stores the result in <i>target</i>
	 * @param m1
	 * @param m2
	 * @param target
	 * @return <i>target</i> (modified)
	 */
	public static Mat4f mul( Mat4fi m1, Mat4fi m2, Mat4f target ) {
		float[] val1 = m1.toArray(new float[16]), val2 = m2.toArray(new float[16]);
		
		target.val[0][0] = val1[0]*val2[0]  + val1[4]*val2[1]  + val1[8]*val2[2]  + val1[12]*val2[3];
		target.val[1][0] = val1[0]*val2[4]  + val1[4]*val2[5]  + val1[8]*val2[6]  + val1[12]*val2[7];
		target.val[2][0] = val1[0]*val2[8]  + val1[4]*val2[9]  + val1[8]*val2[10] + val1[12]*val2[11];
		target.val[3][0] = val1[0]*val2[12] + val1[4]*val2[13] + val1[8]*val2[14] + val1[12]*val2[15];
		
		target.val[0][1] = val1[1]*val2[0]  + val1[5]*val2[1]  + val1[9]*val2[2]  + val1[13]*val2[3];
		target.val[1][1] = val1[1]*val2[4]  + val1[5]*val2[5]  + val1[9]*val2[6]  + val1[13]*val2[7];
		target.val[2][1] = val1[1]*val2[8]  + val1[5]*val2[9]  + val1[9]*val2[10] + val1[13]*val2[11];
		target.val[3][1] = val1[1]*val2[12] + val1[5]*val2[13] + val1[9]*val2[14] + val1[13]*val2[15];
		
		target.val[0][2] = val1[2]*val2[0]  + val1[6]*val2[1]  + val1[10]*val2[2]  + val1[14]*val2[3];
		target.val[1][2] = val1[2]*val2[4]  + val1[6]*val2[5]  + val1[10]*val2[6]  + val1[14]*val2[7];
		target.val[2][2] = val1[2]*val2[8]  + val1[6]*val2[9]  + val1[10]*val2[10] + val1[14]*val2[11];
		target.val[3][2] = val1[2]*val2[12] + val1[6]*val2[13] + val1[10]*val2[14] + val1[14]*val2[15];
		
		target.val[0][3] = val1[3]*val2[0]  + val1[7]*val2[1]  + val1[11]*val2[2]  + val1[15]*val2[3];
		target.val[1][3] = val1[3]*val2[4]  + val1[7]*val2[5]  + val1[11]*val2[6]  + val1[15]*val2[7];
		target.val[2][3] = val1[3]*val2[8]  + val1[7]*val2[9]  + val1[11]*val2[10] + val1[15]*val2[11];
		target.val[3][3] = val1[3]*val2[12] + val1[7]*val2[13] + val1[11]*val2[14] + val1[15]*val2[15];
		
		return target;
	}
	
	/**
	 * Multiplies 2 matrices
	 * @param m1
	 * @param m2
	 * @return m1*m2
	 */
	public static Mat4f mul( Mat4fi m1, Mat4fi m2 ) {
		Mat4f res = new Mat4f();
		
		res.val[0][0] = m1.m00()*m2.m00() + m1.m10()*m2.m01() + m1.m20()*m2.m02() + m1.m30()*m2.m03();
		res.val[1][0] = m1.m00()*m2.m10() + m1.m10()*m2.m11() + m1.m20()*m2.m12() + m1.m30()*m2.m13();
		res.val[2][0] = m1.m00()*m2.m20() + m1.m10()*m2.m21() + m1.m20()*m2.m22() + m1.m30()*m2.m23();
		res.val[3][0] = m1.m00()*m2.m30() + m1.m10()*m2.m31() + m1.m20()*m2.m32() + m1.m30()*m2.m33();
		
		res.val[0][1] = m1.m01()*m2.m00() + m1.m11()*m2.m01() + m1.m21()*m2.m02() + m1.m31()*m2.m03();
		res.val[1][1] = m1.m01()*m2.m10() + m1.m11()*m2.m11() + m1.m21()*m2.m12() + m1.m31()*m2.m13();
		res.val[2][1] = m1.m01()*m2.m20() + m1.m11()*m2.m21() + m1.m21()*m2.m22() + m1.m31()*m2.m23();
		res.val[3][1] = m1.m01()*m2.m30() + m1.m11()*m2.m31() + m1.m21()*m2.m32() + m1.m31()*m2.m33();
		
		res.val[0][2] = m1.m02()*m2.m00() + m1.m12()*m2.m01() + m1.m22()*m2.m02() + m1.m32()*m2.m03();
		res.val[1][2] = m1.m02()*m2.m10() + m1.m12()*m2.m11() + m1.m22()*m2.m12() + m1.m32()*m2.m13();
		res.val[2][2] = m1.m02()*m2.m20() + m1.m12()*m2.m21() + m1.m22()*m2.m22() + m1.m32()*m2.m23();
		res.val[3][2] = m1.m02()*m2.m30() + m1.m12()*m2.m31() + m1.m22()*m2.m32() + m1.m32()*m2.m33();
		
		res.val[0][3] = m1.m03()*m2.m00() + m1.m13()*m2.m01() + m1.m23()*m2.m02() + m1.m33()*m2.m03();
		res.val[1][3] = m1.m03()*m2.m10() + m1.m13()*m2.m11() + m1.m23()*m2.m12() + m1.m33()*m2.m13();
		res.val[2][3] = m1.m03()*m2.m20() + m1.m13()*m2.m21() + m1.m23()*m2.m22() + m1.m33()*m2.m23();
		res.val[3][3] = m1.m03()*m2.m30() + m1.m13()*m2.m31() + m1.m23()*m2.m32() + m1.m33()*m2.m33();
		
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
	
	@Override
	public Mat4f clone() {
		return new Mat4f(this);
	}
}
