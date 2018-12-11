package arenashooter.entities;

import arenashooter.engine.audio.SoundPlayer;
import arenashooter.engine.math.Vec2f;

public class SoundEffect extends Spatial {
	private SoundPlayer sound;

	public SoundEffect( Vec2f position, String path ) {
		sound = new SoundPlayer(path, 4);
	}
	
	public void play() {
		sound.play();
	}
	
	public boolean isPlaying() {
		return sound.isPlaying();
	}
	
	public void stop() {
		sound.stop();
	}

}
