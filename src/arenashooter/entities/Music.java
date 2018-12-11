package arenashooter.entities;

import arenashooter.engine.audio.MusicPlayer;

public class Music extends Entity {
	private MusicPlayer music;
	
	public Music(String path, boolean looping) {
		music = new MusicPlayer(path, looping);
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
