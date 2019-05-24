package arenashooter.engine.audio;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.stb.STBVorbisInfo;

import arenashooter.engine.FileUtils;

import static org.lwjgl.openal.AL10.alGenBuffers;
import static org.lwjgl.openal.AL10.alBufferData;
import static org.lwjgl.openal.AL10.AL_FORMAT_MONO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.stb.STBVorbis.*;

/**
 * Container for OpenAL buffer
 */
public class SoundBuffer {
	private int buffer;

	private SoundBuffer(int buffer) {
		this.buffer = buffer;
	}

	/**
	 * Load a sound file (ogg vorbis) into a buffer.
	 * @param path sound file
	 * @return buffer object (new or existing if already loaded)
	 */
	public static SoundBuffer loadSound(String path) {
		SoundBuffer snd = Audio.getSound(path);

		if( snd == null ) {
			try(STBVorbisInfo info = STBVorbisInfo.malloc()) {
				ShortBuffer pcm = loadVorbis(path, info);
				
				if( pcm == null ) {
					Audio.log.error("Cannot load sound : "+path);
					return null;
				}
				
				int buffer = alGenBuffers();
				
				if(Audio.printError("Cannot create buffer for "+path) != AL10.AL_NO_ERROR)
					Audio.cleanAll();
				
				alBufferData(buffer, info.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, pcm, info.sample_rate());
				
				SoundBuffer res = new SoundBuffer(buffer);
				Audio.registerSound(path, res);
				return res;
				
			}
		}
		
		return snd;
	}
	
	protected int getBuffer() { return buffer; }
	
	private static ShortBuffer loadVorbis(String resource, STBVorbisInfo info) {
		ByteBuffer vorbis;
		
		vorbis = FileUtils.resToByteBuffer(resource);
		
		if( vorbis == null )
			return null;
		
		IntBuffer error = BufferUtils.createIntBuffer(1);
		long decoder = stb_vorbis_open_memory(vorbis, error, null);
		
		if(decoder == NULL) {
			Audio.log.error("Can't load vorbis file : "+resource);
			return null;
		}
		
		stb_vorbis_get_info(decoder, info);
		
		int channels = info.channels();
		
		if( channels != 1 && channels != 2 ) {
			Audio.log.error("Unsupported channel count for "+resource+" ("+channels+") !");
		}
		
		ShortBuffer pcm = BufferUtils.createShortBuffer( stb_vorbis_stream_length_in_samples(decoder) * channels );
		
		stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm);
		stb_vorbis_close(decoder);
		
		return pcm;
	}
}
