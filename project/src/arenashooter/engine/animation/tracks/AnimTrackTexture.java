package arenashooter.engine.animation.tracks;

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

}
