package arenashooter.game.gameStates;

import arenashooter.game.GameMaster;

public class MapChooser extends GameState{
	public final static MapChooser mapChooser = new MapChooser();
	
	private String mapChoosen;
	
	public String getMapChoosen() {
		return mapChoosen;
	}

	@Override
	public void update(double delta) {
		// TODO Auto-generated method stub
		GameMaster.gm.requestNextState();
	}

}
