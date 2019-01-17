package arenashooter.engine.physic.shapes;

import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Shader;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.Shape;
import arenashooter.game.Game;

public class Rectangle extends Shape {
	public Vec2f extent;
	
	public Rectangle(Vec2f extent) {
		this.extent = extent.clone();
	}
	
	@Override
	public double getMomentOfInertia(double mass) {
		//From Wikipedia: 1/12 m * (width^2 + height^2)
		return (mass/12)*((extent.x*2)*(extent.x*2) + (extent.y*2)*(extent.y*2));
	}
	
	@Override
	public Vec2f getAABBextent() {
		return Vec2f.rotate(extent, body.rotation);
	}

	@Override
	public Vec2f project(Vec2f axis) {
		Vec2f extentRotated = Vec2f.rotate(extent, body.rotation);
		Vec2f[] vertices = new Vec2f[] {
			new Vec2f( body.position.x+extentRotated.x, body.position.y+extentRotated.y ),
			new Vec2f( body.position.x-extentRotated.x, body.position.y+extentRotated.y ),
			new Vec2f( body.position.x+extentRotated.x, body.position.y-extentRotated.y ),
			new Vec2f( body.position.x-extentRotated.x, body.position.y-extentRotated.y )
		};
		
		double min = Vec2f.dot(axis, vertices[0]);
		double max = min;
		for (int i = 1; i < vertices.length; i++) {
			double p = Vec2f.dot(axis, vertices[i]);
			if (p < min)
				min = p;
			else if (p > max)
				max = p;
		}
		return new Vec2f(min, max);
	}

	private static final Model quad = Model.loadQuad();
	private static final Shader shader = new Shader("data/shaders/debug_color");
	@Override
	public void debugDraw() {
		if(body == null) return;
		
		shader.bind();
		
		//Create matrices
		Mat4f modelM = Mat4f.transform(body.position, body.rotation, Vec2f.multiply( extent, 2 ));
		shader.setUniformM4("model", modelM);
		shader.setUniformM4("view", Game.camera.viewMatrix);
		shader.setUniformM4("projection", Window.proj);
		
		shader.setUniformV4("color", new float[]{1,0,0,1});
		
		quad.bindToShader(shader);

		quad.bind();
		quad.draw(true);
	}
}
