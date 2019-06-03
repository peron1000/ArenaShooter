package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.StaticBody;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.entities.Editable;
import arenashooter.entities.Entity;
import arenashooter.game.Main;

public class StaticBodyContainer extends Spatial implements Editable {

	private StaticBody body;

	private boolean needsPhysWorld = true;
	private boolean init = false, isEditorTarget = false;

	public StaticBodyContainer(Vec2f position, StaticBody body) {
		super(position);
		this.body = body;
	}

	public StaticBodyContainer(Vec2f position, Vec2f extent, double rotation) {
		this(position, new StaticBody(new ShapeBox(extent), position, rotation, CollisionFlags.LANDSCAPE));
	}

	@Override
	public Entity attachToParent(Entity newParent, String name) {
		if (body != null)
			body.removeFromWorld();
		needsPhysWorld = true;

		Entity prev = super.attachToParent(newParent, name);

		if (getArena() != null) {
			body.addToWorld(getArena().physic);
			needsPhysWorld = false;
		}

		return prev;
	}

	@Override
	/**
	 * Detach from current parent and destroy physic body
	 */
	public void detach() {
		if (body != null)
			body.removeFromWorld();
		needsPhysWorld = true;

		super.detach();
	}

	public StaticBody getBody() {
		return body;
	}

	public boolean isEditorTarget() {
		return isEditorTarget;
	}

	public void setEditorTarget(boolean isEditorTarget) {
		this.isEditorTarget = isEditorTarget;
	}

	@Override
	public Vec2f getWorldPos() {
		return body.getPosition();
	}

	@Override
	public void step(double d) {
		if (!init) {
			init = true;
			body.setUserData(this);
		}

		if (needsPhysWorld) {
			if (getArena() != null) {
				body.addToWorld(getArena().physic);
				needsPhysWorld = false;
			}
		} else {
			localPosition = Vec2f.subtract(body.getPosition(), parentPosition);
			rotation = body.getRotation();
		}

		super.step(d);
	}

	@Override
	public void draw() {
		if (Main.drawCollisions || isEditorTarget) {
			body.debugDraw();
		}

		super.draw();
	}

	@Override
	public void addPosition(Vec2f position) {
		localPosition.x += position.x;
		localPosition.y += position.y;
		for (Entity child : getChildren().values()) {
			if (child instanceof Editable) {
				Editable editable = (Editable) child;
				editable.addPosition(position);
			}
		}
		
		

	}

	@Override
	public void addScale(Vec2f extent) {
	}

	@Override
	public void addRotation(double angle) {
	}

}
