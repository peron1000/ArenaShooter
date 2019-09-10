package arenashooter.engine.audio;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import arenashooter.engine.math.QuatI;
import arenashooter.engine.math.Vec2fi;
import arenashooter.engine.math.Vec3fi;

/**
 * Silent audio implementation
 */
public class NoSound implements AudioManager {

	private float mainVolume = 1;
	private Map<AudioChannel, Float> channels;
	
	public static final Logger log;

	static {
		log = LogManager.getLogger("Audio");
	}
	
	public NoSound() {
		channels = new HashMap<>();
		for( AudioChannel c : AudioChannel.values() )
			channels.put(c, 1f);
	}

	@Override
	public void init() {
		log.info("Initializing (no sound)");
	}

	@Override
	public void destroy() {}

	@Override
	public void update() {}

	@Override
	public Logger getLogger() {
		return log;
	}

	@Override
	public void setListener(Vec3fi loc, QuatI rot) {}

	@Override
	public void playSound(String file, AudioChannel channel, float volume, float pitch) {}

	@Override
	public void playSound2D(String file, AudioChannel channel, float volume, float pitch, Vec2fi position) {}

	@Override
	public SoundBuffer loadSound(String path) {
		return null;
	}

	@Override
	public SoundSource createSource(String file, AudioChannel channel, float volume, float pitch) {
		return new NSSoundSource(channel);
	}

	@Override
	public float getMainVolume() { return mainVolume; }
	
	@Override
	public void setMainVolume(float newVolume) {
		mainVolume = newVolume;
	}
	
	@Override
	public float getChannelVolume(AudioChannel channel) { return channels.get(channel); }
	
	@Override
	public void setChannelVolume(AudioChannel channel, float newVolume) {
		channels.put(channel, Math.max(0, newVolume));
	}
	
	private class NSSoundSource implements SoundSource {
		private float volume = 1, pitch = 1;
		private final AudioChannel channel;
		private boolean playing = false;
		
		NSSoundSource(AudioChannel channel) {
			this.channel = channel;
		}
		
		@Override
		public void destroy() {}

		@Override
		public boolean isPlaying() {
			return playing;
		}

		@Override
		public void play() {
			playing = true;
		}

		@Override
		public void pause() {
			playing = false;
		}

		@Override
		public void stop() {
			playing = false;
		}

		@Override
		public void setPosition2D(Vec2fi position) {}

		@Override
		public void setPosition3D(Vec3fi position) {}

		@Override
		public AudioChannel getChannel() {
			return channel;
		}

		@Override
		public float getVolume() {
			return volume;
		}

		@Override
		public void setVolume(float newVolume) {
			volume = newVolume;
		}

		@Override
		public float getPitch() {
			return pitch;
		}

		@Override
		public void setPitch(float newPitch) {
			pitch = newPitch;
		}

		@Override
		public boolean isLooping() {
			return false;
		}

		@Override
		public void setLooping(boolean isLooping) {}

		@Override
		public void setBuffer(SoundBuffer buffer) {}

	}
}
