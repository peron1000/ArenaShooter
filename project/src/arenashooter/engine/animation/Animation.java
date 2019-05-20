package arenashooter.engine.animation;

import java.util.Queue;

import arenashooter.engine.animation.animevents.AnimEvent;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;

/**
 * Instance of an animation
 */
public class Animation {
	
	private final AnimationData animData;

	private double time=0;
	
	private double playSpeed = 1;
	
	private boolean playing = false;
	
	private Queue<AnimEvent> events;
	
	/**
	 * Create a new instance of an animation
	 * @param data
	 */
	public Animation(AnimationData data) {
		this.animData = data;
	}
	
	/**
	 * Start or resume playing the animation
	 */
	public void play() {
		playing = true;
	}
	
	/**
	 * @set playing to false
	 */
	public void stopPlaying() {
		playing = false;
	}
	
	/**
	 * @return is the animation currently playing
	 */
	public boolean isPlaying() { return playing; }
	
	/**
	 * @return current play position in seconds
	 */
	public double getTime() { return time; }
	
	/**
	 * Set current play position
	 * @param newTime in seconds
	 */
	public void setTime(double newTime) { time = newTime; }
	
	/**
	 * @return total duration of the animation in seconds
	 */
	public double getLength() { return animData.length; }
	
	/**
	 * Update the animation and fill the event queue
	 * @param delta time in seconds
	 */
	public void step(double delta) {
		if(playing) {
			double oldTime = time;
			time += delta*playSpeed;
			
			if(time >= animData.length) {
				if(animData.loop)
					time = time % animData.length;
				else
					time = animData.length;
			}
			
			Queue<AnimEvent> newEvents = animData.eventTrack.getEvents(oldTime, time);
			for(AnimEvent event : newEvents)
				events.add(event);
		}
	}
	
	/**
	 * @return Queue of events to execute
	 */
	public Queue<AnimEvent> getEvents() {
		return events;
	}
	
	/**
	 * Get current value from a double track
	 * @param track name
	 * @return current value or default (0.0) if inexistent track
	 */
	public double getTrackD(String track) {
		if(animData.tracksD.containsKey(track)) 
			return animData.tracksD.get(track).valueAt(time);
		else {
			System.err.println("Animation - Fetching inexistant animation track \""+track+"\" in "+this+", returning default value (0.0)");
			return 0.0;
		}
	}

	/**
	 * Get current value from a Texture track
	 * @param track name
	 * @return current value or default if inexistent track
	 */
	public Texture getTrackTex(String track) {
		if(animData.tracksT.containsKey(track))
			return animData.tracksT.get(track).valueAt(time);
		else {
			System.err.println("Animation - Fetching inexistant animation track \""+track+"\" in "+this+", returning default texture");
			return Texture.default_tex;
		}
	}

	/**
	 * Get current value from a Vec2f track
	 * @param track name
	 * @return current value or default if inexistent track
	 */
	public Vec2f getTrackVec2f(String track) {
		if(animData.tracksVec2f.containsKey(track)) 
			return animData.tracksVec2f.get(track).valueAt(time);
		else {
			System.err.println("Animation - Fetching inexistant animation track \""+track+"\" in "+this+", returning default value (0, 0)");
			return new Vec2f();
		}
	}

	/**
	 * Get current value from a Vec3f track
	 * @param track name
	 * @return current value or default if inexistant track
	 */
	public Vec3f getTrackVec3f(String track) {
		if(animData.tracksVec3f.containsKey(track)) 
			return animData.tracksVec3f.get(track).valueAt(time);
		else {
			System.err.println("Animation - Fetching inexistant animation track \""+track+"\" in "+this+", returning default value (0, 0, 0)");
			return new Vec3f();
		}
	}

}
