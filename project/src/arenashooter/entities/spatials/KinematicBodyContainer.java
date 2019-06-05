package arenashooter.entities.spatials;

import arenashooter.engine.DamageInfo;
import arenashooter.engine.DamageType;
import arenashooter.engine.animation.Animation;
import arenashooter.engine.animation.IAnimated;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.bodies.KinematicBody;
import arenashooter.engine.physic.shapes.PhysicShape;
import arenashooter.engine.physic.shapes.ShapeBox;
import arenashooter.engine.physic.shapes.ShapeDisk;
import arenashooter.entities.Entity;
import arenashooter.game.Main;

public class KinematicBodyContainer extends Spatial implements IAnimated {

	private KinematicBody body;
	
	private Animation currentAnim = null;
	
	private boolean needsPhysWorld = true;
	private boolean init = false;

	public KinematicBodyContainer(KinematicBody body) {
		super();
		this.body = body;
	}
	
	@Override
	public Entity attachToParent(Entity newParent, String name) {
		if(body != null) body.removeFromWorld();
		needsPhysWorld = true;
		
		Entity prev = super.attachToParent(newParent, name);
		
		if(getArena() != null) {
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
		if(body != null) body.removeFromWorld();
		needsPhysWorld = true;
		
		super.detach();
	}
	
	/**
	 * Detach if out of bounds
	 */
	@Override
	public float takeDamage(DamageInfo info) { //TODO: Get impact location
		//Destroy when out of bounds
		if(info.dmgType == DamageType.OUT_OF_BOUNDS) {
			if(ignoreKillBounds)
				return 0;
			else
				detach();
		}
		
		return 0;
	}
	
	
	@Override
	public Vec2f getWorldPos() {
		return body.getPosition();
	}
	
	@Override
	public double getWorldRot() {
		return body.getRotation();
	}
	
	/**
	 * @return linear velocity at center of mass
	 */
	public Vec2f getLinearVelocity() { return body.getLinearVelocity(); }
	
	/**
	 * Set linear velocity at center of mass
	 * @param newVelocity
	 */
	public void setLinearVelocity(Vec2f newVelocity) {
		body.setLinearVelocity(newVelocity);
	}
	
	public KinematicBody getBody() {
		return body;
	}
	
	@Override
	public void step(double d) {
		if(!init) {
			init = true;
			body.setUserData(this);
		}
		
		if(needsPhysWorld) {
			if(getArena() != null) {
				body.addToWorld(getArena().physic);
				needsPhysWorld = false;
			}
		} else {
			localPosition = Vec2f.subtract(body.getPosition(), parentPosition);
			localRotation = body.getRotation() - parentRotation;
		}
		
		if(currentAnim != null) {
			//TODO: Finish this
			currentAnim.step(d);
			Vec2f direction = Vec2f.fromAngle(Vec2f.direction(getWorldPos(), currentAnim.getTrackVec2f("pos")));
			double distance = Vec2f.distance(getWorldPos(), currentAnim.getTrackVec2f("pos"));
			body.setLinearVelocity(direction.multiply((float) (distance/d)));
		}
		
		//Destroy when out of bounds
		if (getArena() != null && (getWorldPos().x < getArena().killBound.x || getWorldPos().x > getArena().killBound.z
				|| getWorldPos().y < getArena().killBound.y || getWorldPos().y > getArena().killBound.w))
			takeDamage(new DamageInfo(0, DamageType.OUT_OF_BOUNDS, new Vec2f(), null));
		
		super.step(d);
	}
	
	@Override
	public void draw() {
		if(Main.drawCollisions) body.debugDraw();
		
		super.draw();
	}

	@Override
	public void setAnim(Animation anim) {
		currentAnim = anim;
	}

	@Override
	public void playAnim() {
		if(currentAnim != null) currentAnim.play();
	}

	@Override
	public void stopAnim() {
		if(currentAnim != null) currentAnim.stopPlaying();
	}

	@Override
	public void animJumpToEnd() {
		if(currentAnim != null) currentAnim.setTime(currentAnim.getLength());
	}

	@Override
	public Animation getAnim() {
		return currentAnim;
	}

	@Override
	public void setAnimSpeed(double speed) {
//		if(currentAnim != null) currentAnim.sp //TODO
	}

	@Override
	public double getAnimSpeed() {
		// TODO Auto-generated method stub
		return 0;
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
