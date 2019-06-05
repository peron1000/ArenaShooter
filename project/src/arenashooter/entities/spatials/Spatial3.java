package arenashooter.entities.spatials;

import java.util.LinkedList;

import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec3f;
import arenashooter.entities.Entity;

public class Spatial3 extends Entity {
	/** Attach position to parent */
	public boolean attachPos = true;
	/** Attach rotation to parent */
	public boolean attachRot = true;
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
	
	public Spatial3() {
		super();
		this.localPosition = new Vec3f();
	}
	
	public Spatial3(Vec3f localPosition) {
		super();
		this.localPosition = localPosition.clone();
	}

	public Spatial3(Vec3f localPosition, Quat localRotation) {
		super();
		this.localPosition = localPosition.clone();
		this.localRotation.set(localRotation);
	}
	
	@Override
	public int getZIndex() {
		//TODO: adding position.z should help with transparency sorting, needs testing
		return ((int)getWorldPos().z)+zIndex;
	}
	
	@Override
	public Entity attachToParent(Entity newParent, String name) {
		Entity prev = super.attachToParent(newParent, name);
		
		updateAttachment();
		
		return prev;
	}
	
	/**
	 * Get this entity's world position
	 * @return parent position + local position
	 */
	public Vec3f getWorldPos() {
		parentRotation.rotate(localPosition, worldPosition);
		return Vec3f.add(parentPosition, worldPosition, worldPosition);
	}
	
	public Quat getWorldRot() {
		return Quat.multiply(parentRotation, localRotation, worldRotation);
	}
	
	public void addLocalPositionSelfAndChildren(Vec3f add) {
		localPosition.add(add);
		for (Entity child : getChildren().values()) {
			if(child instanceof Spatial3) {
				Spatial3 s = (Spatial3) child;
				s.addLocalPositionSelfAndChildren(add);
			}
		}
	}
	

	@Override
	public void updateAttachment() {
		if(getParent() instanceof Spatial) {
			if(attachRot)
				Quat.fromAngle(((Spatial)getParent()).getWorldRot(), parentRotation);
			else
				Quat.fromAngle(0, parentRotation);
			
			if(attachPos)
				parentPosition.set(((Spatial)getParent()).getWorldPos().x, ((Spatial)getParent()).getWorldPos().y, 0);
			else
				parentPosition.set(0, 0, 0);
		} else if (getParent() instanceof Spatial3) {
			if(attachRot)
				parentRotation.set(((Spatial3)getParent()).getWorldRot());
			else
				Quat.fromAngle(0, parentRotation);
			
			if(attachPos)
				parentPosition.set( ((Spatial3)getParent()).getWorldPos() );
			else
				parentPosition.set(0, 0, 0);
		} else {
			Quat.fromAngle(0, parentRotation);
			parentPosition.set(0, 0, 0);
		}
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
				e.updateAttachment();
				e.step(d);
			}
		}
	}
}
