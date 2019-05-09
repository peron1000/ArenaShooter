package arenashooter.engine.animation;

import java.util.List;
import java.util.Map;

import arenashooter.engine.animation.animevents.AnimEvent;
import arenashooter.engine.animation.tracks.AnimTrackDouble;
import arenashooter.engine.animation.tracks.AnimTrackTexture;
import arenashooter.engine.animation.tracks.AnimTrackVec2f;
import arenashooter.engine.animation.tracks.AnimTrackVec3f;
import arenashooter.engine.animation.tracks.EventTrack;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;

public abstract class Animation {

	public boolean looping=false;
	
	private double time=0;
	
	private double length;
	
	private double playSpeed = 1;
	
	private boolean playing = true;
	
	private EventTrack eventTrack;
	
	private Map<String, AnimTrackDouble>  tracksD;
	private Map<String, AnimTrackTexture> tracksT;
	private Map<String, AnimTrackVec2f>   tracksVec2f;
	private Map<String, AnimTrackVec3f>   tracksVec3f;
	
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
	
	/**
	 * Get current value from a double track
	 * @param track name
	 * @return current value or default if inexistant track
	 */
	public double getTrackD(String track) {
		if(tracksD.get(track) == null) {
			System.err.println("Animation - Fetching inexistant animation track \""+track+"\" in "+this+", returning default value (0.0)");
			return 0.0;
		}
		return tracksD.get(track).valueAt(time);
	}

	/**
	 * Get current value from a Texture track
	 * @param track name
	 * @return current value or default if inexistant track
	 */
	public Texture getTrackTex(String track) {
		if(tracksT.get(track) == null) {
			System.err.println("Animation - Fetching inexistant animation track \""+track+"\" in "+this+", returning default texture");
			return Texture.default_tex;
		}
		return tracksT.get(track).valueAt(time);
	}

	/**
	 * Get current value from a Vec2f track
	 * @param track name
	 * @return current value or default if inexistant track
	 */
	public Vec2f getTrackVec2f(String track) {
		if(tracksVec2f.get(track) == null) {
			System.err.println("Animation - Fetching inexistant animation track \""+track+"\" in "+this+", returning default value (0, 0)");
			return new Vec2f();
		}
		return tracksVec2f.get(track).valueAt(time);
	}

	/**
	 * Get current value from a Vec3f track
	 * @param track name
	 * @return current value or default if inexistant track
	 */
	public Vec3f getTrackVec3f(String track) {
		if(tracksVec3f.get(track) == null) {
			System.err.println("Animation - Fetching inexistant animation track \""+track+"\" in "+this+", returning default value (0, 0, 0)");
			return new Vec3f();
		}
		return tracksVec3f.get(track).valueAt(time);
	}

}
