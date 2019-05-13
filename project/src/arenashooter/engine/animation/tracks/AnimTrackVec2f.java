package arenashooter.engine.animation.tracks;

import java.util.Map;

import arenashooter.engine.animation.AnimInterpolation;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;

/**
 * Vec2f animation track with linear interpolation
 */
public class AnimTrackVec2f extends AnimTrack<Vec2f> {

	public AnimTrackVec2f(Map<Double, Vec2f> keyframes) {
		super(keyframes);
	}
	
	public AnimTrackVec2f(Map<Double, Vec2f> keyframes, AnimInterpolation interpMode) {
		super(keyframes, interpMode);
	}

	@Override
	public Vec2f valueAt(double time) {
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
			
			return Utils.catmullRomSpline2f((Vec2f)values[p0], (Vec2f)values[p1], (Vec2f)values[p2], (Vec2f)values[p3], f);
		default: //LINEAR
			int prev = prevKeyframe(time);
			int next = nextKeyframe(time);
			f = times[next]-times[prev];
			if(f != 0)
				f = (time-times[prev])/f;
			return Vec2f.lerp((Vec2f)values[prev], (Vec2f)values[next], f);
		}
	}

}
