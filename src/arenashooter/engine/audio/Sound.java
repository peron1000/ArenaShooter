package arenashooter.engine.audio;

import static org.lwjgl.openal.AL10.alGenBuffers;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBVorbisInfo;

import arenashooter.engine.FileUtils;

import static org.lwjgl.openal.AL10.alDeleteBuffers;
import static org.lwjgl.openal.AL10.alBufferData;
import static org.lwjgl.openal.AL10.AL_FORMAT_MONO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;
import static org.lwjgl.stb.STBVorbis.*;

public class Sound {
	private String file;
	private int buffer;

	private Sound(String path) {
		file = path;
		buffer = alGenBuffers();
		
		try(STBVorbisInfo info = STBVorbisInfo.malloc()) {
			ShortBuffer pcm = loadVorbis(path, info);
			
			alBufferData(buffer, info.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, pcm, info.sample_rate());
		}
	}
	
	private ShortBuffer loadVorbis(String resource, STBVorbisInfo info) {
		ByteBuffer vorbis;
		
		vorbis = FileUtils.resToByteBuffer(resource);
		
		IntBuffer error = BufferUtils.createIntBuffer(1);
		long decoder = stb_vorbis_open_memory(vorbis, error, null);
		
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
	
	public Sound loadSound(String path) {
		Sound snd = Audio.soundBuffers.get(path);
		if( snd == null )
			snd = new Sound(path);
		
		return snd;
	}

	public void destroy() {
		Audio.soundBuffers.remove(file);
		alDeleteBuffers(buffer);
		
		buffer = 0;
		file = null;
	}
}
