package arenashooter.engine.audio.openAL;

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

import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.audio.SoundBuffer;
import arenashooter.engine.audio.SoundSource;
import arenashooter.engine.math.Vec2fi;
import arenashooter.engine.math.Vec3fi;

class ALSoundSource implements SoundSource {
	private final ALAudio manager;
	
	private final int sourceId;
	
	private float volume = 1, pitch = 1;
	private boolean looping = false;
	
	private final AudioChannel channel;
	
	ALSoundSource(ALAudio manager, String path, AudioChannel channel) {
		this.manager = manager;
		this.channel = channel;
		this.sourceId = alGenSources();

		if(ALAudio.printError("Cannot create source for \""+path+"\"") != AL10.AL_NO_ERROR)
			ALAudio.cleanBuffers();
		
		alSource3f( sourceId, AL_POSITION, 0, 0, 0 );
		alSourcei( sourceId, AL_SOURCE_RELATIVE, AL_TRUE );
		alSourcef( sourceId, AL11.AL_REFERENCE_DISTANCE, 0 );
		alSourcef( sourceId, AL11.AL_ROLLOFF_FACTOR, 0 );
	}
	
	@Override
	public void destroy() {
		alDeleteSources(sourceId);
		ALAudio.printError("Cannot destroy source "+sourceId);
		manager.unregisterSource(this);
	}

	@Override
	public boolean isPlaying() { return alGetSourcei(sourceId, AL_SOURCE_STATE) == AL_PLAYING; }

	@Override
	public void play() { alSourcePlay(sourceId); }

	@Override
	public void pause() { alSourcePause(sourceId); }

	@Override
	public void stop() { alSourceStop(sourceId); }

	@Override
	public void setPosition2D(Vec2fi position) {
		if(position == null) {
			alSource3f( sourceId, AL_POSITION, 0, 0, 0 );
			alSourcei( sourceId, AL_SOURCE_RELATIVE, AL_TRUE );
			alSourcef( sourceId, AL11.AL_REFERENCE_DISTANCE, 0 );
			alSourcef( sourceId, AL11.AL_ROLLOFF_FACTOR, 0 );
		} else {
			alSource3f( sourceId, AL_POSITION, position.x(), position.y(), 0 );
			alSourcei( sourceId, AL_SOURCE_RELATIVE, AL_FALSE );
			alSourcef( sourceId, AL11.AL_REFERENCE_DISTANCE, 10 );
			alSourcef( sourceId, AL11.AL_ROLLOFF_FACTOR, .0055f );
		}
	}

	@Override
	public void setPosition3D(Vec3fi position) {
		if(position == null) {
			alSource3f( sourceId, AL_POSITION, 0, 0, 0 );
			alSourcei( sourceId, AL_SOURCE_RELATIVE, AL_TRUE );
			alSourcef( sourceId, AL11.AL_REFERENCE_DISTANCE, 0 );
			alSourcef( sourceId, AL11.AL_ROLLOFF_FACTOR, 0 );
		} else {
			alSource3f( sourceId, AL_POSITION, position.x(), position.y(), position.z() );
			alSourcei( sourceId, AL_SOURCE_RELATIVE, AL_FALSE );
			alSourcef( sourceId, AL11.AL_REFERENCE_DISTANCE, 10 );
			alSourcef( sourceId, AL11.AL_ROLLOFF_FACTOR, .0055f );
		}
	}

	@Override
	public AudioChannel getChannel() { return channel; }

	@Override
	public float getVolume() { return volume; }

	@Override
	public void setVolume(float newVolume) {
		volume = newVolume;
		updateVolume();
	}
	
	protected void updateVolume() {
		alSourcef(sourceId, AL_GAIN, volume*channel.volume*manager.getMainVolume());
		ALAudio.printError("Cannot set volume value for source "+sourceId);
	}
	
	@Override
	public float getPitch() { return pitch; }
	
	@Override
	public void setPitch(float newPitch) {
		pitch = newPitch;
		alSourcef(sourceId, AL_PITCH, newPitch);
		ALAudio.printError("Cannot set pitch value for source "+sourceId);
	}
	
	@Override
	public boolean isLooping() { return looping; }
	
	@Override
	public void setLooping(boolean isLooping) {
		looping = isLooping;
		alSourcei(sourceId, AL_LOOPING, AL_TRUE);
		ALAudio.printError("Cannot set loop value for source "+sourceId);
	}
	
	@Override
	public void setBuffer(SoundBuffer buffer) {
		if( buffer instanceof ALSoundBuffer ) {
			alSourcei(sourceId, AL_BUFFER, ((ALSoundBuffer)buffer).getBufferId());
			ALAudio.printError("Cannot set buffer "+((ALSoundBuffer)buffer).getPath()+" for source "+sourceId);
		} else
			ALAudio.log.error("Sound buffer is not an OpenAL buffer");
	}
	
	int getSourceId() { return sourceId; }
}
