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
import static org.lwjgl.openal.AL10.alDeleteSources;

public class SoundEffect {
	Sound sound;
	int source;
	
	public SoundEffect(String path) {
		sound = Sound.loadSound(path);
		
		source = alGenSources();
		
		alSourcei( source, AL_BUFFER, sound.getBuffer() );
	}
	
	public void play() {
		alSourcePlay(source);
	}
	
	public void pause() {
		alSourcePause(source);
	}
	
	public void stop() {
		alSourceStop(source);
	}
	
	public boolean isPlaying() {
		return alGetSourcei(source, AL_SOURCE_STATE) == AL_PLAYING;
	}
	
	public void destroy() {
		alDeleteSources(source);
	}

}
