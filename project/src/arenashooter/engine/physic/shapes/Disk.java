package arenashooter.engine.physic.shapes;

import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Shader;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.Shape;

public class Disk extends Shape {
	public double radius;
	
	public Disk(double radius) {
		this.radius = radius;
	}
	
	@Override
	public double getMomentOfInertia(double mass) {
		//From Wikipedia: 1/2 mr^2
		return (mass*radius*radius)/2;
	}
	
	@Override
	public Vec2f getAABBextent() {
		return new Vec2f(radius);
	}

	@Override
	public Vec2f project(Vec2f axis) {
		double center = Vec2f.dot(axis, body.position);
		return new Vec2f(center-radius, center+radius);
	}

	private static final Model disk = Model.loadDisk(16);
	private static final Shader shader = Shader.loadShader("data/shaders/debug_color");
	@Override
	public void debugDraw() {
		if(body == null) return;
	
		shader.bind();
		
		//Create matrices
		Mat4f modelM = Mat4f.transform(body.position, body.rotation, new Vec2f( radius*2 ));
		shader.setUniformM4("model", modelM);
		shader.setUniformM4("view", Window.camera.viewMatrix);
		shader.setUniformM4("projection", Window.proj);
		
		shader.setUniformV4("color", new float[]{1,0,0,1});
		
		disk.bindToShader(shader);

		disk.bind();
		disk.draw(true);
	}
}
