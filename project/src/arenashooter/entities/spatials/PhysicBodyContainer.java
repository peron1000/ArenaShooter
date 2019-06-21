package arenashooter.entities.spatials;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.bodies.PhysicBody;
import arenashooter.engine.physic.shapes.PhysicShape;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.engine.physic.shapes.ShapeDisk;
import arenashooter.entities.Entity;
import arenashooter.game.Main;

public abstract class PhysicBodyContainer<T extends PhysicBody> extends Spatial {
	protected T body;
	private boolean needsPhysWorld = true;
	private boolean init = false;

	public PhysicBodyContainer(T body) {
		super();
		this.body = body;
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

	/**
	 * Detach from current parent and destroy physic body
	 */
	@Override
	public void detach() {
		if (body != null)
			body.removeFromWorld();
		needsPhysWorld = true;

		super.detach();
	}

	@Override
	public Vec2f getWorldPos() {
		return body.getPosition();
	}
	
	@Override
	public double getWorldRot() {
		return body.getRotation();
	}

	public T getBody() {
		return body;
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
			localRotation = body.getRotation() - parentRotation;
		}

		super.step(d);
	}

	@Override
	public void draw(boolean transparency) {
		if (Main.drawCollisions)
			body.debugDraw();
	}

	@Override
	public void editorAddPosition(Vec2f position) {
		body.setPosition(Vec2f.add(getWorldPos(), position));
		
		for (Entity e : getChildren().values())
			e.updateAttachment();
	}

	@Override
	public void editorAddScale(Vec2f extent) {
		PhysicShape oldShape = body.getShape();
		if(oldShape instanceof ShapeBox)
			body.setShape(new ShapeBox( Vec2f.add(((ShapeBox)oldShape).getExtent(), extent)  ));
		if(oldShape instanceof ShapeDisk)
			body.setShape(new ShapeDisk( ((ShapeDisk)oldShape).getRadius() + extent.x  ));
	}

	@Override
	public void editorAddRotationZ(double angle) {
		body.setRotation((float) (getWorldRot()+angle));
		
		for (Entity e : getChildren().values())
			e.updateAttachment();
	}

	@Override
	public void editorDraw() {
		body.debugDraw();
	}
}
