package arenashooter.game.gameStates;

import arenashooter.game.Main;

/**
 * Startup State
 */
public class Start extends GameState {
	@Override
	public void update(double delta) {
		Main.getGameMaster().requestNextTest();
	}

}
