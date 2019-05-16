package arenashooter.engine.physic.shapes;

import org.jbox2d.collision.shapes.PolygonShape;

import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Shader;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.PhysicShape;

public class ShapeBox extends PhysicShape {
	private Vec2f extent;
	
	public ShapeBox(Vec2f extent) {
		this.extent = extent.clone();
		b2Shape = new PolygonShape();
		((PolygonShape)b2Shape).setAsBox(extent.x, extent.y);
	}
	

	private static final Model quad = Model.loadQuad();
	private static final Shader shader = Shader.loadShader("data/shaders/debug_color");
	@Override
	public void debugDraw(Vec2f pos, double rot) {
		shader.bind();
		
		//Create matrices
		Mat4f modelM = Mat4f.transform(pos, rot, Vec2f.multiply( extent, 2 ));
		shader.setUniformM4("model", modelM);
		shader.setUniformM4("view", Window.getView());
		shader.setUniformM4("projection", Window.proj);
		
		shader.setUniformV4("color", new float[]{1,0,0,1});
		
		quad.bindToShader(shader);

		quad.bind();
		quad.draw(true);
	}
}
