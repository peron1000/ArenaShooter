package arenashooter.entities;

import arenashooter.engine.audio.SoundSource;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.spatials.Spatial;

/**
 * Spatial entity containing a sound effect.
 * The sound is always moved to this entity's location
 */
public class SoundEffect extends Spatial {
	private SoundSource sound;

	public SoundEffect( Vec2f position, String path, int maxPlays ) {
		this(position, path, maxPlays, .8f, 1.2f);
	}
	
	public SoundEffect( Vec2f position, String path, int maxPlays, float pichMin, float pitchMax ) {
		super(position);
		sound = new SoundSource(path, maxPlays, pichMin, pitchMax, true);
		sound.setPositions( this.position );
	}
	
	/**
	 * Play this sound using a new source or by replacing the oldest one
	 */
	public void play() {
		sound.play();
	}
	
	/**
	 * Is this sound playing
	 * @return at least one source of this sound is playing
	 */
	public boolean isPlaying() {
		return sound.isPlaying();
	}
	
	/**
	 * Stop all sources of this sound
	 */
	public void stop() {
		sound.stop();
	}
	
	/**
	 * Set volume multiplier for all the sources
	 * @param volume new volume multiplier
	 */
	public void setVolume(float volume) {
		sound.setVolume(volume);
	}

	@Override
	public void step(double d) {
		sound.setPositions( position );

		super.step(d);
	}
}
