package arenashooter.entities;

import arenashooter.engine.audio.SoundEffect;
import arenashooter.engine.math.Vec2f;

public class SoundPlayer extends Spatial {
	private SoundEffect sound;

	public SoundPlayer( Vec2f position, String path ) {
		sound = new SoundEffect(path, 4);
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
