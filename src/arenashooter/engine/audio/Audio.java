package arenashooter.engine.audio;

import static org.lwjgl.openal.AL10.alDeleteBuffers;
import static org.lwjgl.openal.ALC11.*;
import static org.lwjgl.openal.EXTThreadLocalContext.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALUtil;


public class Audio {
	private static long device, context;
	
	private static Map<String, SoundEntry> sounds = new HashMap<String, SoundEntry>();

	/**
	 * Initialize the audio system. Don't forget to destroy it !
	 * @param printInfo print informations (available devices, frequency ...)
	 */
	public static void init(boolean printInfo) {
		System.out.println("Audio - Initializing");
		
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
		
		if( printInfo ) printInitInfo( deviceCapabilities );
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
	
	private static void printInitInfo(ALCCapabilities deviceCapabilities) {
		System.out.println("Audio - OpenALC10: "+deviceCapabilities.OpenALC10);
		System.out.println("Audio - OpenALC11: "+deviceCapabilities.OpenALC11);
		System.out.println("Audio - caps.ALC_EXT_EFX: "+deviceCapabilities.ALC_EXT_EFX);
		
		if( deviceCapabilities.OpenALC11 ) {
			List<String> devices = ALUtil.getStringList(NULL, ALC_ALL_DEVICES_SPECIFIER);
			
			if( devices != null ) {
				System.out.println("Audio - Available devices :");
				for( int i=0; i<devices.size(); i++ )
					System.out.println("         "+i+": "+devices.get(i));
			}
		}
		String defaultDevice = alcGetString(NULL, ALC_DEFAULT_DEVICE_SPECIFIER);
		if( defaultDevice != null )
			System.out.println("Audio - Default device : "+defaultDevice);
		
		System.out.println( "Audio - Frequency : "+alcGetInteger(device, ALC_FREQUENCY)+"Hz" );
		System.out.println( "Audio - Refresh rate : "+alcGetInteger(device, ALC_REFRESH)+"Hz" );
		System.out.println( "Audio - Sync : "+(alcGetInteger(device, ALC_SYNC) == ALC_TRUE) );
		System.out.println( "Audio - Mono sources : "+alcGetInteger(device, ALC_MONO_SOURCES) );
		System.out.println( "Audio - Stereo sources : "+alcGetInteger(device, ALC_STEREO_SOURCES) );
	}
	
	protected static void registerSound(String file, Sound sound) {
		SoundEntry newEntry = new SoundEntry(file, sound);
		sounds.put(file, newEntry);
	}
	
	/**
	 * Get a sound from a filename if it is already loaded and available
	 * @param file
	 * @return the Sound, null if not loaded
	 */
	protected static Sound getSound(String file) {
		SoundEntry entry = sounds.get(file);
		if( entry != null )
			return entry.sound.get();
		return null;
	}
	
	/**
	 * Remove unused sounds from memory
	 */
	public static void cleanMemory() {
		ArrayList<String> toRemove = new ArrayList<String>(0);
		
		for ( SoundEntry entry : sounds.values() ) {
		    if( entry.sound.get() == null ) {
		    	toRemove.add(entry.file);
				alDeleteBuffers(entry.buffer);
		    }
		}
		
		for( String s : toRemove ) {
			sounds.remove(s);
		}
	}
	
	private static class SoundEntry {
		int buffer;
		String file;
		WeakReference<Sound> sound;
		
		SoundEntry(String file, Sound sound) {
			buffer = sound.getBuffer();
			this.file = file;
			this.sound = new WeakReference<Sound>(sound);
		}
	}
}
