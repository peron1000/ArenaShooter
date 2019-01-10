package arenashooter.engine.physic.bodies;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.Body;
import arenashooter.engine.physic.Physic;
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
	
	private static final double maxVel = 20000, maxAngularVel = 8*Math.PI;
	
	public RigidBody(Shape shape, Vec2f position, double rotation, double mass) {
		super(shape, position, rotation);
		this.mass = mass;
		if(this.shape != null)
			momentOfInertia = this.shape.getMomentOfInertia(this.mass);
	}
	
	public void process(double d) {
		//Apply global forces like gravity
		applyForce(Physic.globalForce);
		
		//Linear movement
		Vec2f accel = new Vec2f( forces.x/mass, forces.y/mass );
		velocity.x = (float)Utils.clampD( (accel.x*d)+velocity.x, -maxVel, maxVel );
		velocity.y = (float)Utils.clampD( (accel.y*d)+velocity.y, -maxVel, maxVel );
		position.add( Vec2f.multiply(velocity, d) );
		
		//Angular movement
		double angularAccel = torque / momentOfInertia;
		angularVel = Utils.clampD( (angularAccel*d)+angularVel, -maxAngularVel, maxAngularVel );
		rotation += angularVel*d;
	}
	
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
	 * Apply an impulse on a point
	 * @param position
	 * @param force
	 */
	public void applyImpluse(Vec2f position, Vec2f impulse) {
		velocity.add(impulse);
		
		//Local force position
		Vec2f offset = Vec2f.subtract(position, this.position);
		
		angularVel += offset.x*impulse.x - offset.y-impulse.y;
	}

}
