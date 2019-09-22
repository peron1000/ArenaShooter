package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2fi;
import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.StaticBody;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.engine.physic.shapes.ShapeDisk;
import arenashooter.game.Main;

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
	public StaticBodyContainer(Vec2fi worldPosition, Vec2fi extent, double worldRotation) {
		this(new StaticBody(new ShapeBox(extent), worldPosition, worldRotation, CollisionFlags.LANDSCAPE));
	}

	/**
	 * Create a static body with a disk shape
	 * 
	 * @param worldPosition
	 * @param radius        disk radius
	 * @param worldRotation
	 */
	public StaticBodyContainer(Vec2fi worldPosition, double radius, double worldRotation) {
		this(new StaticBody(new ShapeDisk(radius), worldPosition, worldRotation, CollisionFlags.LANDSCAPE));
	}

	
	/*
	 * JSON
	 */
	
	@Override
	protected JsonObject getJson() {
		JsonObject entity = super.getJson();
		
		if(body.getShape() instanceof ShapeBox)
			entity.put("extent", ((ShapeBox)body.getShape()).getExtent());
		else if(body.getShape() instanceof ShapeDisk)
			entity.put("radius", ((ShapeDisk)body.getShape()).getRadius());
		
		return entity;
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
			Main.log.error("Invalid StaticBody definition.");
			sBody = new StaticBody(new ShapeBox(new Vec2f()), new Vec2f(), 0, CollisionFlags.LANDSCAPE);
		}

		StaticBodyContainer container = new StaticBodyContainer(sBody);
		useKeys(container, json);
		return container;
	}
}
