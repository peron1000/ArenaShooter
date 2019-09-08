package arenashooter.game.gameStates.loading;

import arenashooter.game.Main;

public class PreLoadMainSound extends Thread {
	
	@Override
	public void run() {
		Main.getAudioManager().loadSound("data/music/Super_blep_serious_fight.ogg");
	}

}
