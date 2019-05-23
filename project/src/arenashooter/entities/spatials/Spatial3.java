package arenashooter.entities.spatials;

import java.util.LinkedList;

import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec3f;
import arenashooter.entities.Entity;

public class Spatial3 extends Entity {
	/** World space parent position */
	public Vec3f parentPosition = new Vec3f();
	/** Local space position */
	public Vec3f localPosition;
	/** World space position */
	private Vec3f worldPosition = new Vec3f();
	
	/** World space parent rotation */
	public Quat parentRotation = Quat.fromAngle(0);
	/** Local space rotation */
	public Quat localRotation = Quat.fromAngle(0);
	/** World space rotation */
	private Quat worldRotation = Quat.fromAngle(0);

	/** If true and attached to a Spatial, rotation will be set to parent's rotation on step */
	public boolean rotationFromParent = true;
	
	public Spatial3(Vec3f position) {
		super();
		localPosition = position.clone();
	}
	
	@Override
	public int getZIndex() {
		//TODO: adding position.z should help with transparency sorting, needs testing
		return ((int)getWorldPos().z)+zIndex;
	}
	
	@Override
	public Entity attachToParent(Entity newParent, String name) {
		Entity prev = super.attachToParent(newParent, name);
		
		if (newParent instanceof Spatial) {
			parentPosition.x = ((Spatial) newParent).getWorldPos().x;
			parentPosition.y = ((Spatial) newParent).getWorldPos().y;
		} else if (newParent instanceof Spatial3)
			parentPosition.set(((Spatial3) newParent).getWorldPos());
		
		return prev;
	}
	
	/**
	 * Get this entity's world position
	 * @return parent position + local position
	 */
	public Vec3f getWorldPos() {
		if(rotationFromParent) {
			parentRotation.rotate(localPosition, worldPosition);
			return Vec3f.add(parentPosition, worldPosition, worldPosition);
		} else
			return Vec3f.add(parentPosition, localPosition, worldPosition);
	}
	
	public Quat getWorldRot() {
		return Quat.multiply(parentRotation, localRotation, worldRotation);
	}
	
	/**
	 * Update children, transmit position to every Spatial3 child.
	 */
	@Override
	public void step(double d) {
		if(rotationFromParent && getParent() instanceof Spatial3)
			localRotation = ((Spatial3)getParent()).localRotation;
		else if(rotationFromParent && getParent() instanceof Spatial)
				Quat.fromAngle(((Spatial)getParent()).rotation, localRotation);
	
		if(!getChildren().isEmpty()) {
			LinkedList<Entity> toUpdate = new LinkedList<>();
			toUpdate.addAll(getChildren().values());
			for (Entity e : toUpdate) {
				if (e instanceof Spatial3) {
					((Spatial3) e).parentPosition.set(getWorldPos());
					((Spatial3) e).parentRotation.set(getWorldRot());
				} else if (e instanceof Spatial) {
					((Spatial) e).parentPosition.x = getWorldPos().x;
					((Spatial) e).parentPosition.y = getWorldPos().y;
					//TODO: Transmit rotation
				}
				e.step(d);
			}
		}
	}
}
