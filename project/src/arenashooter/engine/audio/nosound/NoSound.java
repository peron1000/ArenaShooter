package arenashooter.engine.audio.nosound;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.audio.AudioManager;
import arenashooter.engine.audio.SoundBuffer;
import arenashooter.engine.audio.SoundSource;
import arenashooter.engine.math.QuatI;
import arenashooter.engine.math.Vec2fi;
import arenashooter.engine.math.Vec3fi;

public class NoSound implements AudioManager {

	private float mainVolume = 1;
	
	public static final Logger log;

	static {
		log = LogManager.getLogger("Audio");
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
	public float getChannelVolume(AudioChannel channel) { return channel.volume; }
	
	@Override
	public void setChannelVolume(AudioChannel channel, float newVolume) {
		channel.volume = Math.max(0, newVolume);
	}
}
