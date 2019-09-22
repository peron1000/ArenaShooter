package arenashooter.engine.physic.shapes;

import org.jbox2d.collision.shapes.PolygonShape;

import arenashooter.engine.graphics.MaterialI;
import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.graphics.Model;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec2fi;
import arenashooter.engine.math.Vec4fi;
import arenashooter.engine.physic.PhysicWorld;
import arenashooter.game.Main;

public class ShapeBox extends PhysicShape {
	private Vec2f extent = new Vec2f();
	
	public ShapeBox(Vec2fi extent) {
		b2Shape = new PolygonShape();
		resize(extent);
	}
	
	public Vec2fi getExtent() {
		return extent;
	}
	
	/**
	 * Set the size of this box, <b>only use this when it's not in the world</b>
	 * @param extent
	 */
	public void resize(Vec2fi extent) {
		this.extent.set(extent);
		this.extent.x = (float) Math.max(this.extent.x, PhysicWorld.MIN_BODY_SIZE);
		this.extent.y = (float) Math.max(this.extent.y, PhysicWorld.MIN_BODY_SIZE);
		((PolygonShape)b2Shape).setAsBox(this.extent.x, this.extent.y);
	}

	private static final Model quad = Model.loadQuad();
	private static MaterialI material;
	private Mat4f modelM = new Mat4f();
	@Override
	public void debugDraw(Vec2fi pos, double rot, Vec4fi color) {
		if(material == null)
			material = Main.getRenderer().loadMaterial("data/materials/debug_color.xml");
		
		//Create matrices
		Mat4f.transform(pos, rot, Vec2f.multiply( extent, 2 ), modelM);
		material.setParamMat4f("model", modelM);
		material.setParamMat4f("view", Main.getRenderer().getView());
		material.setParamMat4f("projection", Main.getRenderer().getProj());
		
		material.setParamVec4f("color", color);
		
		if(material.bind(quad)) {
			quad.bind();
			quad.draw(true);
		}
	}
	
	
	/*
	 * JSON
	 */

	@Override
	public JsonObject getJson() {
		JsonObject json = new JsonObject();
		json.put("extent", extent);
		return json;
	}

}
