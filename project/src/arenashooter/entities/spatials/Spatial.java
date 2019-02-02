package arenashooter.entities.spatials;

import java.util.LinkedList;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Entity;

public class Spatial extends Entity {
	/** World space Parent position */
	public Vec2f position;
	/** Local space position */
	public Vec2f localPosition;
	
	/** World space rotation */
	public double rotation = 0;

	public Spatial() {
		position = new Vec2f();
		localPosition = new Vec2f();
	}

	public Spatial(Vec2f position) {
		this.position = position.clone();
		localPosition = new Vec2f();
	}

	
	/**
	 * Get this entity's world position
	 * @return position+localPosition
>>>>>>> branch 'master' of https://github.com/peron1000/ArenaShooter.git
	 */
	public Vec2f pos() {
		return Vec2f.add(position, localPosition);
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
