package arenashooter.entities;

import arenashooter.engine.audio.SoundEffect;
import arenashooter.engine.math.Vec2f;

public class SoundPlayer extends Spatial {
	private SoundEffect sound;

	public SoundPlayer( Vec2f position, String path ) {
		sound = new SoundEffect(path);
	}
	
	public void play() {
		sound.play();
	}
	
	public void pause() {
		sound.pause();
	}
	
	public void stop() {
		sound.stop();
	}

}
