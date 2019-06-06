package arenashooter.engine.physic.shapes;

import org.jbox2d.collision.shapes.CircleShape;

import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Shader;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.physic.PhysicWorld;

public class ShapeDisk extends PhysicShape {
	private double radius;
	
	public ShapeDisk(double radius) {
		b2Shape = new CircleShape();
		setRadius(radius);
	}
	
	public double getRadius() { return radius; }
	
	/**
	 * Set the radius of this disk, <b>only use this when it's not in the world</b>
	 * @param extent
	 */
	public void setRadius(double newRadius) {
		this.radius = Math.max(newRadius, PhysicWorld.MIN_BODY_SIZE);
		b2Shape.setRadius((float) this.radius);
	}
	
	private static final Model disk = Model.loadDisk(16);
	private static final Shader shader = Shader.loadShader("data/shaders/debug_color");
	private Mat4f modelM = new Mat4f();
	@Override
	public void debugDraw(Vec2f pos, double rot, Vec4f color) {
		shader.bind();
		
		//Create matrices
		Mat4f.transform(pos, rot, new Vec2f( radius*2 ), modelM);
		shader.setUniformM4("model", modelM);
		shader.setUniformM4("view", Window.getView());
		shader.setUniformM4("projection", Window.proj);
		
		shader.setUniformV4("color", color);
		
		disk.bindToShader(shader);

		disk.bind();
		disk.draw(true);
	}
}
