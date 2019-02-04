package arenashooter.engine.animation.tracks;

import java.util.Map;

import arenashooter.engine.graphics.Texture;

/**
 * Texture animation track without interpolation
 */
public class AnimTrackTexture extends AnimTrack<Texture> {

	public AnimTrackTexture(Map<Float, Texture> keyframes) {
		super(keyframes);
	}

	@Override
	public Texture valueAt(float time) {
		return (Texture)values[prevKeyframe(time)];
	}

}
