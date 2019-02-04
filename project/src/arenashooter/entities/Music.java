package arenashooter.entities;

import arenashooter.engine.audio.MusicSource;

public class Music extends Entity {
	private MusicSource music;
	
	public Music(String path, boolean looping) {
		music = new MusicSource(path, looping);
	}
	
	public void setVolume(float volume) {
		music.setVolume(volume);
	}
	
	public void play() {
		music.play();
	}
	
	public void pause() {
		music.pause();
	}
	
	public void stop() {
		music.stop();
	}

}
