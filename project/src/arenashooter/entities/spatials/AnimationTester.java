package arenashooter.entities.spatials;

import arenashooter.engine.animation.Animation;
import arenashooter.engine.animation.AnimationData;
import arenashooter.engine.animation.IAnimated;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Entity;

public class AnimationTester extends Spatial implements IAnimated {

	private Animation anim;
	
	public AnimationTester(Vec2f position) {
		super(position);

		Sprite sprite = new Sprite(position, "data/sprites/UnMoineHD.png");
		sprite.attachToParent(this, "moine");
		
		setAnim(new Animation( AnimationData.loadAnim("data/animations/animTest.xml") ));
		playAnim();
	}
	
	@Override
	public void step(double d) {
		anim.step(d);
		
		Entity sprite = getChildren().get("moine");
		if( sprite instanceof Sprite ) {
			((Sprite)sprite).localPosition.set(anim.getTrackVec2f("pos"));
			((Sprite)sprite).rotation = anim.getTrackD("rot");
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
