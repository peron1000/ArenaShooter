package arenashooter.entities.spatials;

import java.util.LinkedList;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Entity;

public class Spatial extends Entity {
	/** World space Parent position */
	public Vec2f parentPosition;
	/** Local space position */
	public Vec2f localPosition;
	/** World space position */
	private Vec2f worldPosition = new Vec2f();
	
	/** World space rotation */
	public double rotation = 0;
	
	/** If true and attached to a Spatial, rotation will be set to parent's rotation on step */
	public boolean rotationFromParent = false;

	public Spatial() {
		parentPosition = new Vec2f();
		localPosition = new Vec2f();
	}

	public Spatial(Vec2f position) {
		this.parentPosition = position.clone();
		localPosition = new Vec2f();
	}
	
	@Override
	public Entity attachToParent(Entity newParent, String name) {
		Entity prev = super.attachToParent(newParent, name);
		
		if (newParent instanceof Spatial)
			parentPosition.set(((Spatial) newParent).getWorldPos());
		else if (newParent instanceof Spatial3) {
			parentPosition.x = ((Spatial3) newParent).pos().x;
			parentPosition.y = ((Spatial3) newParent).pos().y;
		}
		
		return prev;
	}

	/**
	 * Get this entity's world position
	 * @return parent position + local position
	 */
	public Vec2f getWorldPos() {
		if(rotationFromParent)
			return Vec2f.add(parentPosition, Vec2f.rotate(localPosition, rotation), worldPosition);
		else
			return Vec2f.add(parentPosition, localPosition, worldPosition);
	}

	/**
	 * Update children, transmit position to every Spatial child.
	 */
	@Override
	public void step(double d) {
		if(rotationFromParent && parent instanceof Spatial)
			rotation = ((Spatial)parent).rotation;
		
		if(!getChildren().isEmpty()) {
			LinkedList<Entity> toUpdate = new LinkedList<>();
			toUpdate.addAll(getChildren().values());
			for (Entity e : toUpdate) {
				if (e instanceof Spatial)
					((Spatial) e).parentPosition.set(getWorldPos());
				else if (e instanceof Spatial3) {
					((Spatial3) e).parentPosition.x = getWorldPos().x;
					((Spatial3) e).parentPosition.y = getWorldPos().y;
				}
				e.step(d);
			}
		}
	}
}
