package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.Impact;
import arenashooter.entities.Collider;
import arenashooter.entities.Entity;

public class RigidBody extends Spatial {

	Vec2f vel = new Vec2f();
	float angularVel = 0; //Radians per second
	public double mass;
	public double damping = .99;
	public double angularDamping = .99;

	public RigidBody(Vec2f position, Vec2f extent, double mass) {
		super(position);
		this.mass = mass;
		Collider c = new Collider(this.position, extent);
		c.attachToParent(this, "collider");
	}
	
	public RigidBody(Vec2f position) {
		this(position, new Vec2f(300, 300), 10);
	}
	
	@Override
	public void step(double d) {
		physics(d);
		
		super.step(d);
	}
	
	void physics(double d) { //TODO
//		vel.multiply((float)(damping*d));
//		angularVel *= angularDamping*d;
		
		vel.y += 9.807 * 10;
		
		Collider collider = (Collider)children.get("collider");
		for (Entity plat : getParent().children.values()) {
			if (plat instanceof Plateform) {
				for (Entity coll : ((Plateform) plat).children.values()) {
					if (coll instanceof Collider) {
						Collider c = (Collider) coll;
						Impact impact = new Impact(collider, c, Vec2f.multiply(vel, (float) d));
						vel.x = vel.x * impact.getVelMod().x;
						vel.y = vel.y * impact.getVelMod().y;
					}
				}
			}
		}
		
		rotation += (angularVel*d);
		
		position.add(Vec2f.multiply(vel, (float)d));
	}
	
}
