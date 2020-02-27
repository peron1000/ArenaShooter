package arenashooter.engine.audio.openAL;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.stb.STBVorbisInfo;

import arenashooter.engine.FileUtils;
import arenashooter.engine.audio.SoundBuffer;

import static org.lwjgl.openal.AL10.alGenBuffers;
import static org.lwjgl.openal.AL10.alBufferData;
import static org.lwjgl.openal.AL10.AL_FORMAT_MONO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.stb.STBVorbis.*;

/**
 * Container for OpenAL buffer
 */
class ALSoundBuffer implements SoundBuffer {
	private static Map<String, ALSoundBuffer> buffers = new HashMap<>();
	
	private int bufferId;

	private final String path;
	private boolean ready = false;
	private final int channels;
	private final int sampleRate;
	private ShortBuffer pcm;

	private ALSoundBuffer(String path, ShortBuffer pcm, int channels, int sampleRate) {
		this.bufferId = -2;
		this.pcm = pcm;
		this.channels = channels;
		this.path = path;
		this.sampleRate = sampleRate;
	}

	/**
	 * Load a sound from a file or cache <br/>
	 * This is safe to call from any thread
	 * @param path sound file
	 * @return buffer object (new or existing if already loaded)
	 */
	static SoundBuffer loadSound(String path) {
		SoundBuffer snd = buffers.get(path);
		if(snd != null) return snd;

		long time = System.currentTimeMillis();
		
		try(STBVorbisInfo info = STBVorbisInfo.malloc()) {
			ShortBuffer pcm = loadVorbis(path, info);

			if( pcm == null ) {
				ALAudio.log.error("Cannot load sound : "+path);
				return null;
			}

			ALSoundBuffer res = new ALSoundBuffer(path, pcm, info.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, info.sample_rate());
			buffers.put(res.path, res);
			
			ALAudio.log.debug("Loading new sound \""+path+"\": "+(System.currentTimeMillis() - time)+" ms");
			return res;
		}
	}
	
	private void initSoundBuffer() {
		if(ready) return;
		ready = true;
		
		bufferId = alGenBuffers();
		
		if(ALAudio.printError("Cannot create buffer for "+path) != AL10.AL_NO_ERROR)
			ALAudio.cleanBuffers();
		
		alBufferData(bufferId, channels, pcm, sampleRate);
	}
	
	/**
	 * Only call this from a thread with an openal context
	 * @return
	 */
	protected int getBufferId() {
		if(!ready) initSoundBuffer();
		return bufferId;
	}
	
	/**
	 * @return path to the sound file
	 */
	protected String getPath() {
		return path;
	}
	
	private static ShortBuffer loadVorbis(String path, STBVorbisInfo info) {
		ByteBuffer vorbis;
		
		vorbis = FileUtils.resToByteBuffer(path);
		
		if( vorbis == null )
			return null;
		
		IntBuffer error = BufferUtils.createIntBuffer(1);
		long decoder = stb_vorbis_open_memory(vorbis, error, null);
		
		if(decoder == NULL) {
			ALAudio.log.error("Can't load vorbis file : "+path);
			return null;
		}
		
		stb_vorbis_get_info(decoder, info);
		
		int channels = info.channels();
		
		if( channels != 1 && channels != 2 ) {
			ALAudio.log.error("Unsupported channel count for "+path+" ("+channels+") !");
		}
		
		ShortBuffer pcm = BufferUtils.createShortBuffer( stb_vorbis_stream_length_in_samples(decoder) * channels );
		
		stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm);
		stb_vorbis_close(decoder);
		
		return pcm;
	}
}
