package arenashooter.entities.spatials;

import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.json.EntityTypes;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.StaticBody;
import arenashooter.engine.physic.shapes.PhysicShape;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.engine.physic.shapes.ShapeDisk;

public class StaticBodyContainer extends PhysicBodyContainer<StaticBody> {

	public StaticBodyContainer(StaticBody body) {
		super(body);
	}

	/**
	 * Create a static body with a box shape
	 * @param worldPosition
	 * @param extent box extent
	 * @param worldRotation
	 */
	public StaticBodyContainer(Vec2f worldPosition, Vec2f extent, double worldRotation) {
		this(new StaticBody(new ShapeBox(extent), worldPosition, worldRotation, CollisionFlags.LANDSCAPE));
	}
	
	/**
	 * Create a static body with a disk shape
	 * @param worldPosition
	 * @param radius disk radius
	 * @param worldRotation
	 */
	public StaticBodyContainer(Vec2f worldPosition, double radius, double worldRotation) {
		this(new StaticBody(new ShapeDisk(radius), worldPosition, worldRotation, CollisionFlags.LANDSCAPE));
	}
	
	@Override
	protected EntityTypes getType() {
		PhysicShape shape = getBody().getShape();
		if(shape instanceof ShapeBox) {
			return EntityTypes.STATIC_BOX;
		} else if(shape instanceof ShapeDisk){
			return EntityTypes.STATIC_DISK;
		} else {
			return super.getType();
		}
	}
	
	@Override
	protected JsonObject getJsonObject() {
		JsonObject statiq = super.getJsonObject();
		statiq.putChain("worldPosition", getWorldPos());
		statiq.putChain("worldRotation", getWorldRot());
		return statiq;
	}
}
