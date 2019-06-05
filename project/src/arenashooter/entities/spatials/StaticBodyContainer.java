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
	private boolean init = false;

	public StaticBodyContainer(StaticBody body) {
		super();
		this.body = body;
	}

	/**
	 * Create a static body with a box shape
	 * @param worldPosition
	 * @param extent box extent
	 * @param worldRotation
	 */
	public StaticBodyContainer(Vec2f worldPosition, Vec2f extent, double worldRotation) {
		this(new StaticBody(new ShapeBox(extent), worldPosition, worldRotation, CollisionFlags.LANDSCAPE));
	}
	
	/**
	 * Create a static body with a disk shape
	 * @param worldPosition
	 * @param radius disk radius
	 * @param worldRotation
	 */
	public StaticBodyContainer(Vec2f worldPosition, double radius, double worldRotation) {
		this(new StaticBody(new ShapeDisk(radius), worldPosition, worldRotation, CollisionFlags.LANDSCAPE));
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
			localRotation = body.getRotation() - parentRotation;
		}

		super.step(d);
	}

	@Override
	public void draw() {
		if (Main.drawCollisions) {
			body.debugDraw();
		}

		super.draw();
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
	public void editorAddRotation(double angle) {
		body.setRotation((float) (getWorldRot()+angle));
		
		for (Entity e : getChildren().values())
			e.updateAttachment();
	}

	@Override
	public void editorDraw() {
		body.debugDraw();
	}

}
