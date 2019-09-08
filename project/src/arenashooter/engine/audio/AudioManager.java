package arenashooter.engine.audio;

import org.apache.logging.log4j.Logger;

import arenashooter.engine.math.QuatI;
import arenashooter.engine.math.Vec2fi;
import arenashooter.engine.math.Vec3fi;

public interface AudioManager {
	/**
	 * Initialize the audio system. Don't forget to destroy it !
	 */
	public void init();
	
	/**
	 * Destroy the audio system
	 */
	public void destroy();
	
	/**
	 * This does not need to be called at every sub-step
	 */
	public void update();
	
	public Logger getLogger();
	
	/**
	 * Set the listener ("ears") to a specific location/rotation
	 * @param loc
	 * @param rot
	 */
	public void setListener(Vec3fi loc, QuatI rot);
	
	/**
	 * Play a sound (non-spatialized)
	 * @param file
	 * @param channel
	 * @param volume
	 * @param pitch
	 */
	public void playSound(String file, AudioChannel channel, float volume, float pitch);
	
	/**
	 * Play a sound (spatialized)
	 * @param file
	 * @param channel
	 * @param volume
	 * @param pitch
	 * @param position
	 */
	public void playSound2D(String file, AudioChannel channel, float volume, float pitch, Vec2fi position);
	
	/**
	 * Load a sound from a file or cache <br/>
	 * This is safe to call from any thread
	 * @param path sound file
	 * @return buffer object (new or existing if already loaded)
	 */
	public SoundBuffer loadSound(String path);
	
	/**
	 * Create a new source
	 * @param file
	 * @param channel
	 * @return Source or null
	 */
	public SoundSource createSource(String file, AudioChannel channel, float volume, float pitch);
	
	public float getMainVolume();
	
	public void setMainVolume(float newVolume);
	
	public float getChannelVolume(AudioChannel channel);
	
	public void setChannelVolume(AudioChannel channel, float newVolume);
}
