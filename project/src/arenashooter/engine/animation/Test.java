package arenashooter.engine.animation;

import java.util.HashMap;

import arenashooter.engine.animation.tracks.AnimTrackVec2f;
import arenashooter.engine.math.Vec2f;

public class Test {

	public static void main(String[] args) {
		AnimTrackVec2f track;
		HashMap<Float, Vec2f> keyframes = new HashMap<>();
		keyframes.put(1f, new Vec2f(1));
		keyframes.put(2f, new Vec2f(2));
		keyframes.put(3.5f, new Vec2f(-1, 1));
		track = new AnimTrackVec2f(keyframes);
		
		float time = 0;
		while(time<=3.51f) {
			System.out.println(String.format("%.1f", time)+": "+track.valueAt(time));
			time+=.1f;
		}
	}

}
