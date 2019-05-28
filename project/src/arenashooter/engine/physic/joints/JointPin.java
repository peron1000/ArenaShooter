package arenashooter.engine.physic.joints;

import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.PhysicWorld;
import arenashooter.engine.physic.bodies.PhysicBody;

public class JointPin extends PhysicJoint {

	private RevoluteJointDef jointDef;
	private RevoluteJoint joint;
	private Vec2f anchorA, anchorB;
	
	public JointPin(PhysicBody a, PhysicBody b, Vec2f localAnchorA, Vec2f localAnchorB) {
		super(a, b);
		
		jointDef = new RevoluteJointDef();
		
		anchorA = localAnchorA.clone();
		anchorB = localAnchorB.clone();
		
		jointDef.userData = this;
	}
	
	@Override
	public void create() {
		if(world == null) return;
		jointDef = new RevoluteJointDef();
		jointDef.bodyA = a.getBody();
		jointDef.bodyB = b.getBody();
		jointDef.localAnchorA.set(anchorA.toB2Vec());
		jointDef.localAnchorB.set(anchorB.toB2Vec());
		jointDef.collideConnected = false;
		
		joint = (RevoluteJoint) world.getB2World().createJoint(jointDef);
	}
	
	public float getAngle() {
		if(joint != null)
			return joint.getJointAngle();
		return jointDef.referenceAngle;
	}
	
	/**
	 * Enable angle limit
	 * @param min
	 * @param max
	 */
	public void enableLimit(float min, float max) {
		jointDef.lowerAngle = min;
		jointDef.upperAngle = max;
		jointDef.enableLimit = true;
		
		if(joint != null) {
			joint.setLimits(min, max);
			joint.enableLimit(true);
		}
	}
	
	/**
	 * Disable angle limit
	 */
	public void disableLimit() {
		jointDef.enableLimit = false;
		
		if(joint != null)
			joint.enableLimit(false);
	}

	@Override
	protected Joint getJoint() {
		return joint;
	}

	@Override
	public void destroy() {
		if(world == null) return;
		if(joint != null) {
			world.getB2World().destroyJoint(joint);
			joint = null;
		} else {
			PhysicWorld.log.warn("Attempting to destroy a joint that was never created");
		}
		world = null;
	}

}
