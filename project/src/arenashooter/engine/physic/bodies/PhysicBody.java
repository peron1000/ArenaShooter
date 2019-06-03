package arenashooter.engine.physic.bodies;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.joints.JointEdge;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.PhysicWorld;
import arenashooter.engine.physic.shapes.PhysicShape;
import arenashooter.engine.physic.joints.PhysicJoint;
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
	 * 
	 * @param container Entity using this body
	 */
	public void setUserData(Spatial container) {
		userData = container;
		bodyDef.setUserData(userData);
		if (body != null)
			body.setUserData(userData);
		if (fixture != null)
			fixture.setUserData(userData);
	}

	/**
	 * Get this body's shape, <b>only modify it if hasn't been added to the world
	 * yet!</b>
	 * 
	 * @return
	 */
	public PhysicShape getShape() {
		return shape;
	}

	/**
	 * Attempt to create this body
	 * 
	 * @param world
	 */
	public void addToWorld(PhysicWorld world) {
		this.world = world;
		if (body != null) {
			PhysicWorld.log.warn("Trying to create an already existing body");
			return;
		}
		world.createBody(this);
	}

	/**
	 * Create this body <br/>
	 * <b>Do not call this during physic step!</b> <br/>
	 * Only call this from PhysicWorld, otherwise use addToWorld()!
	 */
	public abstract Body create();

	/**
	 * Mark this body for destruction
	 */
	public void removeFromWorld() {
		if (world == null || body == null)
			return;

		// Destroy all joints first
		JointEdge jointE = body.getJointList();
		while (jointE != null) {
			((PhysicJoint) jointE.joint.getUserData()).removeFromWorld();
			jointE = jointE.next;
		}

		world.destroyBody(this);
	}

	/**
	 * Destroy this body <br/>
	 * <b>Do not call this during physic step!</b> <br/>
	 * Only call this from PhysicWorld, otherwise use removeFromWorld()!
	 */
	public void destroy() {
		if (world == null)
			return;
		if (body != null) {
			world.getB2World().destroyBody(body);
			body = null;
		} else {
			PhysicWorld.log.warn("Attempting to destroy a body that was never created");
		}
		if (fixture != null) {
			fixture.destroy();
			fixture = null;
		} else {
			PhysicWorld.log.warn("Attempting to destroy a body that was never created");
		}
		world = null;
	}

	public Body getBody() {
		return body;
	}

	public boolean isRotationLocked() {
		return bodyDef.isFixedRotation();
	}

	public void setRotationLocked(boolean locked) {
		bodyDef.setFixedRotation(locked);
		if (body != null)
			body.setFixedRotation(locked);
	}

	public boolean isSensor() {
		return isSensor;
	}

	/**
	 * Set this body as a sensor. <b>Only works if the body isn't attached to a
	 * world yet</b>
	 * 
	 * @param isSensor
	 */
	public void setIsSensor(boolean isSensor) {
		if (body == null)
			this.isSensor = isSensor;
	}

	public boolean isActive() {
		if (body != null)
			return body.isActive();
		return bodyDef.isActive();
	}

	public void setActive(boolean active) {
		bodyDef.setActive(active);
		if (body != null)
			body.setActive(active);
	}

	/**
	 * Set rotation (only works before body creation)
	 * 
	 * @param angle
	 */
	public void setRotation(float angle) {
		bodyDef.setAngle(angle);
	}

	public Vec2f getPosition() {
		if (body == null)
			return new Vec2f(bodyDef.getPosition());
		return new Vec2f(body.getPosition());
	}

	public float getRotation() {
		if (body == null)
			return bodyDef.getAngle();
		return body.getAngle();
	}

	public void debugDraw() {
		if (body != null) {
			shape.debugDraw(getPosition(), getRotation());
		}
	}

}
