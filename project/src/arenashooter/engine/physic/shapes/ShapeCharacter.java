package arenashooter.engine.physic.shapes;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;

import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Shader;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.PhysicShape;

public class ShapeCharacter extends PhysicShape {
	private Model model;
	private static final Vec2f vec1 = new Vec2f(1);
	
	public ShapeCharacter() {
		Vec2[] vertices = {
				new Vec2( -.5f, -1),
				new Vec2(  .5f, -1),
				new Vec2(  .5f, .55f),
				new Vec2( .05f, 1),
				new Vec2(-.05f, 1),
				new Vec2( -.5f, .55f)
		};
		
		b2Shape = new PolygonShape();
		((PolygonShape)b2Shape).set(vertices, vertices.length);
		
		float[] modelData = new float[vertices.length*8];
		for(int i=0; i<vertices.length; i++) {
			modelData[i*8] = vertices[i].x;
			modelData[(i*8)+1] = vertices[i].y;
		}
		int[] modelIndices = {
				0, 1, 2, 
				1, 2, 3,
				2, 3, 5,
				3, 4, 5
		};
		model = new Model(modelData, modelIndices);
	}
	

	private static final Shader shader = Shader.loadShader("data/shaders/debug_color");
	@Override
	public void debugDraw(Vec2f pos, double rot) {
		shader.bind();
		
		//Create matrices
		Mat4f modelM = Mat4f.transform(pos, rot, vec1);
		shader.setUniformM4("model", modelM);
		shader.setUniformM4("view", Window.getView());
		shader.setUniformM4("projection", Window.proj);
		
		shader.setUniformV4("color", new float[]{1,0,0,1});
		
		model.bindToShader(shader);

		model.bind();
		model.draw(true);
	}
}
