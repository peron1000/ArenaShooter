package arenashooter.entities;

import arenashooter.engine.audio.SoundSource;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.spatials.Spatial;

public class SoundEffect extends Spatial {
	private SoundSource sound;

	public SoundEffect( Vec2f position, String path ) {
		super(position);
		sound = new SoundSource(path, 4, .8f, 1.2f, true);
		sound.setPosition( this.position );
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
		sound.setPosition( position );

		super.step(d);
	}
}
