package arenashooter.engine.audio;

import static org.lwjgl.openal.AL10.alDeleteBuffers;
import static org.lwjgl.openal.AL10.alDeleteSources;
import static org.lwjgl.openal.AL10.alListener3f;
import static org.lwjgl.openal.AL10.alListenerfv;
import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.AL_ORIENTATION;
import static org.lwjgl.openal.ALC11.*;
import static org.lwjgl.openal.EXTThreadLocalContext.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
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

import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;

/**
 * Audio manager
 */
public final class Audio {
	private static long device, context;
	
	private static float mainVolume = 1;
	
	private static Map<String, BufferEntry> buffers = new HashMap<>();
	private static List<SourceEntry> sourcesOld = new ArrayList<>();
	private static Set<SoundSource> sources = new HashSet<>();
	private static Set<SoundSource> autoDestroySources = new HashSet<>();
	
	public static final Logger log = LogManager.getLogger("Audio");
	
	private static FloatBuffer listenerRot;

	//This class cannot be instantiated
	private Audio() {}
	
	/**
	 * Initialize the audio system. Don't forget to destroy it !
	 */
	public static void init() {
		log.info("Initializing");
		
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
	
	/**
	 * Destroy the audio system
	 */
	public static void destroy() {
		log.info("Stopping");
		
		alcMakeContextCurrent(NULL);
		alcDestroyContext(context);
		alcCloseDevice(device);
	}
	
	/**
	 * This does not need to be called at every sub-step
	 */
	public static void update() {
		//Clean-up sources
		List<SoundSource> toDestroy = autoDestroySources.stream().filter(p -> !p.isPlaying()).collect(Collectors.toList());
		for( SoundSource source : toDestroy ) {
			source.destroy();
			autoDestroySources.remove(source);
		}
	}
	
	/**
	 * Remove a source from collection
	 * @param source
	 */
	static void unregisterSource(SoundSource source) {
		sources.remove(source);
	}
	
	/**
	 * Set the listener ("ears") to a specific location/rotation
	 * @param loc
	 * @param rot
	 */
	public static void setListener(Vec3f loc, Quat rot) {
		alListener3f( AL_POSITION, loc.x, loc.y, loc.z );
		
		Vec3f forward = Vec3f.multiply(rot.forward(), -1 );
		Vec3f up = rot.up();
		
		listenerRot.clear();
		listenerRot.put(forward.x);
		listenerRot.put(forward.y);
		listenerRot.put(forward.z);
		listenerRot.put(up.x);
		listenerRot.put(up.y);
		listenerRot.put(up.z);
		listenerRot.flip();
		
		alListenerfv( AL_ORIENTATION, listenerRot );
	}
	
	/**
	 * Play a sound (non-spatialized)
	 * @param file
	 * @param channel
	 * @param volume
	 * @param pitch
	 */
	public static void playSound(String file, AudioChannel channel, float volume, float pitch) {
		if(channel.volume <= 0 || volume <= 0 || file == null || file.isEmpty()) return;
		
		SoundBuffer buf = SoundBuffer.loadSound(file);
		if(buf == null) {
			log.error("Cannot play sound \""+file+"\"");
			return;
		}
		
		SoundSource source = new SoundSource(channel);
		
		source.setBuffer(buf);
		source.setPitch(pitch);
		source.setVolume(volume);
		
		source.play();

		//Add source to collections
		sources.add(source);
		autoDestroySources.add(source);
	}
	
	/**
	 * Play a sound (spatialized)
	 * @param file
	 * @param channel
	 * @param volume
	 * @param pitch
	 * @param position
	 */
	public static void playSound2D(String file, AudioChannel channel, float volume, float pitch, Vec2f position) {
		if(channel.volume <= 0 || volume <= 0 ||  file == null || file.isEmpty()) return;
		
		SoundBuffer buf = SoundBuffer.loadSound(file);
		if(buf == null) {
			log.error("Cannot play sound \""+file+"\"");
			return;
		}
		
		SoundSource source = new SoundSource(channel);
		
		source.setBuffer(buf);
		source.setPitch(pitch);
		source.setVolume(volume);
		source.setPosition2D(position);
		
		source.play();
		
		//Add source to collections
		sources.add(source);
		autoDestroySources.add(source);
	}
	
	/**
	 * Create a new source
	 * @param file
	 * @param channel
	 * @return Source or null
	 */
	public static SoundSource createSource(String file, AudioChannel channel, float volume, float pitch) {
		if(file == null || file.isEmpty()) return null;
		
		SoundBuffer buf = SoundBuffer.loadSound(file);
		if(buf == null) {
			log.error("Cannot load sound \""+file+"\"");
			return null;
		}
		
		SoundSource source = new SoundSource(channel);

		source.setBuffer(buf);
		source.setPitch(pitch);
		source.setVolume(volume);
		
		//Add source to collection
		sources.add(source);
		
		return source;
	}
	
	public static float getMainVolume() { return mainVolume; }
	
	public static void setMainVolume(float newVolume) {
		mainVolume = newVolume;
		
		for(SoundSource source : sources)
			source.updateVolume();
	}
	
	public static float getChannelVolume(AudioChannel channel) { return channel.volume; }
	
	public static void setChannelVolume(AudioChannel channel, float newVolume) {
		channel.volume = Math.max(0, newVolume);

		for(SoundSource source : sources) {
			if(source.getChannel() == channel)
				source.updateVolume();
		}
	}
	
	private static void printInitInfo(ALCCapabilities deviceCapabilities) {
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
	
	protected static void registerSound(String file, SoundBuffer sound) {
		BufferEntry newEntry = new BufferEntry(file, sound);
		if(buffers.get(file) != null && buffers.get(file).sound.get() != null) log.error("Sound already registered: "+file);
		buffers.put(file, newEntry);
	}
	
	@Deprecated
	protected static void registerPlayer( MusicSource musicSource ) {
		sourcesOld.add( new SourceEntry(musicSource) );
	}
	
	/**
	 * Get a sound from a filename if it is already loaded and available
	 * @param file
	 * @return the Sound, null if not loaded
	 */
	protected static SoundBuffer getSound(String file) {
		BufferEntry entry = buffers.get(file);
		if( entry != null && entry.sound.get() != null )
			return entry.sound.get();
		return null;
	}
	
	/**
	 * Remove unused sound players from memory
	 */
	public static void cleanPlayers() {
		log.info("Cleaning players...");
		
		int sourcesRemoved = 0;
		for( int i=sourcesOld.size()-1; i>=0; i-- ) {
			if( sourcesOld.get(i).sound.get() == null ) {
				alDeleteSources( sourcesOld.get(i).sources);
				Audio.printError("Audio - Error deleting source(s)");
				sourcesOld.remove(i);
				sourcesRemoved++;
			}
		}
		
		log.info("Cleaned up "+sourcesRemoved+" sources.");
	}
	
	/**
	 * Remove unused buffers from memory
	 */
	public static void cleanBuffers() {
		log.info("Cleaning buffers...");
		
		ArrayList<String> toRemove = new ArrayList<String>(0);
		for ( BufferEntry entry : buffers.values() ) {
		    if( entry.sound.get() == null ) {
		    	toRemove.add(entry.file);
				alDeleteBuffers(entry.buffer);
		    }
		}
		
		for( String s : toRemove )
			buffers.remove(s);
		
		log.info("Cleaned up "+toRemove.size()+" buffers.");
	}
	
	/**
	 * Clean memory (buffers and sources)
	 */
	public static void cleanAll() {
		cleanBuffers();
		cleanPlayers();
	}
	
	private static class BufferEntry {
		int buffer;
		String file;
		WeakReference<SoundBuffer> sound;
		
		BufferEntry(String file, SoundBuffer sound) {
			buffer = sound.getBufferId();
			this.file = file;
			this.sound = new WeakReference<SoundBuffer>(sound);
		}
	}
	
	private static class SourceEntry {
		int sources[];
		WeakReference<MusicSource> sound;
		
		SourceEntry(MusicSource musicSource) {
			this.sources = musicSource.getSources();
			this.sound = new WeakReference<MusicSource>(musicSource);
		}
	}
}
