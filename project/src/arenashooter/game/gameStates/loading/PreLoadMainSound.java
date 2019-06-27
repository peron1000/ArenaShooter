package arenashooter.game.gameStates.loading;

import arenashooter.engine.audio.SoundBuffer;

public class PreLoadMainSound extends Thread {
	
	@Override
	public void run() {
		SoundBuffer.loadSound("data/music/Super_blep_serious_fight.ogg");
	}

}
