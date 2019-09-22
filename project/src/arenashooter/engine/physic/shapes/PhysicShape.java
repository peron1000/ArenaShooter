package arenashooter.engine.physic.shapes;

import java.io.IOException;
import java.io.Writer;

import org.jbox2d.collision.shapes.Shape;

import arenashooter.engine.math.Vec2fi;
import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4fi;

public abstract class PhysicShape implements Jsonable {
	protected Shape b2Shape;
	
	public Shape getB2Shape() { return b2Shape; }
	
	public abstract void debugDraw(Vec2fi pos, double rot, Vec4fi color);
	
	
	/*
	 * JSON
	 */
	
	public abstract JsonObject getJson();
	
	@Override
	public String toJson() {
		return getJson().toJson();
	}
	
	@Override
	public void toJson(Writer writable) throws IOException {
		getJson().toJson(writable);
	}
	
	public static PhysicShape fromJson(JsonObject json) {
		if(json.containsKey("radius")) {
			double radius = ((Number) json.get("radius")).doubleValue();
			return new ShapeDisk(radius);
		} else if(json.containsKey("extent")) {
			Vec2f extent = Vec2f.jsonImport((JsonArray) json.get("extent"));
			return new ShapeBox(extent );
		} else {
			return new ShapeCharacter();
		}
	}
}
