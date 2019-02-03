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
 * Object used to manage a sound with a single source. 
 */
public class SoundSourceSingle implements AudioSourceI {
	private SoundBuffer sound;
	private int source;
	private float pitchMin, pitchMax;
	private boolean spatialized = false;
	
	/**
	 * 
	 * @param path path to the sound file (vorbis)
	 * @param maxPlays maximum simultaneous plays ( must be >0 )
	 */
	public SoundSourceSingle(String path, float pitchMin, float pitchMax, boolean spatialized, boolean loop) {
		sound = SoundBuffer.loadSound(path);
		
		if( sound == null ) return;
		
		this.pitchMin = pitchMin;
		this.pitchMax = pitchMax;
		this.spatialized = spatialized;
		
		source = alGenSources();

		if(Audio.printError("Audio - Error creating source for "+path) != AL10.AL_NO_ERROR)
			Audio.cleanAll();

		//Link the source to the buffer
		alSourcei( source, AL_BUFFER, sound.getBuffer() );

		//Spatialization
		if( spatialized ) {
			alSourcei( source, AL_SOURCE_RELATIVE, AL_FALSE );
			alSourcef( source, AL11.AL_REFERENCE_DISTANCE, 10 );
			alSourcef( source, AL11.AL_ROLLOFF_FACTOR, .0055f );
		} else {
			alSourcei( source, AL_SOURCE_RELATIVE, AL_TRUE );
			alSourcef( source, AL11.AL_REFERENCE_DISTANCE, 0 );
			alSourcef( source, AL11.AL_ROLLOFF_FACTOR, 0 );
		}
		
		if( loop )
			alSourcei( source, AL11.AL_LOOPING, AL_TRUE );
		
		Audio.registerPlayer(this);
	}
	
	/**
	 * Play this sound using a new source or by replacing the oldest one
	 */
	@Override
	public void play() {
		if( sound == null ) return;
		alSourcef( source, AL_PITCH, Utils.lerpF(pitchMin, pitchMax, (float)Math.random()));
		alSourcePlay(source);
	}
	
	/**
	 * Play this sound using a new source or by replacing the oldest one. 
	 * Z is set to 0. 
	 * Only works for spatialized sounds.
	 * @param the world space location of the new source
	 */
	public void play(Vec2f pos) {
		if( sound == null ) return;
		alSourcef( source, AL_PITCH, Utils.lerpF(pitchMin, pitchMax, (float)Math.random()));
		alSource3f( source, AL_POSITION, pos.x, pos.y, 0 );
		alSourcePlay(source);
	}
	
	/**
	 * Stop all sources of this sound
	 */
	@Override
	public void stop() {
		if( sound == null ) return;
		alSourceStop(source);
	}
	
	/**
	 * Pause all sources of this sound
	 */
	public void pause() {
		if( sound == null ) return;
		alSourcePause(source);
	}
	
	/**
	 * Is this sound playing
	 * @return at least one source of this sound is playing
	 */
	@Override
	public boolean isPlaying() {
		if( sound == null ) return false;
		
		return alGetSourcei(source, AL_SOURCE_STATE) == AL_PLAYING;
	}
	
	@Override
	public void setVolume(float volume) {
		alSourcef( source, AL_GAIN, volume);
	}
	
	public void setPitch(float pitch) {
		alSourcef( source, AL_PITCH, pitch );
	}
	
	@Override
	public int[] getSources() { return new int[] {source}; }
	
	/**
	 * Changes the position of all the sources. 
	 * Only works for spatialized sounds.
	 * @param pos new position
	 */
	public void setPosition( Vec3f pos ) {
		if( spatialized )
			alSource3f( source, AL_POSITION, pos.x, pos.y, pos.z );
	}
	
	/**
	 * Changes the position of all the sources. 
	 * Z is set to 0. 
	 * Only works for spatialized sounds.
	 * @param pos new position
	 */
	public void setPosition( Vec2f pos ) {
		if( spatialized )
			alSource3f( source, AL_POSITION, pos.x, pos.y, 0 );
	}
}
