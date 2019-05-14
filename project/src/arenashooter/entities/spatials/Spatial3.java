package arenashooter.entities.spatials;

import java.util.LinkedList;

import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec3f;
import arenashooter.entities.Entity;

public class Spatial3 extends Entity {
	/** Parent position */
	public Vec3f parentPosition = new Vec3f();
	/** Local space position */
	public Vec3f localPosition;
	
	/** World space rotation */
	public Quat rotation = Quat.fromAngle(0);
	
	public Spatial3(Vec3f position) {
		super();
		localPosition = position.clone();
	}
	
	@Override
	public int getZIndex() {
		//TODO: adding position.z should help with transparency sorting, needs testing
		return ((int)pos().z)+zIndex;
	}
	
	/**
	 * Get this entity's world position
	 * @return parent position + local position
	 */
	public Vec3f pos() {
		return Vec3f.add(parentPosition, localPosition);
	}
	
	/**
	 * Update children, transmit position to every Spatial3 child.
	 */
	@Override
	public void step(double d) {
		if(!getChildren().isEmpty()) {
			LinkedList<Entity> toUpdate = new LinkedList<>();
			toUpdate.addAll(getChildren().values());
			for (Entity e : toUpdate) {
				if (e instanceof Spatial3)
					((Spatial3) e).parentPosition.set(pos());
				else if (e instanceof Spatial) {
					((Spatial) e).parentPosition.x = pos().x;
					((Spatial) e).parentPosition.y = pos().y;
				}
				e.step(d);
			}
		}
	}
}
