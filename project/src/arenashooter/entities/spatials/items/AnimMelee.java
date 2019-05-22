package arenashooter.entities.spatials.items;

import arenashooter.engine.animation.Animation;
import arenashooter.engine.animation.AnimationData;
import arenashooter.engine.animation.IAnimated;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.Spatial;
import arenashooter.entities.spatials.Sprite;

public class AnimMelee extends Spatial implements IAnimated {

	private Animation anim;
	protected Item item = null;
	protected Sprite sprite = null;

	public AnimMelee(Vec2f position, Item item) {
		super(position);
		setAnim(new Animation(AnimationData.loadAnim("data/animations/animTest2.xml")));
		this.item = item;
		this.sprite = item.getSprite();
		sprite.rotationFromParent = false;
		rotationFromParent = false;
	}

	@Override
	public void step(double d) {
		super.step(d);

		anim.step(d);

		Character character = item.getCharacter();
		if (character != null) {
			rotation = Utils.lerpAngle(rotation, character.aimInput, Math.min(1, d * 17));

			//if (character.lookRight) {
			sprite.localPosition = new Vec2f(-0.20, 0);
			item.localPosition.set(anim.getTrackVec2f("rightPos"));
			item.rotation = anim.getTrackD("rightRot") + rotation;
			sprite.localPosition = new Vec2f(1.0, 0);

			//			} else if (!character.lookRight) {
			//				sprite.localPosition = new Vec2f(0.20, 0);
			//				item.position.set(anim.getTrackVec2f("leftPos"));
			//				item.rotation = anim.getTrackD("leftRot") + rotation;
			//				sprite.localPosition = new Vec2f(1.0, 0);
			//			}
		}

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
		anim.stopPlaying();
	}

	public boolean isPlaying() {
		return anim.isPlaying();
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
