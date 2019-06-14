package arenashooter.entities.spatials;

import arenashooter.engine.DamageInfo;
import arenashooter.engine.DamageType;
import arenashooter.engine.animation.Animation;
import arenashooter.engine.animation.IAnimated;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.bodies.KinematicBody;

public class KinematicBodyContainer extends PhysicBodyContainer<KinematicBody> implements IAnimated {

	private Animation currentAnim = null;

	public KinematicBodyContainer(KinematicBody body) {
		super(body);
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
	
	@Override
	public void step(double d) {
		super.step(d);
		
		//Animation
		if(currentAnim != null) {
			//TODO: Add rotation
			currentAnim.step(d);
			if(currentAnim.hasTrackVec2f("pos")) {
				Vec2f direction = Vec2f.fromAngle(Vec2f.direction(getWorldPos(), currentAnim.getTrackVec2f("pos")));
				double distance = Vec2f.distance(getWorldPos(), currentAnim.getTrackVec2f("pos"));
				body.setLinearVelocity(direction.multiply((float) (distance/d)));
			}
		}
		
		//Destroy when out of bounds
		if (getArena() != null && (getWorldPos().x < getArena().killBound.x || getWorldPos().x > getArena().killBound.z
				|| getWorldPos().y < getArena().killBound.y || getWorldPos().y > getArena().killBound.w))
			takeDamage(new DamageInfo(0, DamageType.OUT_OF_BOUNDS, new Vec2f(), 0, null));
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
}
