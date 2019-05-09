package arenashooter.engine.animation.tracks;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import arenashooter.engine.animation.animevents.AnimEvent;

public class EventTrack {
	protected double[] times;
	protected AnimEvent[] values;

	public EventTrack(Map<Double, AnimEvent> keyframes) {
		times = new double[keyframes.size()];
		values = new AnimEvent[keyframes.size()];
		
		//Make sure keyframes times are sorted
		ArrayList<Double> timesList = new ArrayList<>(keyframes.size());
		timesList.addAll(keyframes.keySet());
		timesList.sort( new Comparator<Double>() {
			public int compare(Double d1, Double d2) {
				return d1.compareTo(d2);
			}
		});

		for(int i=0; i<timesList.size(); i++) {
			times[i] = timesList.get(i);
			values[i] = keyframes.get(times[i]);
		}
	}
	
	/**
	 * Get all anim events between two time stamps
	 * @param from
	 * @param to
	 * @return
	 */
	public Queue<AnimEvent> getEvents(double from, double to) {
		Queue<AnimEvent> res = new LinkedList<>();
		
		double current = from;
		while(current < to) {
			int next = nextKeyframe(current);
			
			res.add(values[next]);
			
			current = times[next];
			
			//TODO: loop if to<from
		}
		
		return res;
	}

	protected int prevKeyframe(double time) {
		for(int i=times.length-1; i>=0; i--) {
			if(times[i]<=time) return i;
		}
		return 0;
	}
	
	protected int nextKeyframe(double time) {
		for(int i=0; i<times.length; i++) {
			if(times[i]>time) return i;
		}
		return times.length-1;
	}
}
