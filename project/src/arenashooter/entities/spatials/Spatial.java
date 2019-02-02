package arenashooter.entities.spatials;

import java.util.LinkedList;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Entity;

public class Spatial extends Entity {
	/** World space Parent position */
	public Vec2f position;
	/** Local space position */
	public Vec2f localOffSet;
	
	/** World space rotation */
	public double rotation = 0;

	public Spatial() {
		position = new Vec2f();
		localOffSet = new Vec2f();
	}

	public Spatial(Vec2f position) {
		this.position = position.clone();
		localOffSet = new Vec2f();
	}
	
	public Vec2f pos() {
		return Vec2f.add(position, localOffSet);
	}

	/**
	 * Update children, transmit position to every Spatial child.
	 */
	@Override
	public void step(double d) {
		if(!children.isEmpty()) {
			LinkedList<Entity> toUpdate = new LinkedList<>();
			toUpdate.addAll(children.values());
			for (Entity e : toUpdate) {
				if (e instanceof Spatial)
					((Spatial) e).position.set(pos());
				e.step(d);
			}
		}
	}
}
