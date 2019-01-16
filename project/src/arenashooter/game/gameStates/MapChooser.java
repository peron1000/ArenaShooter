package arenashooter.game.gameStates;

import arenashooter.game.GameMaster;

public class MapChooser extends GameState {
	public final static MapChooser mapChooser = new MapChooser();

	private String mapChosen;

	public String getMapChoosen() {
		return mapChosen;
	}

	@Override
	public void update(double delta) {
		// TODO Auto-generated method stub
		GameMaster.gm.requestNextState();
	}

}
