package arenashooter.engine.audio;

import static org.lwjgl.openal.AL10.AL_SOURCE_STATE;
import static org.lwjgl.openal.AL10.AL_PLAYING;
import static org.lwjgl.openal.AL10.AL_BUFFER;
import static org.lwjgl.openal.AL10.alGenSources;
import static org.lwjgl.openal.AL10.alSourcei;
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.AL10.alSourcePause;
import static org.lwjgl.openal.AL10.alSourceStop;
import static org.lwjgl.openal.AL10.alGetSourcei;

/**
 * Object used to manage a sound with multiple sources. 
 * When playing, a new source is automatically chosen. 
 */
public class SoundSource implements AudioSourceI {
	private Sound sound;
	private int[] source;
	private int next = 0;
	
	/**
	 * 
	 * @param path path to the sound file (vorbis)
	 * @param maxPlays maximum simultaneous plays ( must be >0 )
	 */
	public SoundSource(String path, int maxPlays) {
		sound = Sound.loadSound(path);
		
		if( sound == null ) return;
		
		source = new int[Math.max(1, maxPlays)];
		
		for( int i=0; i<source.length; i++ ) {
			source[i] = alGenSources();
		
			alSourcei( source[i], AL_BUFFER, sound.getBuffer() );
		}
		
		Audio.registerPlayer(this);
	}
	
	/**
	 * Play this sound using a new source or by replacing the oldest one
	 */
	@Override
	public void play() {
		if( sound == null ) return;
		
		alSourcePlay(source[next]);
		next++;
		if(next >= source.length) next = 0;
	}
	
	/**
	 * Stop all sources of this sound
	 */
	@Override
	public void stop() {
		if( sound == null ) return;
		
		for( int i=0; i<source.length; i++ )
			alSourceStop(source[i]);
	}
	
	/**
	 * Pause all sources of this sound
	 */
	public void pause() {
		if( sound == null ) return;
		
		for( int i=0; i<source.length; i++ )
			alSourcePause(source[i]);
	}
	
	/**
	 * Is this sound playing
	 * @return at least one source of this sound is playing
	 */
	@Override
	public boolean isPlaying() {
		if( sound == null ) return false;
		
		for( int i=0; i<source.length; i++ )
			if( alGetSourcei(source[i], AL_SOURCE_STATE) == AL_PLAYING )
				return true;
		return false;
	}
	
	@Override
	public int[] getSources() { return source.clone(); }

}
