package arenashooter.engine.animation.tracks;

import java.util.HashMap;
import java.util.Map;

import arenashooter.engine.graphics.Texture;

/**
 * Texture animation track without interpolation
 */
public class AnimTrackTexture extends AnimTrack<Texture> {

	public AnimTrackTexture(Map<Double, Texture> keyframes) {
		super(keyframes);
	}

	@Override
	public Texture valueAt(double time) {
		return (Texture)values[prevKeyframe(time)];
	}

	@Override
	public Map<Double, Texture> extractData() {
		Map<Double, Texture> keyframes = new HashMap<>();
		for(int i=0; i<times.length; i++)
			keyframes.put(times[i], Texture.loadTexture( ((Texture)values[i]).getPath() ));
		return keyframes;
	}
	
	@Override
	public AnimTrackTexture clone() {
		return new AnimTrackTexture(extractData());
	}

}
