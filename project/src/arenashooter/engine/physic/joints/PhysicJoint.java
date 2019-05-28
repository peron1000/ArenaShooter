package arenashooter.engine.physic.joints;

import org.jbox2d.dynamics.joints.Joint;

import arenashooter.engine.physic.PhysicWorld;
import arenashooter.engine.physic.bodies.PhysicBody;

public abstract class PhysicJoint {

	protected final PhysicBody a, b;
	protected PhysicWorld world;
	
	public PhysicJoint(PhysicBody a, PhysicBody b) {
		this.a = a;
		this.b = b;
	}
	
	protected abstract Joint getJoint();
	
	/**
	 * Attempt to create this body
	 * @param world
	 */
	public void addToWorld(PhysicWorld world) {
		this.world = world;
		if(getJoint() != null) { //Joint has already been created
			PhysicWorld.log.warn("Trying to create an already existing joint");
			return;
		}
		world.createJoint(this);
	}
	
	/**
	 *  Create this Joint
	 * <br/><b>Do not call this during physic step!</b>
	 * <br/>Only call this from PhysicWorld, otherwise use addToWorld()!
	 */
	public abstract void create();
	
	/**
	 * Mark this body for destruction
	 */
	public void removeFromWorld() {
		if(world == null) return;
		world.destroyJoint(this);
	}
	
	/**
	 * Destroy this body
	 * <br/><b>Do not call this during physic step!</b>
	 * <br/>Only call this from PhysicWorld, otherwise use removeFromWorld()!
	 */
	public abstract void destroy();

}
