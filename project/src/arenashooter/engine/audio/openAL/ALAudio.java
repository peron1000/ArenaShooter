package arenashooter.engine.audio.openAL;

import static org.lwjgl.openal.AL10.alListener3f;
import static org.lwjgl.openal.AL10.alListenerfv;
import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.AL_ORIENTATION;
import static org.lwjgl.openal.ALC11.*;
import static org.lwjgl.openal.EXTThreadLocalContext.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALUtil;

import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.audio.AudioManager;
import arenashooter.engine.audio.SoundBuffer;
import arenashooter.engine.audio.SoundSource;
import arenashooter.engine.math.QuatI;
import arenashooter.engine.math.Vec2fi;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec3fi;

/**
 * OpenAL audio implementation
 */
public class ALAudio implements AudioManager{
	private long device, context;
	
	private float mainVolume = 1;
	private Map<AudioChannel, Float> channels;

	private Set<ALSoundSource> sources = new HashSet<>();
	private Set<ALSoundSource> autoDestroySources = new HashSet<>();
	
	private FloatBuffer listenerRot;
	
	static final Logger log;
	
	static {
		log = LogManager.getLogger("Audio");
	}
	
	@Override
	public Logger getLogger() { return log; }
	
	public ALAudio() {
		channels = new HashMap<>();
		for( AudioChannel c : AudioChannel.values() )
			channels.put(c, 1f);
	}

	@Override
	public void init() {
		log.info("Initializing (OpenAL)");
		
		//Get default device
		device = alcOpenDevice( (ByteBuffer)null );
		
		if( device == NULL ) {
			log.fatal("Failed to open default device");
		}
		
		//Create device capabilities
		ALCCapabilities deviceCapabilities = ALC.createCapabilities(device);
		
		if( !deviceCapabilities.OpenALC10 ) {
			log.fatal("Failed to create device capabilities");
		}

		//Create context
		context = alcCreateContext( device, (IntBuffer)null );
		alcSetThreadContext(context);
		AL.createCapabilities(deviceCapabilities);
		
		AL11.alDistanceModel( AL11.AL_INVERSE_DISTANCE );
		
		printInitInfo( deviceCapabilities );
		
		listenerRot = BufferUtils.createFloatBuffer(6);
	}
	
	@Override
	public void destroy() {
		log.info("Stopping");
		
		alcMakeContextCurrent(NULL);
		alcDestroyContext(context);
		alcCloseDevice(device);
	}
	
	@Override
	public void update() {
		//Clean-up sources
		List<SoundSource> toDestroy = autoDestroySources.stream().filter(p -> !p.isPlaying()).collect(Collectors.toList());
		for( SoundSource source : toDestroy ) {
			source.destroy();
			autoDestroySources.remove(source);
		}
	}
	
	/**
	 * Remove a source from collection
	 * @param alSoundSource
	 */
	void unregisterSource(ALSoundSource alSoundSource) {
		sources.remove(alSoundSource);
	}
	
	@Override
	public void setListener(Vec3fi loc, QuatI rot) {
		alListener3f( AL_POSITION, loc.x(), loc.y(), loc.z() );
		
		Vec3f forward = Vec3f.multiply(rot.forward(), -1 );
		Vec3f up = rot.up();
		
		((Buffer)listenerRot).clear();
		listenerRot.put(forward.x);
		listenerRot.put(forward.y);
		listenerRot.put(forward.z);
		listenerRot.put(up.x);
		listenerRot.put(up.y);
		listenerRot.put(up.z);
		((Buffer)listenerRot).flip();
		
		alListenerfv( AL_ORIENTATION, listenerRot );
	}
	
	@Override
	public void playSound(String file, AudioChannel channel, float volume, float pitch) {
		if(getChannelVolume(channel) <= 0 || volume <= 0 || file == null || file.isEmpty()) return;
		
		SoundBuffer buf = loadSound(file);
		if(buf == null) {
			log.error("Cannot play sound \""+file+"\"");
			return;
		}
		
		ALSoundSource source = new ALSoundSource(this, file, channel);
		
		source.setBuffer(buf);
		source.setPitch(pitch);
		source.setVolume(volume);
		
		source.play();

		//Add source to collections
		sources.add(source);
		autoDestroySources.add(source);
	}
	
	@Override
	public void playSound2D(String file, AudioChannel channel, float volume, float pitch, Vec2fi position) {
		if(getChannelVolume(channel) <= 0 || volume <= 0 ||  file == null || file.isEmpty()) return;
		
		SoundBuffer buf = loadSound(file);
		if(buf == null) {
			log.error("Cannot play sound \""+file+"\"");
			return;
		}
		
		ALSoundSource source = new ALSoundSource(this, file, channel);
		
		source.setBuffer(buf);
		source.setPitch(pitch);
		source.setVolume(volume);
		source.setPosition2D(position);
		
		source.play();
		
		//Add source to collections
		sources.add(source);
		autoDestroySources.add(source);
	}
	
	@Override
	public SoundSource createSource(String file, AudioChannel channel, float volume, float pitch) {
		if(file == null || file.isEmpty()) return null;
		
		SoundBuffer buf = loadSound(file);
		if(buf == null) {
			log.error("Cannot load sound \""+file+"\"");
			return null;
		}
		
		ALSoundSource source = new ALSoundSource(this, file, channel);

		source.setBuffer(buf);
		source.setPitch(pitch);
		source.setVolume(volume);
		
		//Add source to collection
		sources.add(source);
		
		return source;
	}
	
	@Override
	public float getMainVolume() { return mainVolume; }
	
	@Override
	public void setMainVolume(float newVolume) {
		mainVolume = newVolume;
		
		for(ALSoundSource source : sources)
			source.updateVolume();
	}
	
	@Override
	public float getChannelVolume(AudioChannel channel) { return channels.get(channel); }
	
	@Override
	public void setChannelVolume(AudioChannel channel, float newVolume) {
		channels.put(channel, Math.max(0, newVolume));

		for(ALSoundSource source : sources) {
			if(source.getChannel() == channel)
				source.updateVolume();
		}
	}

	@Override
	public SoundBuffer loadSound(String path) {
		return ALSoundBuffer.loadSound(path);
	}
	
	private void printInitInfo(ALCCapabilities deviceCapabilities) {
		log.debug("OpenALC10: "+deviceCapabilities.OpenALC10);
		log.debug("OpenALC11: "+deviceCapabilities.OpenALC11);
		log.debug("caps.ALC_EXT_EFX: "+deviceCapabilities.ALC_EXT_EFX);
		
		if( deviceCapabilities.OpenALC11 ) {
			List<String> devices = ALUtil.getStringList(NULL, ALC_ALL_DEVICES_SPECIFIER);
			
			if( devices != null ) {
				log.debug("Available devices :");
				for( int i=0; i<devices.size(); i++ )
					log.debug("Device "+i+": "+devices.get(i));
			}
		}
		String defaultDevice = alcGetString(NULL, ALC_DEFAULT_DEVICE_SPECIFIER);
		if( defaultDevice != null )
			log.info("Default device : "+defaultDevice);
		
		log.debug( "Frequency : "+alcGetInteger(device, ALC_FREQUENCY)+"Hz" );
		log.debug( "Refresh rate : "+alcGetInteger(device, ALC_REFRESH)+"Hz" );
		log.debug( "Sync : "+(alcGetInteger(device, ALC_SYNC) == ALC_TRUE) );
		log.debug( "Mono sources : "+alcGetInteger(device, ALC_MONO_SOURCES) );
		log.debug( "Stereo sources : "+alcGetInteger(device, ALC_STEREO_SOURCES) );
	}
	
	/**
	 * Print OpenAL error message
	 * @param additionnalMsg additionnal error message to print, leave null if none
	 * @return openAL error code
	 */
	public static int printError(String additionnalMsg) {
		int error = AL10.alGetError();
		
		if(error != AL10.AL_NO_ERROR) {
			if(additionnalMsg != null) log.error(additionnalMsg+": "+AL10.alGetString(error));
			else log.error( AL10.alGetString(error) );
		}
		
		return error;
	}
	
	/**
	 * Remove unused buffers from memory
	 */
	public static void cleanBuffers() {
		log.info("Cleaning buffers...");
		
		log.warn("Not implemented yet");
		//TODO
		
//		log.info("Cleaned up "+toRemove.size()+" buffers.");
	}
}
