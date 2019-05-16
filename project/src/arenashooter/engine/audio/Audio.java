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
import java.util.List;
import java.util.Map;

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
import arenashooter.engine.math.Vec3f;

/**
 * Audio manager
 */
public final class Audio {
	private static long device, context;
	
	private static Map<String, BufferEntry> sounds = new HashMap<String, BufferEntry>();
	private static List<SourceEntry> sources = new ArrayList<SourceEntry>();
	
	protected static Logger log = LogManager.getLogger("Audio");

	//This class cannot be instantiated
	private Audio() {}
	
	/**
	 * Initialize the audio system. Don't forget to destroy it !
	 */
	public static void init() {
		log.info("Initializing");
		
		device = alcOpenDevice( (ByteBuffer)null );
		
		if( device == NULL ) {
			throw new IllegalStateException("Audio - Failed to open the default device !");
		}
		
		ALCCapabilities deviceCapabilities = ALC.createCapabilities(device);
		
		if( !deviceCapabilities.OpenALC10 ) {
			throw new IllegalStateException("Audio - Failed to create device capabilities !");
		}

		context = alcCreateContext( device, (IntBuffer)null );
		alcSetThreadContext(context);
		AL.createCapabilities(deviceCapabilities);
		
		AL11.alDistanceModel( AL11.AL_INVERSE_DISTANCE );
		
		printInitInfo( deviceCapabilities );
	}
	
	/**
	 * Destroy the audio system
	 */
	public static void destroy() {
		System.out.println("Audio - Stopping");
		
		alcMakeContextCurrent(NULL);
		alcDestroyContext(context);
		alcCloseDevice(device);
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
		
		FloatBuffer listenerRot = BufferUtils.createFloatBuffer(6);
		listenerRot.put(forward.x);
		listenerRot.put(forward.y);
		listenerRot.put(forward.z);
		listenerRot.put(up.x);
		listenerRot.put(up.y);
		listenerRot.put(up.z);
		listenerRot.flip();
		
		alListenerfv( AL_ORIENTATION, listenerRot );
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
			if(additionnalMsg != null) log.info(additionnalMsg+AL10.alGetString(error));
			else log.error( AL10.alGetString(error) );
		}
		
		return error;
	}
	
	protected static void registerSound(String file, SoundBuffer sound) {
		BufferEntry newEntry = new BufferEntry(file, sound);
		if(sounds.get(file) != null && sounds.get(file).sound.get() != null) System.err.println("Audio - Sound already registered !");
		sounds.put(file, newEntry);
	}
	
	protected static void registerPlayer( AudioSourceI player ) {
		sources.add( new SourceEntry(player) );
	}
	
	/**
	 * Get a sound from a filename if it is already loaded and available
	 * @param file
	 * @return the Sound, null if not loaded
	 */
	protected static SoundBuffer getSound(String file) {
		BufferEntry entry = sounds.get(file);
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
		for( int i=sources.size()-1; i>=0; i-- ) {
			if( sources.get(i).sound.get() == null ) {
				alDeleteSources( sources.get(i).sources);
				Audio.printError("Audio - Error deleting source(s)");
				sources.remove(i);
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
		for ( BufferEntry entry : sounds.values() ) {
		    if( entry.sound.get() == null ) {
		    	toRemove.add(entry.file);
				alDeleteBuffers(entry.buffer);
		    }
		}
		
		for( String s : toRemove )
			sounds.remove(s);
		
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
			buffer = sound.getBuffer();
			this.file = file;
			this.sound = new WeakReference<SoundBuffer>(sound);
		}
	}
	
	private static class SourceEntry {
		int sources[];
		WeakReference<AudioSourceI> sound;
		
		SourceEntry(AudioSourceI player) {
			this.sources = player.getSources();
			this.sound = new WeakReference<AudioSourceI>(player);
		}
	}
}
