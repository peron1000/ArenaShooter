package arenashooter.engine.animation;

import java.util.LinkedList;
import java.util.Queue;

import arenashooter.engine.animation.animevents.AnimEvent;
import arenashooter.engine.graphics.GLTexture;
import arenashooter.engine.graphics.TextureI;
import arenashooter.engine.graphics.GLRenderer;
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
	
	private Queue<AnimEvent> events = new LinkedList<AnimEvent>();
	
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
	
	public double getPlaySpeed() { return playSpeed; }
	
	public void setplaySpeed(double speed) { playSpeed = speed; }
	
	/**
	 * Update the animation and fill the event queue
	 * @param delta time in seconds
	 */
	public void step(double delta) {
		if(playing) {
			if(!animData.loop && time >= animData.length)
				playing = false;
			
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
			GLRenderer.log.error("Fetching inexistant animation track \""+track+"\" in "+this+", returning default value (0.0)");
			return 0.0;
		}
	}

	/**
	 * Get current value from a Texture track
	 * @param track name
	 * @return current value or default if inexistent track
	 */
	public TextureI getTrackTex(String track) {
		if(animData.tracksT.containsKey(track))
			return animData.tracksT.get(track).valueAt(time);
		else {
			GLRenderer.log.error("Fetching inexistant animation track \""+track+"\" in "+this+", returning default texture");
			return GLTexture.default_tex;
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
			GLRenderer.log.error("Fetching inexistant animation track \""+track+"\" in "+this+", returning default value (0, 0)");
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
			GLRenderer.log.error("Fetching inexistant animation track \""+track+"\" in "+this+", returning default value (0, 0, 0)");
			return new Vec3f();
		}
	}
	
	/**
	 * @param track
	 * @return if <i>track</i> exists and is a double track
	 */
	public boolean hasTrackD(String track) {
		return animData.tracksD.containsKey(track);
	}

	/**
	 * @param track
	 * @return if <i>track</i> exists and is a texture track
	 */
	public boolean hasTrackTex(String track) {
		return animData.tracksT.containsKey(track);
	}

	/**
	 * @param track
	 * @return if <i>track</i> exists and is a Vec2f track
	 */
	public boolean hasTrackVec2f(String track) {
		return animData.tracksVec2f.containsKey(track);
	}

	/**
	 * @param track
	 * @return if <i>track</i> exists and is a Vec3f track
	 */
	public boolean hasTrackVec3f(String track) {
		return animData.tracksVec3f.containsKey(track);
	}

}
