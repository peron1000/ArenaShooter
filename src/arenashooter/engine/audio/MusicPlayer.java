package arenashooter.engine.audio;

import static org.lwjgl.openal.AL10.AL_SOURCE_STATE;
import static org.lwjgl.openal.AL10.AL_PLAYING;
import static org.lwjgl.openal.AL10.AL_BUFFER;
import static org.lwjgl.openal.AL10.AL_LOOPING;
import static org.lwjgl.openal.AL10.AL_TRUE;
import static org.lwjgl.openal.AL10.AL_FALSE;
import static org.lwjgl.openal.AL10.alGenSources;
import static org.lwjgl.openal.AL10.alSourcei;
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.AL10.alSourcePause;
import static org.lwjgl.openal.AL10.alSourceStop;
import static org.lwjgl.openal.AL10.alGetSourcei;
import static org.lwjgl.openal.AL10.alDeleteSources;

/**
 * Object used to manage a sound with multiple sources. 
 * When playing, a new source is automatically chosen. 
 */
public class MusicPlayer {
	private Sound sound;
	private int source;
	private boolean looping = false;
	
	/**
	 * 
	 * @param path path to the sound file (vorbis)
	 * @param maxPlays maximum simultaneous plays ( must be >0 )
	 */
	public MusicPlayer(String path, boolean looping) {
		sound = Sound.loadSound(path);
		
		source = alGenSources();
		
		alSourcei( source, AL_BUFFER, sound.getBuffer() );
		
		setLooping(looping);
	}
	
	/**
	 * Play this sound using a new source or by replacing the oldest one
	 */
	public void play() {
		alSourcePlay(source);
	}
	
	/**
	 * Stop this sound
	 */
	public void stop() {
		alSourceStop(source);
	}
	
	/**
	 * Pause this sound
	 */
	public void pause() {
		alSourcePause(source);
	}
	
	/**
	 * Is this sound playing
	 */
	public boolean isPlaying() {
		return alGetSourcei(source, AL_SOURCE_STATE) == AL_PLAYING;
	}
	
	public boolean isLooping() { return looping; }
	
	public void setLooping(boolean looping) {
		if( looping == this.looping ) return;
		this.looping = looping;
		
		alSourcei(source, AL_LOOPING,  looping ? AL_TRUE : AL_FALSE);
	}
	
	/**
	 * Remove the source from memory
	 */
	public void destroy() {
		alDeleteSources(source);
	}

}
