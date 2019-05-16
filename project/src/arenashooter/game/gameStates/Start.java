package arenashooter.game.gameStates;

import arenashooter.game.GameMaster;

public class Start extends GameState {
	public Start() {
	}

	@Override
	public void update(double delta) {
		// TODO: update next instruction
		GameMaster.gm.requestNextState(new Intro(), "data/mapXML/empty.xml");
	}

}
