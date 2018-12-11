package arenashooter.engine.audio;

import static org.lwjgl.openal.AL10.AL_SOURCE_STATE;
import static org.lwjgl.openal.AL10.AL_PLAYING;
import static org.lwjgl.openal.AL10.AL_BUFFER;
import static org.lwjgl.openal.AL10.alGenSources;
import static org.lwjgl.openal.AL10.alSourcei;
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.AL10.alSourceStop;
import static org.lwjgl.openal.AL10.alGetSourcei;
import static org.lwjgl.openal.AL10.alDeleteSources;

/**
 * Object used to manage a sound with multiple sources. 
 * When playing, a new source is automatically chosen. 
 */
public class SoundEffect {
	private Sound sound;
	private int[] source;
	private int next = 0;
	
	/**
	 * 
	 * @param path path to the sound file (vorbis)
	 * @param maxPlays maximum simultaneous plays
	 */
	public SoundEffect(String path, int maxPlays) {
		sound = Sound.loadSound(path);
		
		source = new int[maxPlays];
		for( int i=0; i<source.length; i++ ) {
			source[i] = alGenSources();
		
			alSourcei( source[i], AL_BUFFER, sound.getBuffer() );
		}
	}
	
	/**
	 * Play this sound using a new source or by replacing the oldest one
	 */
	public void play() {
		alSourcePlay(source[next]);
		next++;
		if(next >= source.length) next = 0;
	}
	
	/**
	 * Stop all sources of this sound
	 */
	public void stop() {
		for( int i=0; i<source.length; i++ )
			alSourceStop(source[i]);
	}
	
	/**
	 * Is this sound playing
	 * @return at least one source of this sound is playing
	 */
	public boolean isPlaying() {
		for( int i=0; i<source.length; i++ )
			if( alGetSourcei(source[i], AL_SOURCE_STATE) == AL_PLAYING )
				return true;
		return false;
	}
	
	/**
	 * Remove the sources from memory
	 */
	public void destroy() {
		alDeleteSources(source);
	}

}
