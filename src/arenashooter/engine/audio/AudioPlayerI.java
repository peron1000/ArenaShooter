package arenashooter.engine.audio;

public interface AudioPlayerI {
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
	 * @return an array of all the openAL sources used by this
	 */
	int[] getSources();
}
