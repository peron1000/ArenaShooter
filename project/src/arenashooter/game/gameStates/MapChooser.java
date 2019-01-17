package arenashooter.game.gameStates;

import arenashooter.game.GameMaster;

public class MapChooser extends GameState {
	private String mapChosen;

	public String getMapChoosen() {
		return "data/mapXML/mapXML.xml";
	}

	@Override
	public void update(double delta) {
		// TODO : choose a map
		GameMaster.gm.requestNextState();
	}

}
