package arenashooter.engine.physic.bodies;

import java.awt.RadialGradientPaint;

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
	
	public RigidBody(Shape shape, Vec2f position, double rotation, double mass) {
		super(shape, position, rotation);
		this.mass = mass;
		if(this.shape != null)
			momentOfInertia = this.shape.getMomentOfInertia(this.mass);
	}
	
	public void process(double d) {
		updateForces();
		
		Vec2f accel = new Vec2f( forces.x/mass, forces.y/mass );
		velocity.add( Vec2f.multiply(accel, d) );
		position.add( Vec2f.multiply(velocity, d) );
		
		angularVel += torque;
		rotation += angularVel*d;
	}
	
	public void applyForce(Vec2f force) {
		
	}
	
	public void applyForce(Vec2f position, Vec2f force) {
		//Local force position
		Vec2f offset = Vec2f.subtract(position, this.position);
		
		torque += offset.x*force.x - offset.y-force.y;
	}
	
	protected void updateForces() {
		forces.y = (float)(mass * -9.807);
	}

}
