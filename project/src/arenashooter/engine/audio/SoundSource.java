package arenashooter.engine.audio;

import static org.lwjgl.openal.AL10.AL_BUFFER;
import static org.lwjgl.openal.AL10.AL_FALSE;
import static org.lwjgl.openal.AL10.AL_GAIN;
import static org.lwjgl.openal.AL10.AL_PITCH;
import static org.lwjgl.openal.AL10.AL_LOOPING;
import static org.lwjgl.openal.AL10.AL_TRUE;
import static org.lwjgl.openal.AL10.alGenSources;
import static org.lwjgl.openal.AL10.alGetSourcei;
import static org.lwjgl.openal.AL10.alDeleteSources;
import static org.lwjgl.openal.AL10.AL_PLAYING;
import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.AL_SOURCE_RELATIVE;
import static org.lwjgl.openal.AL10.AL_SOURCE_STATE;
import static org.lwjgl.openal.AL10.alSource3f;
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.AL10.alSourcePause;
import static org.lwjgl.openal.AL10.alSourceStop;
import static org.lwjgl.openal.AL10.alSourcef;
import static org.lwjgl.openal.AL10.alSourcei;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;

public class SoundSource {
	private final int sourceId;
	
	private float volume = 1, pitch = 1;
	private boolean looping = false;
	
	private final AudioChannel channel;
	
	SoundSource(String path, AudioChannel channel) {
		this.channel = channel;
		this.sourceId = alGenSources();

		if(Audio.printError("Cannot create source for \""+path+"\"") != AL10.AL_NO_ERROR)
			Audio.cleanBuffers();
		
		alSource3f( sourceId, AL_POSITION, 0, 0, 0 );
		alSourcei( sourceId, AL_SOURCE_RELATIVE, AL_TRUE );
		alSourcef( sourceId, AL11.AL_REFERENCE_DISTANCE, 0 );
		alSourcef( sourceId, AL11.AL_ROLLOFF_FACTOR, 0 );
	}
	
	/**
	 * Destroy this source and unregister it from Audio manager
	 */
	public void destroy() {
		alDeleteSources(sourceId);
		Audio.unregisterSource(this);
	}
	
	public boolean isPlaying() { return alGetSourcei(sourceId, AL_SOURCE_STATE) == AL_PLAYING; }
	
	public void play() { alSourcePlay(sourceId); }
	
	public void pause() { alSourcePause(sourceId); }
	
	public void stop() { alSourceStop(sourceId); }
	
	public void setPosition2D(Vec2f position) {
		if(position == null) {
			alSource3f( sourceId, AL_POSITION, 0, 0, 0 );
			alSourcei( sourceId, AL_SOURCE_RELATIVE, AL_TRUE );
			alSourcef( sourceId, AL11.AL_REFERENCE_DISTANCE, 0 );
			alSourcef( sourceId, AL11.AL_ROLLOFF_FACTOR, 0 );
		} else {
			alSource3f( sourceId, AL_POSITION, position.x, position.y, 0 );
			alSourcei( sourceId, AL_SOURCE_RELATIVE, AL_FALSE );
			alSourcef( sourceId, AL11.AL_REFERENCE_DISTANCE, 10 );
			alSourcef( sourceId, AL11.AL_ROLLOFF_FACTOR, .0055f );
		}
	}
	
	public void setPosition3D(Vec3f position) {
		if(position == null) {
			alSource3f( sourceId, AL_POSITION, 0, 0, 0 );
			alSourcei( sourceId, AL_SOURCE_RELATIVE, AL_TRUE );
			alSourcef( sourceId, AL11.AL_REFERENCE_DISTANCE, 0 );
			alSourcef( sourceId, AL11.AL_ROLLOFF_FACTOR, 0 );
		} else {
			alSource3f( sourceId, AL_POSITION, position.x, position.y, position.z );
			alSourcei( sourceId, AL_SOURCE_RELATIVE, AL_FALSE );
			alSourcef( sourceId, AL11.AL_REFERENCE_DISTANCE, 10 );
			alSourcef( sourceId, AL11.AL_ROLLOFF_FACTOR, .0055f );
		}
	}
	
	public AudioChannel getChannel() { return channel; }
	
	public float getVolume() { return volume; }
	
	public void setVolume(float newVolume) {
		volume = newVolume;
		updateVolume();
	}
	
	protected void updateVolume() {
		alSourcef(sourceId, AL_GAIN, volume*channel.volume*Audio.getMainVolume());
	}
	
	public float getPitch() { return pitch; }
	
	public void setPitch(float newPitch) {
		pitch = newPitch;
		alSourcef(sourceId, AL_PITCH, newPitch);
	}
	
	public boolean isLooping() { return looping; }
	
	public void setLooping(boolean isLooping) {
		looping = isLooping;
		alSourcei(sourceId, AL_LOOPING, AL_TRUE);
	}
	
	public void setBuffer(SoundBuffer buffer) {
		alSourcei(sourceId, AL_BUFFER, buffer.getBufferId());
	}
	
	int getSourceId() { return sourceId; }
}
