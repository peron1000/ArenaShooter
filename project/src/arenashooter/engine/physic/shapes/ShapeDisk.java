package arenashooter.engine.physic.shapes;

import org.jbox2d.collision.shapes.CircleShape;

import arenashooter.engine.graphics.MaterialI;
import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec2fi;
import arenashooter.engine.math.Vec4fi;
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
	private static MaterialI material;
	private Mat4f modelM = new Mat4f();
	@Override
	public void debugDraw(Vec2fi pos, double rot, Vec4fi color) {
		if(material == null)
			material = Window.loadMaterial("data/materials/debug_color.xml");
		
		//Create matrices
		Mat4f.transform(pos, rot, new Vec2f( radius*2 ), modelM);
		material.setParamMat4f("model", modelM);
		material.setParamMat4f("view", Window.getView());
		material.setParamMat4f("projection", Window.getProj());
		
		material.setParamVec4f("color", color);
		
		if(material.bind(disk)) {
			disk.bind();
			disk.draw(true);
		}
	}
	
	
	/*
	 * JSON
	 */

	@Override
	public JsonObject getJson() {
		JsonObject json = new JsonObject();
		json.put("radius", radius);
		return json;
	}
}
