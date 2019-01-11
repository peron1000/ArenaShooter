package arenashooter.engine.audio;

public interface AudioSourceI {
	/**
	 * Play this sound
	 */
	public void play();
	
	/**
	 * Stop this sound
	 */
	public void stop();
	
	/**
	 * Is this sound playing
	 * @return this sound is playing
	 */
	public boolean isPlaying();
	
	/**
	 * Set volume multiplier for all the sources
	 * @param volume new volume multiplier
	 */
	public void setVolume(float volume);
	
	/**
	 * @return an array of all the openAL sources used by this
	 */
	int[] getSources();
}
