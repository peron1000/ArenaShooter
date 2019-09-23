package arenashooter.engine.physic.shapes;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;

import arenashooter.engine.graphics.Material;
import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.graphics.GLModel;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec2fi;
import arenashooter.engine.math.Vec4fi;
import arenashooter.game.Main;

public class ShapeCharacter extends PhysicShape {
	private GLModel model;
	private static final Vec2f vec1 = new Vec2f(1);
	
	//   1     -1
	//  / \
	// 0   2
	// |   |
	// |   |
	// 6   3
	// \   /
	//  5-4    +1
	public ShapeCharacter() {
		Vec2[] vertices = {
				new Vec2( -.5f, -.48f), //0
				new Vec2(    0, -1),    //1
				new Vec2(  .5f, -.48f), //2
				new Vec2(  .5f, .55f),  //3
				new Vec2( .05f, 1),     //4
				new Vec2(-.05f, 1),     //5
				new Vec2( -.5f, .55f)   //6
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
				2, 3, 4,
				4, 5, 6
		};
		model = new GLModel(modelData, modelIndices);
	}
	

	private static Material material;
	private Mat4f modelM = new Mat4f();
	@Override
	public void debugDraw(Vec2fi pos, double rot, Vec4fi color) {
		if(material == null)
			material = Main.getRenderer().loadMaterial("data/materials/debug_color.material");
		
		//Create matrices
		Mat4f.transform(pos, rot, vec1, modelM);
		material.setParamMat4f("model", modelM);
		material.setParamMat4f("view", Main.getRenderer().getView());
		material.setParamMat4f("projection", Main.getRenderer().getProj());
		
		material.setParamVec4f("color", color);
		
		if(material.bind(model)) {
			model.bind();
			model.draw(true);
		}
	}
	
	/*
	 * JSON
	 */
	
	@Override
	public JsonObject getJson() {
		return new JsonObject();
	}
}
