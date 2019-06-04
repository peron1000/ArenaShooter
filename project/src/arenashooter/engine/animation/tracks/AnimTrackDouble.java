package arenashooter.engine.animation.tracks;

import java.util.HashMap;
import java.util.Map;

import arenashooter.engine.animation.AnimInterpolation;
import arenashooter.engine.math.Utils;

/**
 * Double animation track with linear interpolation
 */
public class AnimTrackDouble extends AnimTrack<Double> {
	public AnimTrackDouble(Map<Double, Double> keyframes) {
		super(keyframes);
	}
	
	public AnimTrackDouble(Map<Double, Double> keyframes, AnimInterpolation interpMode) {
		super(keyframes, interpMode);
	}

	@Override
	public Double valueAt(double time) {
		int prev = prevKeyframe(time);
		int next = nextKeyframe(time);
		double f = times[next]-times[prev];
		if(f != 0)
			f = (time-times[prev])/f;
		return Utils.lerpD((double)values[prev], (double)values[next], f);
	}

	@Override
	public Map<Double, Double> extractData() {
		Map<Double, Double> keyframes = new HashMap<>();
		for(int i=0; i<times.length; i++)
			keyframes.put(times[i], ((Double)values[i]).doubleValue());
		return keyframes;
	}
	
	@Override
	public AnimTrackDouble clone() {
		return new AnimTrackDouble(extractData(), interpMode);
	}

}
