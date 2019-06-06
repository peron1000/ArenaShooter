package arenashooter.engine.animation.animevents;

import arenashooter.engine.audio.AudioChannel;

public class AnimEventSound extends AnimEvent {
	public final String path;
	public final float volume, pitch;
	public final AudioChannel channel;
	public final boolean spatialized;
	
	public AnimEventSound(String path, AudioChannel channel, float volume, float pitch, boolean spatialized) {
		this.path = path;
		this.channel = channel;
		this.volume = volume;
		this.pitch = pitch;
		this.spatialized = spatialized;
	}

}
