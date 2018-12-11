package arenashooter.engine.audio;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBVorbisInfo;

import arenashooter.engine.FileUtils;

import static org.lwjgl.openal.AL10.alGenBuffers;
import static org.lwjgl.openal.AL10.alBufferData;
import static org.lwjgl.openal.AL10.AL_FORMAT_MONO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.stb.STBVorbis.*;

public class Sound {
	private int buffer;

	private Sound(String path) {
		buffer = alGenBuffers();
		
		try(STBVorbisInfo info = STBVorbisInfo.malloc()) {
			ShortBuffer pcm = loadVorbis(path, info);
			
			alBufferData(buffer, info.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, pcm, info.sample_rate());
			
			Audio.registerSound(path, this);
		}
	}

	public static Sound loadSound(String path) {
		Sound snd = Audio.getSound(path);

		if( snd == null )
			return new Sound(path);
		
		return snd;
	}
	
	protected int getBuffer() { return buffer; }
	
	private ShortBuffer loadVorbis(String resource, STBVorbisInfo info) {
		ByteBuffer vorbis;
		
		vorbis = FileUtils.resToByteBuffer(resource);
		
		IntBuffer error = BufferUtils.createIntBuffer(1);
		long decoder = stb_vorbis_open_memory(vorbis, error, null);
		
		if(decoder == NULL) {
			System.err.println("Audio - Can't load vorbis file : "+resource);
			return null;
		}
		
		stb_vorbis_get_info(decoder, info);
		
		int channels = info.channels();
		
		if( channels != 1 && channels != 2 ) {
			System.err.println("Audio - Unsupported channel count for "+resource+" ("+channels+") !");
		}
		
		ShortBuffer pcm = BufferUtils.createShortBuffer( stb_vorbis_stream_length_in_samples(decoder) * channels );
		
		stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm);
		stb_vorbis_close(decoder);
		
		return pcm;
	}
}
