package arenashooter.entities.spatials;

import arenashooter.engine.audio.Audio;
import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.audio.SoundSource;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Arena;

/**
 * Spatial entity containing a sound effect.
 * The sound is always moved to this entity's location
 */
public class SoundEffect extends Spatial {
	private SoundSource source = null;
	
	private final String path;
	private float volume, pitch;
	private boolean looping;
	private final AudioChannel channel;

	/**
	 * Create a new spatialized sound effect
	 * @param position initial location of the sound
	 * @param path audio file
	 * @param channel
	 */
	public SoundEffect( Vec2f localPosition, String path, AudioChannel channel ) {
		this(localPosition, path, channel, 1, 1, 1, false);
	}
	
	/**
	 * Create a new spatialized sound effect
	 * @param position initial location of the sound
	 * @param path audio file
	 * @param channel
	 * @param volume
	 * @param pitch
	 * @param looping
	 */
	public SoundEffect( Vec2f localPosition, String path, AudioChannel channel, float volume, float pitch, boolean looping ) {
		this(localPosition, path, channel, volume, pitch, pitch, looping);
	}
	
	/**
	 * Create a new spatialized sound effect with random pitch
	 * @param position initial location of the sound
	 * @param path audio file
	 * @param channel
	 * @param volume
	 * @param pitchMin minimum pitch
	 * @param pitchMax maximum pitch
	 * @param looping
	 */
	public SoundEffect( Vec2f localPosition, String path, AudioChannel channel, float volume, float pitchMin, float pitchMax, boolean looping ) {
		super(localPosition);
		
		this.path = path;
		this.pitch = Utils.lerpF(pitchMin, pitchMax, Math.random());
		this.volume = volume;
		this.looping = looping;
		this.channel = channel;
	}
	
	/**
	 * Play this sound using a new source or by replacing the oldest one
	 */
	public void play() {
		if(getArena() == null) {
			Audio.log.warn("Sound "+path+" isn't attached in world and won't be played");
			return;
		}
		if(source == null)
			createSource();
		if(source != null) {
			source.play();
			if(!source.isPlaying())
				Audio.log.error("Despite having a valid source, \""+path+"\" is not playing");
		}
	}
	
	/**
	 * Is this sound playing
	 * @return at least one source of this sound is playing
	 */
	public boolean isPlaying() {
		if(source == null) return false;
		return source.isPlaying();
	}
	
	/**
	 * Stop all sources of this sound
	 */
	public void stop() {
		if(source != null)
			source.stop();
	}
	
	/**
	 * Set volume multiplier for all the sources
	 * @param volume new volume multiplier
	 */
	public void setVolume(float volume) {
		this.volume = volume;
		if(source != null)
			source.setVolume(volume);
	}
	
	public float getPitch() { return pitch; }
	
	public void setPitch(float newPitch) {
		pitch = newPitch;
		if(source != null)
			source.setPitch(newPitch);
	}
	
	@Override
	protected void recursiveDetach(Arena oldArena) {
		super.recursiveDetach(oldArena);
		
		destroySource();
	}
	
	private void createSource() {
		if(source != null) destroySource();
		source = Audio.createSource(path, channel, volume, pitch);
		if(source == null) return;
		source.setLooping(looping);
		source.setPosition2D(getWorldPos());
	}
	
	/**
	 * Destroy source and set it to null
	 */
	private void destroySource() {
		if(source == null) return;
		source.destroy();
		source = null;
	}

	@Override
	public void step(double d) {
		super.step(d);
		
		if(getArena() == null) destroySource();
		
		if(source != null)
			source.setPosition2D(getWorldPos());
	}
	
	/**
	 * Create a copy of this SoundEffect (cloned transform, path, volume, pitch, looping boolean)
	 */
	@Override
	public SoundEffect clone() {
		return new SoundEffect(localPosition, path, channel, volume, pitch, looping);
	}
}
