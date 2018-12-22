package arenashooter.entities;

import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec3f;

public class Spatial3 extends Entity {
	public Vec3f position;
	public Quat rotation = Quat.fromAngle(0);
	
	public Spatial3(Vec3f position) {
		this.position = position.clone();
	}
	
	/**
	 * Update children, transmit position to every Spatial3 child.
	 */
	@Override
	public void step(double d) {
		for (Entity e : children.values()) {
			if( e instanceof Spatial3 )
				((Spatial3)e).position.set(position);
			e.step(d);
		}
	}
}
