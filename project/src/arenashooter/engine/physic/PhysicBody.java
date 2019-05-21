package arenashooter.engine.physic;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.Fixture;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.spatials.Spatial;

public abstract class PhysicBody {
	protected PhysicShape shape;
	protected BodyDef bodyDef;
	protected Body body;
	protected final CollisionFlags collFlags;
	protected Fixture fixture;
	protected Spatial userData = null;
	
	private boolean isSensor = false;
	
	protected PhysicWorld world;
	
	public PhysicBody(PhysicShape shape, Vec2f position, double rotation, CollisionFlags collFlags) {
		this.shape = shape;
		this.collFlags = collFlags;

		bodyDef = new BodyDef();
	}
	
	/**
	 * Set user data for this body
	 * @param container Entity using this body
	 */
	public void setUserData(Spatial container) {
		userData = container;
		bodyDef.setUserData(userData);
		if(body != null) body.setUserData(userData);
		if(fixture != null) fixture.setUserData(userData);
		System.out.println(bodyDef.getUserData());
	}
	
	public abstract void addToWorld(PhysicWorld world);
	
	public void removeFromWorld() {
		if(body == null || world == null) return;
		world.getB2World().destroyBody(body);
		world = null;
	}
	
	public Body getBody() { return body; }
	
	public boolean isRotationLocked() { return bodyDef.isFixedRotation(); }
	
	public void setRotationLocked( boolean locked ) {
		bodyDef.setFixedRotation(locked);
		if(body != null)
			body.setFixedRotation(locked);
	}
	
	public boolean isSensor() {
		return isSensor;
	}
	
	/**
	 * Set this body as a sensor. <b>Only works if the body isn't attached to a world yet</b>
	 * @param isSensor
	 */
	public void setIsSensor(boolean isSensor) {
		if(body == null)
			this.isSensor = isSensor;
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
