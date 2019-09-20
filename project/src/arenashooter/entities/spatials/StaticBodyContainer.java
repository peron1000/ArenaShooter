package arenashooter.entities.spatials;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.StaticBody;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.engine.physic.shapes.ShapeDisk;

public class StaticBodyContainer extends PhysicBodyContainer<StaticBody> {

	public StaticBodyContainer(StaticBody body) {
		super(body);
	}

	/**
	 * Create a static body with a box shape
	 * 
	 * @param worldPosition
	 * @param extent        box extent
	 * @param worldRotation
	 */
	public StaticBodyContainer(Vec2f worldPosition, Vec2f extent, double worldRotation) {
		this(new StaticBody(new ShapeBox(extent), worldPosition, worldRotation, CollisionFlags.LANDSCAPE));
	}

	/**
	 * Create a static body with a disk shape
	 * 
	 * @param worldPosition
	 * @param radius        disk radius
	 * @param worldRotation
	 */
	public StaticBodyContainer(Vec2f worldPosition, double radius, double worldRotation) {
		this(new StaticBody(new ShapeDisk(radius), worldPosition, worldRotation, CollisionFlags.LANDSCAPE));
	}

	public static StaticBodyContainer fromJson(JsonObject json) {
		StaticBody sBody;
		if (json.containsKey("radius")) {
			double radius = ((Number) json.get("radius")).doubleValue();
			sBody = new StaticBody(new ShapeDisk(radius), new Vec2f(), 0, CollisionFlags.LANDSCAPE);
		} else if(json.containsKey("extent")) {
			JsonArray array = (JsonArray) json.get("extent");
			Vec2f extent = Vec2f.jsonImport(array );
			sBody = new StaticBody(new ShapeBox(extent), new Vec2f(), 0, CollisionFlags.LANDSCAPE);
		} else {
			sBody = new StaticBody(new ShapeBox(new Vec2f(10)), new Vec2f(), 0, CollisionFlags.LANDSCAPE);
		}
		return new StaticBodyContainer(sBody);
	}
}
