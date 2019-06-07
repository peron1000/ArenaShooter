package arenashooter.entities.spatials.items;

import java.util.Queue;

import arenashooter.engine.animation.Animation;
import arenashooter.engine.animation.AnimationData;
import arenashooter.engine.animation.IAnimated;
import arenashooter.engine.animation.animevents.AnimEvent;
import arenashooter.engine.animation.animevents.AnimEventCustom;
import arenashooter.engine.animation.animevents.AnimEventSound;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.Spatial;
import arenashooter.entities.spatials.Sprite;

public class AnimMelee extends Spatial implements IAnimated {

	private Animation anim;
	protected Melee item = null;
	protected Sprite sprite = null;

	public AnimMelee(Vec2f position, Melee item) {
		super(position);
		setAnim(new Animation(AnimationData.loadAnim("data/animations/anim_sword_01.xml")));
		this.item = item;
		this.sprite = item.getSprite();
		sprite.attachRot = false;
		attachRot = false;
	}

	@Override
	public void step(double d) {
		super.step(d);

		anim.step(d);
		
		Queue<AnimEvent> events = anim.getEvents();
		AnimEvent current = events.peek();
		while( (current = events.poll()) != null ) {
			if(current instanceof AnimEventCustom) {
				if(((AnimEventCustom)current).data.equals("startDamage"))
					item.startDamage();
				else if(((AnimEventCustom)current).data.equals("startDamage"))
					item.stopDamage();
			} else if(current instanceof AnimEventSound) {
				((AnimEventSound) current).play(item.getWorldPos());
			}
		}

		Character character = item.getCharacter();
		if (character != null) {
			localRotation = Utils.lerpAngle(localRotation, character.aimInput, Math.min(1, d * 17));
			item.localPosition.set(anim.getTrackVec2f("rightPos"));
			item.localRotation = anim.getTrackD("rightRot") + getWorldRot();
			sprite.localPosition = new Vec2f(1.0, 0);
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
		anim.setTime(0);
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
