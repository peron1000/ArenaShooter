package arenashooter.entities.spatials.items;

import arenashooter.engine.animation.Animation;
import arenashooter.engine.animation.AnimationData;
import arenashooter.engine.animation.IAnimated;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.Spatial;

public class animMelee extends Spatial implements IAnimated {

	private Animation anim;
	protected Item item = null;
	public animMelee(Vec2f position, Item item) {
		super(position);
		playAnim();
		setAnim(new Animation( AnimationData.loadAnim("data/animations/animTest2.xml") ));
		this.item = item;
	}
	
	@Override
	public void step(double d) {
		anim.step(d);
		Character character = item.getCharacter();
		Melee melee = (Melee) getParent();
		if (character != null) {
//			if (!character.lookRight) {
				item.position.set(anim.getTrackVec2f("rightPos"));
				item.rotation = anim.getTrackD("rightRot");
				
//			} else if (character.lookRight) {
//				item.position.set(anim.getTrackVec2f("leftPos"));
//				item.rotation = anim.getTrackD("leftRot");
//			}
//		}
		}
		super.step(d);
	}

	@Override
	public void setAnim(Animation anim) {
		this.anim = anim;
	}

	@Override
	public void playAnim() {
		anim.play();
	}

	@Override
	public void stopAnim() {
		// TODO Auto-generated method stub

	}

	@Override
	public void animJumpToEnd() {
		// TODO Auto-generated method stub

	}

	@Override
	public Animation getAnim() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAnimSpeed(double speed) {
		// TODO Auto-generated method stub

	}

	@Override
	public double getAnimSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

}
