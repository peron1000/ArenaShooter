package arenashooter.entities.spatials;

import arenashooter.engine.audio.Audio;
import arenashooter.engine.audio.AudioSourceI;
import arenashooter.engine.audio.SoundSourceMulti;
import arenashooter.engine.audio.SoundSourceSingle;
import arenashooter.engine.math.Vec2f;

/**
 * Spatial entity containing a sound effect.
 * The sound is always moved to this entity's location
 */
public class SoundEffect extends Spatial {
	private AudioSourceI sound;

	/**
	 * Create a new spatialized sound effect
	 * @param position initial location of the sound
	 * @param path audio file
	 * @param maxPlays maximum simultaneous plays of this sound ()
	 */
	public SoundEffect( Vec2f position, String path, int maxPlays ) {
		this(position, path, maxPlays, 1, 1);
	}
	
	/**
	 * Create a new spatialized sound effect with random pitch
	 * @param position initial location of the sound
	 * @param path audio file
	 * @param maxPlays maximum simultaneous plays of this sound (-1 for a looping sound)
	 * @param pitchMin minimum pitch
	 * @param pitchMax maximum pitch
	 */
	public SoundEffect( Vec2f position, String path, int maxPlays, float pitchMin, float pitchMax ) {
		super(position);
		
		if( maxPlays < 0 )
			sound = new SoundSourceSingle(path, pitchMin, pitchMax, true, true);
		else if( maxPlays == 0 ) {
			Audio.log.error("Invalid maxPlays value (0), defaulting to 1 (for +"+path+")");
			sound = new SoundSourceSingle(path, pitchMin, pitchMax, true, false);
		} else if( maxPlays == 1 )
			sound = new SoundSourceSingle(path, pitchMin, pitchMax, true, false);
		else
			sound = new SoundSourceMulti(path, maxPlays, pitchMin, pitchMax, true);
		
		if( sound instanceof SoundSourceMulti )
			((SoundSourceMulti)sound).setPositions( this.parentPosition );
		else if( sound instanceof SoundSourceSingle )
			((SoundSourceSingle)sound).setPosition( this.parentPosition );
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
		if(getArena() == null) sound.stop();
		
		if( sound instanceof SoundSourceMulti )
			((SoundSourceMulti)sound).setPositions( parentPosition );
		else if( sound instanceof SoundSourceSingle )
			((SoundSourceSingle)sound).setPosition( parentPosition );

		super.step(d);
	}
}
