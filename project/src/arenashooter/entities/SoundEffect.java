package arenashooter.entities;

import arenashooter.engine.audio.AudioSourceI;
import arenashooter.engine.audio.SoundSourceMulti;
import arenashooter.engine.audio.SoundSourceSingle;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.spatials.Spatial;

/**
 * Spatial entity containing a sound effect.
 * The sound is always moved to this entity's location
 */
public class SoundEffect extends Spatial {
	private AudioSourceI sound;

	public SoundEffect( Vec2f position, String path, int maxPlays ) {
		this(position, path, maxPlays, .8f, 1.2f);
	}
	
	public SoundEffect( Vec2f position, String path, int maxPlays, float pitchMin, float pitchMax ) {
		super(position);
		
		if( maxPlays < 0 )
			sound = new SoundSourceSingle(path, pitchMin, pitchMax, true, true);
		else if( maxPlays == 0 )
			sound = new SoundSourceSingle(path, pitchMin, pitchMax, true, false);
		else
			sound = new SoundSourceMulti(path, maxPlays, pitchMin, pitchMax, true);
		
		if( sound instanceof SoundSourceMulti )
			((SoundSourceMulti)sound).setPositions( this.position );
		else if( sound instanceof SoundSourceSingle )
			((SoundSourceSingle)sound).setPosition( this.position );
	}
	
	public AudioSourceI getSound() { return sound; }
	
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
		if( sound instanceof SoundSourceMulti )
			((SoundSourceMulti)sound).setPositions( position );
		else if( sound instanceof SoundSourceSingle )
			((SoundSourceSingle)sound).setPosition( position );

		super.step(d);
	}
}
