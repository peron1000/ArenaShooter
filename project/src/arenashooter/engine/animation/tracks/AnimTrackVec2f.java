package arenashooter.engine.animation.tracks;

import java.util.Map;

import arenashooter.engine.math.Vec2f;

/**
 * Vec2f animation track with linear interpolation
 */
public class AnimTrackVec2f extends AnimTrack<Vec2f> {

	public AnimTrackVec2f(Map<Float, Vec2f> keyframes) {
		super(keyframes);
	}

	@Override
	public Vec2f valueAt(float time) {
		int prev = prevKeyframe(time);
		int next = nextKeyframe(time);
		float f = times[next]-times[prev];
		if(f != 0)
			f = (time-times[prev])/f;
		return Vec2f.lerp((Vec2f)values[prev], (Vec2f)values[next], f);
	}

}
