package arenashooter.engine.animation.tracks;

import java.util.Map;

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
		int i=0;
		for(Map.Entry<Float, T> entry : keyframes.entrySet()) {
			times[i] = entry.getKey();
			values[i] = entry.getValue();
			i++;
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
