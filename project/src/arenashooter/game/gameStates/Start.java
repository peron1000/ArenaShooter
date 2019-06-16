package arenashooter.game.gameStates;

import arenashooter.game.Main;

/**
 * Startup State
 */
public class Start extends GameState {
	public Start() {
		super(1);
	}

	@Override
	public void update(double delta) {
		Main.getGameMaster().requestNextState(new Intro(), "data/mapXML/menu_intro.xml");
	}

}
