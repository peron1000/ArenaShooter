package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Entity;

public class Spatial extends Entity {
	/** World space position */
	public Vec2f position;
	/** World space rotation */
	public double rotation = 0;

	public Spatial() {
		position = new Vec2f();
	}

	public Spatial(Vec2f position) {
		this.position = position.clone();
	}

	/**
	 * Update children, transmit position to every Spatial child.
	 */
	@Override
	public void step(double d) {
		for (Entity e : children.values()) {
			if (e instanceof Spatial)
				((Spatial) e).position.set(position);
			e.step(d);
		}
	}
}
