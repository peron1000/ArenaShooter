package arenashooter.engine.animation.animevents;

import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.math.Vec2fi;
import arenashooter.game.Main;

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
	
	/**
	 * Play this sound
	 * @param position ignored if spatialized = false (can be left to null in this case)
	 */
	public void play(Vec2fi position) {
		if(spatialized)
			Main.getAudioManager().playSound2D(path, channel, volume, pitch, position);
		else
			Main.getAudioManager().playSound(path, channel, volume, pitch);
	}

}
