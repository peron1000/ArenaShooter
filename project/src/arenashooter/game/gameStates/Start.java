package arenashooter.game.gameStates;

import arenashooter.game.GameMaster;

public class Start extends GameState {
	public Start() {
		super(1);
	}

	@Override
	public void update(double delta) {
		GameMaster.gm.requestNextState(new Intro(), "data/mapXML/menu_empty.xml");
	}

}
