package arenashooter.engine.physic.bodies;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.Body;
import arenashooter.engine.physic.Shape;

/**
 * Fully simulated object
 */
public class RigidBody extends Body {
	public double mass;
	
	private Vec2f forces = new Vec2f();
	private double torque;
	
	public Vec2f velocity = new Vec2f();
	public double angularVel = 0;
	private double momentOfInertia = 0;
	
	private static final double maxVel = 20000, maxAngularVel = 4*Math.PI;
	
	private Vec2f oldPos;
	private double oldRot;
	
	public RigidBody(Shape shape, Vec2f position, double rotation, double mass) {
		super(shape, position, rotation);
		this.mass = mass;
		if(this.shape != null)
			momentOfInertia = this.shape.getMomentOfInertia(this.mass);
	}
	
	/**
	 * Apply forces to velocities and reset forces
	 * Save current pos/rot and apply velocities
	 * @param d
	 * @param globalForce
	 */
	public void preprocess(double d, Vec2f globalForce) {
		//Apply global forces like gravity
		applyForce(Vec2f.multiply(globalForce, 32));
//		applyForce(globalForce);
		
		//Linear movement
		oldPos = position.clone();
		//Vec2f accel = new Vec2f( forces.x/mass, forces.y/mass );
		Vec2f accel = new Vec2f( forces.x, forces.y );
		velocity.x = (float)Utils.clampD( (accel.x*d)+velocity.x, -maxVel, maxVel );
		velocity.y = (float)Utils.clampD( (accel.y*d)+velocity.y, -maxVel, maxVel );
		position.add( Vec2f.multiply(velocity, d) );
		forces.x = 0;
		forces.y = 0;
		
		//Angular movement
		oldRot = rotation;
		double angularAccel = torque / momentOfInertia;
		angularVel = Utils.clampD( (angularAccel*d)+angularVel, -maxAngularVel, maxAngularVel );
		rotation += angularVel*d;
		torque = 0;
		
	}
	
	/**
	 * Apply velocities to position and rotation based on values saved by preprocess()
	 * @param d
	 * @param globalForce
	 */
	public void process(double d) {
		position.set( Vec2f.add(oldPos, Vec2f.multiply(velocity, d)) );
//		position.add( Vec2f.multiply(velocity, d) );
		rotation = oldRot + angularVel*d;
//		rotation += angularVel*d;
	}
	
//	public void process(double d, Vec2f globalForce) {
//		//Apply global forces like gravity
//		applyForce(Vec2f.multiply(globalForce, 16));
////		applyForce(globalForce);
//		
//		//Linear movement
//		//Vec2f accel = new Vec2f( forces.x/mass, forces.y/mass );
//		Vec2f accel = new Vec2f( forces.x, forces.y );
//		velocity.x = (float)Utils.clampD( (accel.x*d)+velocity.x, -maxVel, maxVel );
//		velocity.y = (float)Utils.clampD( (accel.y*d)+velocity.y, -maxVel, maxVel );
//		position.add( Vec2f.multiply(velocity, d) );
//		forces.x = 0;
//		forces.y = 0;
//		
//		//Angular movement
//		double angularAccel = torque / momentOfInertia;
//		angularVel = Utils.clampD( (angularAccel*d)+angularVel, -maxAngularVel, maxAngularVel );
//		rotation += angularVel*d;
//		torque = 0;
//	}
	
	/**
	 * Apply a force at center of mass
	 * @param force
	 */
	public void applyForce(Vec2f force) {
		forces.add(force);
	}
	
	/**
	 * Apply a force on a point
	 * @param position
	 * @param force
	 */
	public void applyForce(Vec2f position, Vec2f force) {
		forces.add(force);
		
		//Local force position
		Vec2f offset = Vec2f.subtract(position, this.position);
		
		torque += offset.x*force.x - offset.y-force.y;
	}
	
	/**
	 * Apply an impulse at center of mass
	 * @param impulse
	 */
	public void applyImpulse(Vec2f impulse) {
		velocity.add(impulse);
	}
	
	/**
	 * Apply an impulse on a point
	 * @param position
	 * @param impulse
	 */
	public void applyImpulse(Vec2f position, Vec2f impulse) {
		velocity.add(impulse);
		
		//Local force position
		Vec2f offset = Vec2f.subtract(position, this.position);
		
		angularVel += offset.x*impulse.x - offset.y-impulse.y;
	}

}
