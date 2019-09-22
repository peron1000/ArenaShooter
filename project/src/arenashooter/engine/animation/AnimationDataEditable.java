package arenashooter.engine.animation;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import arenashooter.engine.animation.animevents.AnimEvent;
import arenashooter.engine.animation.tracks.AnimTrackDouble;
import arenashooter.engine.animation.tracks.AnimTrackTexture;
import arenashooter.engine.animation.tracks.AnimTrackVec2f;
import arenashooter.engine.animation.tracks.AnimTrackVec3f;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;

public class AnimationDataEditable {
	public double length;

	public boolean loop;
	
	public Map<Double, AnimEvent>		 trackEvents;
	public Map<String, Map<Double, Double>>  tracksD;
	public Map<String, Map<Double, Texture>> tracksT;
	public Map<String, Map<Double, Vec2f>>   tracksVec2f;
	public Map<String, Map<Double, Vec3f>>   tracksVec3f;
	
	public AnimationDataEditable(AnimationData source) {
		length = source.length;
		loop = source.loop;
		
		trackEvents = source.eventTrack.extractData();
		
		tracksD = new HashMap<>();
		for(Entry<String, AnimTrackDouble> track : source.tracksD.entrySet())
			tracksD.put(track.getKey(), track.getValue().extractData());
		
		tracksT = new HashMap<>();
		for(Entry<String, AnimTrackTexture> track : source.tracksT.entrySet())
			tracksT.put(track.getKey(), track.getValue().extractData());
		
		tracksVec2f = new HashMap<>();
		for(Entry<String, AnimTrackVec2f> track : source.tracksVec2f.entrySet())
			tracksVec2f.put(track.getKey(), track.getValue().extractData());
		
		tracksVec3f = new HashMap<>();
		for(Entry<String, AnimTrackVec3f> track : source.tracksVec3f.entrySet())
			tracksVec3f.put(track.getKey(), track.getValue().extractData());
	}

	public AnimationDataEditable() {
		length = 1;
		loop = true;
		trackEvents = new HashMap<>();
		tracksD = new HashMap<>();
		tracksT = new HashMap<>();
		tracksVec2f = new HashMap<>();
		tracksVec3f = new HashMap<>();
	}
}
