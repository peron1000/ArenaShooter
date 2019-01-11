package arenashooter.engine.audio;

import static org.lwjgl.openal.AL10.AL_SOURCE_STATE;
import static org.lwjgl.openal.AL10.AL_TRUE;
import static org.lwjgl.openal.AL10.AL_FALSE;
import static org.lwjgl.openal.AL10.AL_PLAYING;
import static org.lwjgl.openal.AL10.AL_BUFFER;
import static org.lwjgl.openal.AL10.AL_GAIN;
import static org.lwjgl.openal.AL10.AL_PITCH;
import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.AL_SOURCE_RELATIVE;
import static org.lwjgl.openal.AL10.alGenSources;
import static org.lwjgl.openal.AL10.alSourcei;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

import static org.lwjgl.openal.AL10.alSourcef;
import static org.lwjgl.openal.AL10.alSource3f;
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.AL10.alSourcePause;
import static org.lwjgl.openal.AL10.alSourceStop;
import static org.lwjgl.openal.AL10.alGetSourcei;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;

/**
 * Object used to manage a sound with multiple sources. 
 * When playing, a new source is automatically chosen. 
 */
public class SoundSource implements AudioSourceI {
	private Sound sound;
	private int[] source;
	private int next = 0;
	private float pitchMin, pitchMax;
	private boolean spatialized = false;
	
	/**
	 * 
	 * @param path path to the sound file (vorbis)
	 * @param maxPlays maximum simultaneous plays ( must be >0 )
	 */
	public SoundSource(String path, int maxPlays, float pitchMin, float pitchMax, boolean spatialized) {
		sound = Sound.loadSound(path);
		
		if( sound == null ) return;
		
		this.pitchMin = pitchMin;
		this.pitchMax = pitchMax;
		this.spatialized = spatialized;
		
		source = new int[Math.max(1, maxPlays)];
		
		for( int i=0; i<source.length; i++ ) {
			source[i] = alGenSources();
		
			//Link the source to the buffer
			alSourcei( source[i], AL_BUFFER, sound.getBuffer() );
			
			Audio.printError();
			
			//Spatialization
			if( spatialized ) {
				alSourcei( source[i], AL_SOURCE_RELATIVE, AL_FALSE );
				alSourcef( source[i], AL11.AL_REFERENCE_DISTANCE, 10 );
				alSourcef( source[i], AL11.AL_ROLLOFF_FACTOR, .0055f );
			} else {
				alSourcei( source[i], AL_SOURCE_RELATIVE, AL_TRUE );
				alSourcef( source[i], AL11.AL_REFERENCE_DISTANCE, 0 );
				alSourcef( source[i], AL11.AL_ROLLOFF_FACTOR, 0 );
			}
		}
		
		Audio.registerPlayer(this);
	}
	
	/**
	 * Play this sound using a new source or by replacing the oldest one
	 */
	@Override
	public void play() {
		if( sound == null ) return;
		alSourcef( source[next], AL_PITCH, Utils.lerpF(pitchMin, pitchMax, (float)Math.random()));
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
	public void setVolume(float volume) {
		for( int i=0; i<source.length; i++ )
			alSourcef( source[i], AL_GAIN, volume);
	}
	
	@Override
	public int[] getSources() { return source.clone(); }
	
	/**
	 * Changes the position of all the sources. 
	 * Only works for spatialized sounds.
	 * @param pos new position
	 */
	public void setPosition( Vec3f pos ) {
		if( spatialized )
			for( int i=0; i<source.length; i++ )
				alSource3f( source[i], AL_POSITION, pos.x, pos.y, pos.z );
	}
	
	/**
	 * Changes the position of all the sources. 
	 * Z is set to 0. 
	 * Only works for spatialized sounds.
	 * @param pos new position
	 */
	public void setPosition( Vec2f pos ) {
		if( spatialized )
			for( int i=0; i<source.length; i++ )
				alSource3f( source[i], AL_POSITION, pos.x, pos.y, 0 );
	}

}
