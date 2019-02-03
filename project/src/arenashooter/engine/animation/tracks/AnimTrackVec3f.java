package arenashooter.engine.animation.tracks;

import java.util.Map;

import arenashooter.engine.math.Vec3f;

/**
 * Vec3f animation track with linear interpolation
 */
public class AnimTrackVec3f extends AnimTrack<Vec3f> {

	public AnimTrackVec3f(Map<Float, Vec3f> keyframes) {
		super(keyframes);
	}

	@Override
	public Vec3f valueAt(float time) {
		int prev = prevKeyframe(time);
		int next = nextKeyframe(time);
		float f = times[next]-times[prev];
		if(f != 0)
			f = (time-times[prev])/f;
		return Vec3f.lerp((Vec3f)values[prev], (Vec3f)values[next], f);
	}

}
