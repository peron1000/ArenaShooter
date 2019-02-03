package arenashooter.engine.animation.tracks;

import java.util.Map;

import arenashooter.engine.math.Utils;

/**
 * Double animation track with linear interpolation
 */
public class AnimTrackDouble extends AnimTrack<Double> {

	public AnimTrackDouble(Map<Float, Double> keyframes) {
		super(keyframes);
	}

	@Override
	public Double valueAt(float time) {
		int prev = prevKeyframe(time);
		int next = nextKeyframe(time);
		float f = times[next]-times[prev];
		if(f != 0)
			f = (time-times[prev])/f;
		return Utils.lerpD((double)values[prev], (double)values[next], f);
	}

}
