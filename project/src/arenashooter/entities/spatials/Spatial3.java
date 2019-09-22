package arenashooter.entities.spatials;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import arenashooter.engine.json.StrongJsonKey;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2fi;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec3fi;
import arenashooter.entities.Arena;
import arenashooter.entities.Entity;

public class Spatial3 extends Entity {
	/** Attach position to parent */
	public boolean attachPos = true;
	/** Attach rotation to parent */
	public boolean attachRot = true;
	/** World space parent position */
	private Vec3f parentPosition = new Vec3f();
	/** Local space position */
	public Vec3f localPosition;
	/** World space position */
	private Vec3f worldPosition = new Vec3f();
	/** World space parent rotation */
	private Quat parentRotation = new Quat();
	/** Local space rotation */
	public Quat localRotation = new Quat();
	/** World space rotation */
	private Quat worldRotation = new Quat();

	/** Yaw, Pitch, Roll used in editor */
	private Vec3f editorLocalRotEuler = new Vec3f();

	public Spatial3() {
		super();
		this.localPosition = new Vec3f();
		this.localRotation.toEuler(editorLocalRotEuler);
	}
	
	public Spatial3(Vec3fi localPosition) {
		super();
		this.localPosition = new Vec3f(localPosition);
		this.localRotation.toEuler(editorLocalRotEuler);
	}

	public Spatial3(Vec3fi localPosition, Quat localRotation) {
		super();
		this.localPosition = new Vec3f(localPosition);
		this.localRotation.set(localRotation);
		this.localRotation.toEuler(editorLocalRotEuler);
	}

	@Override
	public int getZIndex() {
		// TODO: adding position.z should help with transparency sorting, needs testing
		return ((int) getWorldPos().z) + zIndex;
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
	public Vec3f getWorldPos() {
		parentRotation.rotate(localPosition, worldPosition);
		return Vec3f.add(parentPosition, worldPosition, worldPosition);
	}

	public Quat getWorldRot() {
		return Quat.multiply(parentRotation, localRotation, worldRotation);
	}

	@Override
	public void editorAddPosition(Vec2fi position) {
		localPosition.x += position.x();
		localPosition.y += position.y();
		
		for(Entity e : getChildren().values())
			e.updateAttachment();
	}

	@Override
	public void editorAddRotationX(double angle) { // Yaw
		editorLocalRotEuler.x += angle;
		Quat.fromEuler(editorLocalRotEuler, localRotation);

		for (Entity e : getChildren().values())
			e.updateAttachment();
	}

	@Override
	public void editorAddRotationY(double angle) { // Pitch
		editorLocalRotEuler.y += angle;
		Quat.fromEuler(editorLocalRotEuler, localRotation);

		for (Entity e : getChildren().values())
			e.updateAttachment();
	}

	@Override
	public void editorAddRotationZ(double angle) { // Roll
		editorLocalRotEuler.z += angle;
		Quat.fromEuler(editorLocalRotEuler, localRotation);

		for (Entity e : getChildren().values())
			e.updateAttachment();
	}

	@Override
	public void editorAddDepth(float depth) {
		localPosition.z += depth;
	}

	@Override
	public void updateAttachment() {
		if (getParent() instanceof Spatial) {
			if (attachRot)
				Quat.fromAngle(((Spatial) getParent()).getWorldRot(), parentRotation);
			else
				Quat.fromAngle(0, parentRotation);
			
			if(attachPos)
				parentPosition.set(((Spatial)getParent()).getWorldPos().x(), ((Spatial)getParent()).getWorldPos().y(), 0);
			else
				parentPosition.set(0, 0, 0);
		} else if (getParent() instanceof Spatial3) {
			if (attachRot)
				parentRotation.set(((Spatial3) getParent()).getWorldRot());
			else
				Quat.fromAngle(0, parentRotation);

			if (attachPos)
				parentPosition.set(((Spatial3) getParent()).getWorldPos());
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
		if (!getChildren().isEmpty()) {
			List<Entity> toUpdate = new LinkedList<>();
			toUpdate.addAll(getChildren().values());
			for (Entity e : toUpdate) {
				e.updateAttachment();
				e.step(d);
			}
		}
	}

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
				JsonArray a = json.getCollection(this);
				if (a != null)
					localPosition = Vec3f.jsonImport(a);
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
				JsonArray a = json.getCollection(this);
				if (a != null)
					localRotation = Quat.jsonImport(a);
			}
		});
		return set;
	}

	public static Spatial3 fromJson(JsonObject json) throws Exception {
		Spatial3 s = new Spatial3();
		useKeys(s, json);
		return s;
	}

}
