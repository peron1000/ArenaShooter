package arenashooter.engine.animation.tracks;

import java.util.HashMap;
import java.util.Map;

import arenashooter.engine.animation.AnimInterpolation;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec3f;

/**
 * Vec3f animation track with linear interpolation
 */
public class AnimTrackVec3f extends AnimTrack<Vec3f> {

	public AnimTrackVec3f(Map<Double, Vec3f> keyframes) {
		super(keyframes);
	}
	
	public AnimTrackVec3f(Map<Double, Vec3f> keyframes, AnimInterpolation interpMode) {
		super(keyframes, interpMode);
	}

	@Override
	public Vec3f valueAt(double time) {
		double f;
		switch(interpMode) {
		case CUBIC: //TODO: Test loop/non-loop
			int p1 = prevKeyframe(time);
			int p0 = prevKeyframe(p1);
			int p2 = nextKeyframe(time);
			int p3 = nextKeyframe(p2);
			
			if(p1 == 0) p0 = times.length-1;
			if(p2 == times.length-1) p3 = 0;

			f = times[p2]-times[p1];
			if(f != 0)
				f = (time-times[p1])/f;
			
			return Utils.catmullRomSpline3f((Vec3f)values[p0], (Vec3f)values[p1], (Vec3f)values[p2], (Vec3f)values[p3], f);
		default: //LINEAR
			int prev = prevKeyframe(time);
			int next = nextKeyframe(time);
			f = times[next]-times[prev];
			if(f != 0)
				f = (time-times[prev])/f;
			return Vec3f.lerp((Vec3f)values[prev], (Vec3f)values[next], f, new Vec3f());
		}
	}

	@Override
	public Map<Double, Vec3f> extractData() {
		Map<Double, Vec3f> keyframes = new HashMap<>();
		for(int i=0; i<times.length; i++)
			keyframes.put(times[i], ((Vec3f)values[i]).clone());
		return keyframes;
	}

	@Override
	public AnimTrackVec3f clone() {
		return new AnimTrackVec3f(extractData(), interpMode);
	}
}
