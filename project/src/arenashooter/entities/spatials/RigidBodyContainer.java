package arenashooter.entities.spatials;

import java.util.Set;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.DamageInfo;
import arenashooter.engine.DamageType;
import arenashooter.engine.json.StrongJsonKey;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec2fi;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.engine.physic.shapes.ShapeDisk;
import arenashooter.game.Main;

public class RigidBodyContainer extends PhysicBodyContainer<RigidBody> {

	public RigidBodyContainer(RigidBody body) {
		super(body);
	}

	/**
	 * @return linear velocity at center of mass
	 */
	public Vec2fi getLinearVelocity() {
		return body.getLinearVelocity();
	}

	/**
	 * Set linear velocity at center of mass
	 * 
	 * @param newVelocity
	 */
	public void setLinearVelocity(Vec2fi newVelocity) {
		body.setLinearVelocity(newVelocity);
	}

	public float getAngularVelocity() {
		return body.getAngularVelocity();
	}

	public void setAngularVelocity(float angularVelocity) {
		body.setAngularVelocity(angularVelocity);
	}

	/**
	 * Apply an impulse at center of mass
	 * 
	 * @param impulse
	 */
	public void applyImpulse(Vec2fi impulse) {
		body.applyImpulse(impulse);
	}

	/**
	 * Apply an impulse at location
	 * 
	 * @param impulse
	 * @param location world position
	 */
	public void applyImpulse(Vec2fi impulse, Vec2fi location) {
		body.applyImpulse(impulse, location);
	}

	/**
	 * Apply an impulse depending on damage received. <br/>
	 * Detach if out of bounds
	 */
	@Override
	public float takeDamage(DamageInfo info) { // TODO: Get impact location
		applyImpulse(Vec2f.multiply(info.direction, info.impulse));

		// Destroy when out of bounds
		if (info.dmgType == DamageType.OUT_OF_BOUNDS) {
			if (ignoreKillBounds)
				return 0;
			else
				detach();
		}

		return 0;
	}

	@Override
	public void step(double d) {
		super.step(d);

		// Destroy when out of bounds
		if (getArena() != null && (getWorldPos().x() < getArena().killBound.x || getWorldPos().x() > getArena().killBound.z
				|| getWorldPos().y() < getArena().killBound.y || getWorldPos().y() > getArena().killBound.w))
			takeDamage(new DamageInfo(0, DamageType.OUT_OF_BOUNDS, new Vec2f(), 0, null));

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

	@Override
	public Set<StrongJsonKey> getJsonKey() {
		Set<StrongJsonKey> set = super.getJsonKey();
		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				return body.getFriction();
			}
			@Override
			public String getKey() {
				return "friction";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				float friction = json.getFloat(this);
				body = new RigidBody(body.getShape(), body.getPosition(), body.getRotation(), CollisionFlags.RIGIDBODY,
						body.getDensity(), friction);
			}
		});
		set.add(new StrongJsonKey() {
			@Override
			public Object getValue() {
				return body.getDensity();
			}
			@Override
			public String getKey() {
				return "density";
			}
			@Override
			public void useKey(JsonObject json) throws Exception {
				float density = json.getFloat(this);
				body = new RigidBody(body.getShape(), body.getPosition(), body.getRotation(), CollisionFlags.RIGIDBODY,
						density, body.getFriction());
			}
		});
		return set;
	}

	public static RigidBodyContainer fromJson(JsonObject json) {
		RigidBody rBody;
		if (json.containsKey("radius")) {
			double radius = ((Number) json.get("radius")).doubleValue();
			rBody = new RigidBody(new ShapeDisk(radius), new Vec2f(), 0, CollisionFlags.RIGIDBODY, 0, 0);
		} else if (json.containsKey("extent")) {
			JsonArray array = (JsonArray) json.get("extent");
			Vec2f extent = Vec2f.jsonImport(array);
			rBody = new RigidBody(new ShapeBox(extent), new Vec2f(), 0, CollisionFlags.RIGIDBODY, 0, 0);
		} else {
			Main.log.error("Invalid RigidBody definition.");
			rBody = new RigidBody(new ShapeBox(new Vec2f()), new Vec2f(), 0, CollisionFlags.RIGIDBODY, 0, 0);
		}

		RigidBodyContainer container = new RigidBodyContainer(rBody);
		useKeys(container, json);
		return container;
	}

}
