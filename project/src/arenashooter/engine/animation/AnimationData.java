package arenashooter.engine.animation;

import java.util.HashMap;
import java.util.Map;

import arenashooter.engine.animation.tracks.AnimTrackDouble;
import arenashooter.engine.animation.tracks.AnimTrackTexture;
import arenashooter.engine.animation.tracks.AnimTrackVec2f;
import arenashooter.engine.animation.tracks.AnimTrackVec3f;
import arenashooter.engine.animation.tracks.EventTrack;

public class AnimationData {

	private static Map<String, AnimationData> cache = new HashMap<String, AnimationData>();
	
	final double length;

	final boolean looping;
	
	final EventTrack eventTrack;
	
	final Map<String, AnimTrackDouble>  tracksD;
	final Map<String, AnimTrackTexture> tracksT;
	final Map<String, AnimTrackVec2f>   tracksVec2f;
	final Map<String, AnimTrackVec3f>   tracksVec3f;

	private AnimationData(double length, boolean loop, EventTrack eventTrack,  
			Map<String, AnimTrackDouble> tracksD, Map<String, AnimTrackTexture> tracksT, 
			Map<String, AnimTrackVec2f> tracksVec2f, Map<String, AnimTrackVec3f> tracksVec3f) {
		looping = loop;
		this.length = length;
		this.eventTrack = eventTrack;
		this.tracksD = tracksD;
		this.tracksT = tracksT;
		this.tracksVec2f = tracksVec2f;
		this.tracksVec3f = tracksVec3f;
	}
	
	public static AnimationData loadAnim(String path) {
		AnimationData result = cache.get(path);
		if(result != null) return result;
		return null; //TODO: load from file
	}
	
}
