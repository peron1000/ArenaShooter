package arenashooter.engine.animation.tracks;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import arenashooter.engine.animation.animevents.AnimEvent;

public class EventTrack {
	protected float[] times;
	protected AnimEvent[] values;

	public EventTrack(Map<Float, AnimEvent> keyframes) {
		times = new float[keyframes.size()];
		values = new AnimEvent[keyframes.size()];
		
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
	
	List<AnimEvent> getEvents(float from, float to) {
		LinkedList<AnimEvent> res = new LinkedList<>();
		
		float current = from;
		while(current < to) {
			int next = nextKeyframe(current);
			
			res.add(values[next]);
			
			current = times[next];
			
			//TODO: loop if to<from
		}
		
		return res;
	}

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
