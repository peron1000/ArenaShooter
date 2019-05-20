package arenashooter.entities.spatials;

import arenashooter.engine.animation.Animation;
import arenashooter.engine.animation.AnimationData;
import arenashooter.engine.animation.IAnimated;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.entities.Entity;

public class AnimationTester extends Spatial implements IAnimated {

	private Animation anim;
	
	public AnimationTester(Vec2f position) {
		super(position);

		Mesh mesh = new Mesh(new Vec3f(), new Quat(), "data/meshes/test_ball.obj");
		mesh.attachToParent(this, "mesh");
		
		setAnim(new Animation( AnimationData.loadAnim("data/animations/animTest.xml") ));
		playAnim();
	}
	
	@Override
	public void step(double d) {
		anim.step(d);
		
		Entity mesh = getChildren().get("mesh");
		if( mesh instanceof Mesh ) {
			((Mesh)mesh).localPosition.set(anim.getTrackVec3f("pos"));
			Quat.fromAngle(anim.getTrackD("rot"), ((Mesh)mesh).rotation);
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
