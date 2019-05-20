package arenashooter.engine.physic;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

import arenashooter.engine.math.Vec2f;

public abstract class PhysicBody {
	protected PhysicShape shape;
	protected BodyDef bodyDef;
	protected Body body;
	protected final CollisionFlags collFlags;
	
	protected PhysicWorld world;
	
	public PhysicBody(PhysicShape shape, Vec2f position, double rotation, CollisionFlags collFlags) {
		this.shape = shape;
		this.collFlags = collFlags;
	}
	
	public abstract void addToWorld(PhysicWorld world);
	
	public void removeFromWorld() {
		world.getB2World().destroyBody(body);
		world = null;
	}
	
	
	public boolean isRotationLocked() { return bodyDef.isFixedRotation(); }
	
	public void setRotationLocked( boolean locked ) {
		bodyDef.setFixedRotation(locked);
		if(body != null)
			body.setFixedRotation(locked);
	}
	
	public boolean isActive() { return body.isActive(); }
	
	public void setActive(boolean active) { body.setActive(active); }
	
	public Vec2f getPosition() {
		if(body == null) return new Vec2f(bodyDef.getPosition());
		return new Vec2f(body.getPosition());
	}
	
	public float getRotation() { return body.getAngle(); }
	
	public void debugDraw() {
		if(body != null)
			shape.debugDraw(getPosition(), getRotation());
	}
	
}
