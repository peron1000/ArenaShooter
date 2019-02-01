package arenashooter.engine.animation.tracks;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

/**
 * Abstract class for a keyframe animation track
 * @param <T> Animated data type
 */
public abstract class AnimTrack<T> {
	protected float[] times;
	protected Object[] values;
	
	public AnimTrack(Map<Float, T> keyframes) {
		times = new float[keyframes.size()];
		values = new Object[keyframes.size()];
		
		//Make sure keyframes times are sorted
		ArrayList<Float> timesList = new ArrayList<>(keyframes.size());
		timesList.addAll(keyframes.keySet());
		timesList.sort( new Comparator<Float>() {
			public int compare(Float f1, Float f2) {
				return f1.compareTo(f2);
			}
		});

		for(int i=0; i<timesList.size(); i++) {
			times[i] = timesList.get(i);
			values[i] = keyframes.get(times[i]);
		}
	}

	/**
	 * Get the value of this track at a specified time
	 * @param time
	 * @return value
	 */
	public abstract T valueAt(float time);
	
	protected int prevKeyframe(float time) {
		for(int i=times.length-1; i>=0; i--) {
			if(times[i]<=time) return i;
		}
		return 0;
	}
	
	protected int nextKeyframe(float time) {
		for(int i=0; i<times.length; i++) {
			if(times[i]>time) return i;
		}
		return times.length-1;
	}
}
