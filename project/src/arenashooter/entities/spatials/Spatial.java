package arenashooter.entities.spatials;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.DamageInfo;
import arenashooter.engine.json.StrongJsonKey;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec2fi;
import arenashooter.entities.Arena;
import arenashooter.entities.Entity;

public class Spatial extends Entity {
	/** Attach position to parent */
	public boolean attachPos = true;
	/** Attach rotation to parent */
	public boolean attachRot = true;
	/** World space Parent position */
	protected Vec2f parentPosition = new Vec2f();
	/** Local space position */
	public Vec2f localPosition;
	/** World space position */
	private Vec2f worldPosition = new Vec2f();
	/** World space Parent rotation */
	protected double parentRotation = 0;
	/** World space rotation */
	public double localRotation = 0;

	public boolean ignoreKillBounds = false;

	public Spatial() {
		localPosition = new Vec2f();
	}

	public Spatial(Vec2fi localPosition) {
		this.localPosition = new Vec2f(localPosition);
	}

	@Override
	protected void recursiveAttach(Entity newParent) {
		super.recursiveAttach(newParent);
		updateAttachment();
	}

	@Override
	protected void recursiveDetach(Arena oldArena) {
		super.recursiveDetach(oldArena);
		updateAttachment();
	}

	/**
	 * Get this entity's world position
	 * 
	 * @return parent position + local position
	 */
	public Vec2fi getWorldPos() {
		return Vec2f.add(parentPosition, Vec2f.rotate(localPosition, parentRotation), worldPosition);
	}

	public double getWorldRot() {
		return parentRotation + localRotation;
	}

	/**
	 * Apply damage on this entity
	 * 
	 * @param info
	 * @return actual damages taken
	 */
	public float takeDamage(DamageInfo info) {
		return 0;
	}

	@Override
	public void editorAddPosition(Vec2fi position) {
		localPosition.add(position);

		for (Entity e : getChildren().values())
			e.updateAttachment();
	}

	@Override
	public void editorAddRotationZ(double angle) {
		localRotation += angle;

		for (Entity e : getChildren().values())
			e.updateAttachment();
	}

	@Override
	public void updateAttachment() {
		if (getParent() instanceof Spatial) {
			if (attachRot)
				parentRotation = ((Spatial) getParent()).getWorldRot();
			else
				parentRotation = 0;

			if (attachPos)
				parentPosition.set(((Spatial) getParent()).getWorldPos());
			else
				parentPosition.set(0, 0);
		} else if (getParent() instanceof Spatial3) {
			// TODO: Rotation from spatial3
//			if(attachRot)
//				parentRotation = ((Spatial3)getParent()).getWorldRot()
//			else
			parentRotation = 0;

			if (attachPos)
				parentPosition.set(((Spatial3) getParent()).getWorldPos().x, ((Spatial3) getParent()).getWorldPos().y);
			else
				parentPosition.set(0, 0);
		} else {
			parentRotation = 0;
			parentPosition.set(0, 0);
		}
	}

	/**
	 * Update children, transmit position to every Spatial child.
	 */
	@Override
	public void step(double d) {
		if (!getChildren().isEmpty()) {
			List<Entity> toUpdate = new LinkedList<>();
			toUpdate.addAll(getChildren().values());
			for (Entity e : toUpdate) {
				e.updateAttachment();
				e.step(d);
			}
		}
	}
	
	
	/*
	 * JSON
	 */

	@Override
	public Set<StrongJsonKey> getJsonKey() {
		Set<StrongJsonKey> set = super.getJsonKey();
		set.add(new StrongJsonKey() {

			@Override
			public Object getValue() {
				return localPosition;
			}

			@Override
			public String getKey() {
				return "position";
			}

			@Override
			public void useKey(JsonObject json) throws Exception {
				localPosition = Vec2f.jsonImport(json.getCollection(this));
			}
		});
		set.add(new StrongJsonKey() {

			@Override
			public Object getValue() {
				return localRotation;
			}

			@Override
			public String getKey() {
				return "rotation";
			}

			@Override
			public void useKey(JsonObject json) throws Exception {
				localRotation = json.getDouble(this);
			}
		});
		return set;
	}
}
