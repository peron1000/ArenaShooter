package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.StaticBody;
import arenashooter.engine.physic.shapes.PhysicShape;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.engine.physic.shapes.ShapeDisk;
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
	public double getWorldRot() {
		return body.getRotation();
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
		body.setPosition(Vec2f.add(getWorldPos(), position));
		
		for (Entity e : getChildren().values()) {
			if (e instanceof Spatial) {
				((Spatial) e).parentPosition.set(getWorldPos());
				((Spatial) e).parentRotation = getWorldRot();
			} else if (e instanceof Spatial3) {
				((Spatial3) e).parentPosition.x = getWorldPos().x;
				((Spatial3) e).parentPosition.y = getWorldPos().y;
			}
		}
	}

	@Override
	public void addScale(Vec2f extent) {
		PhysicShape oldShape = body.getShape();
		if(oldShape instanceof ShapeBox)
			body.setShape(new ShapeBox( Vec2f.add(((ShapeBox)oldShape).getExtent(), extent)  ));
		if(oldShape instanceof ShapeDisk)
			body.setShape(new ShapeDisk( ((ShapeDisk)oldShape).getRadius() + extent.x  ));
	}

	@Override
	public void addRotation(double angle) {
		body.setRotation((float) (getWorldRot()+angle));
	}

	@Override
	public void drawEditor() {
		body.debugDraw();
	}

}
