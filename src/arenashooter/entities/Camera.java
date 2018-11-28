package arenashooter.entities;

import arenashooter.engine.math.Mat4f;

public class Camera extends Spatial3 {
	
	public Mat4f viewMatrix = Mat4f.identity();
	
	@Override
	public void step(double d) {
		super.step(d);
		viewMatrix = Mat4f.viewMatrix(position, rotation);
	}
}
