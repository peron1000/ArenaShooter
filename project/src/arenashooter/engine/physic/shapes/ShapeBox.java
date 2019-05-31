package arenashooter.engine.physic.shapes;

import org.jbox2d.collision.shapes.PolygonShape;

import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Shader;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;

public class ShapeBox extends PhysicShape {
	private Vec2f extent;
	
	public ShapeBox(Vec2f extent) {
		this.extent = extent.clone();
		b2Shape = new PolygonShape();
		((PolygonShape)b2Shape).setAsBox(extent.x, extent.y);
	}
	
	/**
	 * Set the size of this box, <b>only use this when it's not in the world</b>
	 * @param extent
	 */
	public void resize(Vec2f extent) {
		this.extent.set(extent);
		((PolygonShape)b2Shape).setAsBox(this.extent.x, this.extent.y);
	}

	private static final Model quad = Model.loadQuad();
	private static final Shader shader = Shader.loadShader("data/shaders/debug_color");
	private Mat4f modelM = new Mat4f();
	@Override
	public void debugDraw(Vec2f pos, double rot) {
		shader.bind();
		
		//Create matrices
		Mat4f.transform(pos, rot, Vec2f.multiply( extent, 2 ), modelM);
		shader.setUniformM4("model", modelM);
		shader.setUniformM4("view", Window.getView());
		shader.setUniformM4("projection", Window.proj);
		
		shader.setUniformV4("color", new float[]{1,0,0,1});
		
		quad.bindToShader(shader);

		quad.bind();
		quad.draw(true);
	}
}
