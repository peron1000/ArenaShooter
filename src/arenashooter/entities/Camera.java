package arenashooter.entities;

import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec3f;

public class Camera extends Spatial3 {
	public Mat4f viewMatrix = Mat4f.identity();
	
	public Camera(Vec3f position) {
		super(position);
	}
	
	@Override
	public void step(double d) {
		super.step(d);
		viewMatrix = Mat4f.viewMatrix(position, rotation);
	}
	
	
}
