package arenashooter.engine.audio.nosound;

import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.audio.SoundBuffer;
import arenashooter.engine.audio.SoundSource;
import arenashooter.engine.math.Vec2fi;
import arenashooter.engine.math.Vec3fi;

public class NSSoundSource implements SoundSource {
	private float volume = 1, pitch = 1;
	private final AudioChannel channel;
	
	NSSoundSource(AudioChannel channel) {
		this.channel = channel;
	}
	
	@Override
	public void destroy() {}

	@Override
	public boolean isPlaying() {
		return false;
	}

	@Override
	public void play() {}

	@Override
	public void pause() {}

	@Override
	public void stop() {}

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
