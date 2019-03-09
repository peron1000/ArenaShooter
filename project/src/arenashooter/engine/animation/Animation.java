package arenashooter.engine.animation;

import java.util.List;

import arenashooter.engine.animation.animevents.AnimEvent;
import arenashooter.engine.animation.tracks.AnimTrack;
import arenashooter.engine.animation.tracks.EventTrack;

public abstract class Animation {

	public boolean looping=false;
	
	private double time=0;
	
	private double length;
	
	private double playSpeed = 1;
	
	private boolean playing = true;
	
	private EventTrack eventTrack;
	private AnimTrack[] tracks;
	
	public Animation() {
		// TODO Auto-generated constructor stub
	}
	
	public void play() {
		playing = true;
	}
	
	public boolean isPlaying() { return playing; }
	
	public void step(double delta) {
		if(playing) {
			double oldTime = time;
			time += delta*playSpeed;
			
			if(time >= length) {
				if(looping)
					time = time % length;
				else
					time = length;
			}
			
			List<AnimEvent> events = eventTrack.getEvents(oldTime, time);
			//TODO: execute events
		}
	}

}
