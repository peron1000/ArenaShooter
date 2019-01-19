package arenashooter.entities.spatials;

import java.util.LinkedList;

import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec3f;
import arenashooter.entities.Entity;

public class Spatial3 extends Entity {
	/** World space position */
	public Vec3f position;
	/** World space rotation */
	public Quat rotation = Quat.fromAngle(0);
	
	public Spatial3(Vec3f position) {
		this.position = position.clone();
	}
	
	/**
	 * Update children, transmit position to every Spatial3 child.
	 */
	@Override
	public void step(double d) {
		if(!children.isEmpty()) {
			LinkedList<Entity> toUpdate = new LinkedList<>();
			toUpdate.addAll(children.values());
			for (Entity e : toUpdate) {
				if (e instanceof Spatial3)
					((Spatial3) e).position.set(position);
				e.step(d);
			}
		}
	}
}
